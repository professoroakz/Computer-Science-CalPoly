/* ----------------------------------------------------------------------
 MapReduce MPI Stock Analytics in C++
------------------------------------------------------------------------- */

#include <cmath>
#include <vector>
#include <cstdlib>
#include <sstream>
#include <float.h>
#include "mpi.h"
#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "sys/stat.h"
#include "mapreduce.h"
#include "keyvalue.h"

using namespace MAPREDUCE_NS;

struct MinMax {
    double min = DBL_MAX;
    double max = -DBL_MAX;
};

struct FileInput {
    std::string filename, flag;
};

struct Histogram {
    //Volume Traded, Adjusted Closing Price, and Daily Differential (Closing Price - Opening Price
    int volumeTraded, adjClosePrice, dailyDifferential;
};

/* Helper functions for readFile */

std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems);

std::vector<std::string> split(const std::string &s, char delim);

/* Mapper: Read input file an add key-value to KV-store */
void readFile(int itask, KeyValue *kv, void *ptr); // ptr to FileInput

/* Reducer: Calculate sum of multiple values of each unique key */
void sum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr);

/* Mapper: Map a key to it's interval with respect to 'h'. New key is interval-index */
void toInterval(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr); // ptr to h (int)

/* Mapper: Find min and max value */
void findMinMax(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr); // ptr to MinMax

/* Reducer: Calucalte sum of multiple values of each unique key */
void intervalSum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr);

/* ---------------------------------------------------------------------- */

int main(int narg, char **args)
{
  MPI_Init(&narg,&args);

  int my_id, nprocs;
  MPI_Comm_rank(MPI_COMM_WORLD,&my_id);
  MPI_Comm_size(MPI_COMM_WORLD,&nprocs);

  if (narg <= 2) {
    if (my_id == 0) printf("Syntax: ./stock_histograms [file] [flag]\n\tflags:\tv = Volume Traded (default)\n\t\ta = Adjusted Closing Price\n\t\td = Daily Differential (Closing Price - Opening Price)");
    MPI_Abort(MPI_COMM_WORLD,1);
  }

    std::string filename(args[1]);
    std::string flag(args[2]);
    FileInput fileInput;
    fileInput.filename = filename;
    fileInput.flag = flag;

  MapReduce *mr = new MapReduce(MPI_COMM_WORLD);
//  mr->verbosity = 2;
  mr->timer = 1;
  //mr->memsize = 1;
  //mr->outofcore = 1;

  MPI_Barrier(MPI_COMM_WORLD);

    MinMax minMax;
    int numEntries = mr->map(1, readFile, &fileInput); // Get file content
    mr->collate(NULL); // KV -> KMV (value - 1)
    mr->reduce(sum, NULL); // KMV -> KV (value - # occurence of value)
    MPI_Barrier(MPI_COMM_WORLD);

    MapReduce *mr2 = new MapReduce(MPI_COMM_WORLD);
    mr2->map(mr, findMinMax, &minMax);
    delete mr2;

    int h = (minMax.max - minMax.min) / (int)(2 * (pow(numEntries, 0.33)));
    std::cout << "h = (max - min) / (2 * n ^ 0.33)" << std::endl;
    std::cout << h << " = (" << minMax.max << " - " << minMax.min << ") / ( 2 * " << numEntries << " ^ 0.33)" << std::endl;

    mr->map(mr, toInterval, &h); // KV (value - # occurence of value) -> KV (index of bin - # occurence of value)
    mr->collate(NULL); // KV -> KMV (value = index of bin - # occurence of value)
    mr->reduce(intervalSum, NULL);
    mr->print(-1, 1, 1, 1);

  delete mr;

  if (my_id == 0) {

  }

  MPI_Finalize();
}

/* ---------------------------------------------------------------------- */

std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while (std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}


std::vector<std::string> split(const std::string &s, char delim) {
    std::vector<std::string> elems;
    split(s, delim, elems);
    return elems;
}

void readFile(int itask, KeyValue *kv, void *ptr)
{
    FileInput *fileInput = (FileInput*) ptr;
    std::string line;
    std::ifstream file(fileInput->filename);

    while(std::getline(file, line)) {
        std::vector<std::string> splitLine = split(line, ',');
        int key;
        if(fileInput->flag == "d"){
            key = std::stoi(splitLine[4]) - std::stoi(splitLine[2]);
        } else if(fileInput->flag == "a") {
            key = std::stoi(splitLine[6]);
        } else {
            key = std::stoi(splitLine[5]);
        }

        kv->add((char*)&key, 8, NULL, 0);
    }
}

void sum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr)
{
    kv->add(key,keybytes,(char *) &nvalues,sizeof(int));
}

void findMinMax(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr)
{
    MinMax *minMax = (MinMax*) ptr;
    double k = *(double *) key;
    if(k < minMax->min)
        minMax->min = k;
    if(k > minMax->max)
        minMax->max = k;
}

void toInterval(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr)
{
    int h = *(int *) ptr;
    double k = *(double *) key;
    int v = *(int *) value;

    int intervalIndex = k / h;
    kv->add((char*)&intervalIndex,4,(char*)&v,4);
}

void intervalSum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr)
{
    int *vs = (int *) multivalue;
    int sum = 0;

    for(int i = 0; i < nvalues; ++i)
        sum += vs[i];

    kv->add(key,4,(char*)&sum,4);
}

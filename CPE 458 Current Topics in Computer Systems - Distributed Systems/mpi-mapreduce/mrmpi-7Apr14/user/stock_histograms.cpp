/* ----------------------------------------------------------------------
 MapReduce MPI Stock Analytics in C++
------------------------------------------------------------------------- */

#include <cmath>
#include <vector>
#include <cstdlib>
#include <sstream>
#include <float.h>
#include <fstream>
#include "mpi.h"
#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "sys/stat.h"
#include "mapreduce.h"
#include "keyvalue.h"

using namespace MAPREDUCE_NS;

/* Struct for min and max values of data */
struct MinMax
{
    double min = DBL_MAX;
    double max = -DBL_MAX;
    std::string flag;
};

/* Struct for input-filename and flag for selected column(s) */
struct FileInput
{
    std::string filename, flag;
};

/* Struct for handling output with flag for column(s) and bin width */
struct FlagAndH
{
    std::string flag;
    double h;
};

/* Helper function for readFile */
std::vector<std::string> split(const std::string &s, char delim);

/* Mapper: Read input file an add keys to KV-store */
/* Key is double or unsigned int, value is NULL */
void readFile(int itask, KeyValue *kv, void *ptr); // ptr to FileInput-struct

/* Reducer: Calculate sum of multiple values of each unique key */
/* Key stays the same, value is int */
void sum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr); // ptr is not used

/* Mapper: Map a key to it's interval with respect to 'h'. New key is interval-index */
/* Key is unsigned long if FlagAndH->flag == "v", otherwise key is double. Value is int */
/* Key type and value type stays the same */
void toInterval(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr); // ptr to FlagAndH-struct

/* Mapper: Find min and max values */
/* Key is unsigned long if minMax->flag == "v", otherwise key is double. Value does not care */
void findMinMax(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr); // ptr to MinMax-struct

/* Reducer: Calucalte sum of multiple values of each unique key */
/* Key is int, multivalue is int[] */
/* Key stays int, value becomes int */
void intervalSum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr); // ptr is not used

/* Mapper: Prints the data */
/* Key is unsigned long if FlagAndH->flag == "v", otherwise key is double. Value is int */
void printData(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr); // ptr to FlagAndH-struct

/* ---------------------------------------------------------------------- */

int main(int narg, char **args)
{
    MPI_Init(&narg,&args);

    int my_id, nprocs;
    MPI_Comm_rank(MPI_COMM_WORLD,&my_id);

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

    // Get file content
    int numEntries = mr->map(1, readFile, &fileInput);
    mr->collate(NULL);
    // Calculate sum for each unique key
    mr->reduce(sum, NULL);

    MapReduce *mr2 = new MapReduce(MPI_COMM_WORLD);
    // Find min and max values
    MinMax minMax;
    minMax.flag = flag;
    mr2->map(mr, findMinMax, &minMax);
    delete mr2;

    // Calculate bin-width
    double h = (minMax.max - minMax.min) / (int)(2 * (pow(numEntries, 0.33)));

    FlagAndH fnh;
    fnh.h = h;
    fnh.flag = flag;
    // Map each key with its value to a bin-index with respect to h
    mr->map(mr, toInterval, &fnh);
    mr->collate(NULL);
    // Calculate sum of values for each bin-index
    mr->reduce(intervalSum, NULL);
    mr->gather(1);
    // Sort bin-indicies (int) ascending
    mr->sort_keys(1);
    // Print data
    mr->map(mr, printData, &fnh);

    MPI_Barrier(MPI_COMM_WORLD);
    delete mr;

    MPI_Finalize();
}

/* ---------------------------------------------------------------------- */

std::vector<std::string> split(const std::string &s, char delim)
{
    std::vector<std::string> elems;
    std::stringstream ss(s);
    std::string item;
    while (std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}

void readFile(int itask, KeyValue *kv, void *ptr)
{
    FileInput *fileInput = (FileInput*) ptr;
    std::string line;
    std::ifstream file(fileInput->filename);
    std::getline(file, line); // Skip name row

    while(std::getline(file, line)) {
        std::vector<std::string> splitLine = split(line, ',');
        double key;
        if(fileInput->flag == "d"){ // Daily Differential
            key = std::stod(splitLine[4]) - std::stod(splitLine[2]);
        } else if(fileInput->flag == "a") { // Adjusted Closing Price
            key = std::stod(splitLine[6]);
        } else { // Volume Traded (default)
            key = std::stoul(splitLine[5]);
        }

        kv->add((char*)&key, sizeof(double), NULL, 0);
    }
}

void sum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr)
{
    kv->add(key,keybytes,(char *) &nvalues,sizeof(int));
}

void findMinMax(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr)
{
    MinMax *minMax = (MinMax*) ptr;
    if(minMax->flag == "v")
    {
        unsigned long k = *(unsigned long *) key;
        if(k < minMax->min)
            minMax->min = k;
        if(k > minMax->max)
            minMax->max = k;
    } else {
        double k = *(double *) key;
        if(k < minMax->min)
            minMax->min = k;
        if(k > minMax->max)
            minMax->max = k;
    }
}

void toInterval(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr)
{
    FlagAndH fnh = *(FlagAndH *) ptr;
    int v = *(int *) value;
    
    if(fnh.flag == "v"){
        unsigned long k = *(unsigned long *) key;
        int bin = k / fnh.h;
        kv->add((char*)&bin,sizeof(int),(char*)&v,sizeof(int));
    } else {
        double k = *(double *) key;
        int bin = k / fnh.h;
        kv->add((char*)&bin,sizeof(int),(char*)&v,sizeof(int));
    }
}

void intervalSum(char *key, int keybytes, char *multivalue, int nvalues, int *valuebytes, KeyValue *kv, void *ptr)
{
    int bin = *(int *)key;
    int *vs = (int *) multivalue;
    int sum = 0;
    for(int i = 0; i < nvalues; ++i)
        sum += vs[i];
    
    kv->add(key, keybytes, (char*) &sum, sizeof(int));
}

void printData(uint64_t itask, char *key, int keybytes, char *value, int valuebytes, KeyValue *kv, void *ptr)
{
    int bin = *(int *)key;
    int v = *(int *) value;
    FlagAndH fnh= *(FlagAndH*) ptr;
    
    if(fnh.flag == "v"){
        unsigned long binVal = bin * fnh.h;
        std::cout << binVal << "," << v << std::endl;
    } else {
        double binVal = bin * fnh.h;
        std::cout << binVal << "," << v << std::endl;
    }
}

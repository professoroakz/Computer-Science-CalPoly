/* ----------------------------------------------------------------------
// MapReduce MPI Stock Analytics in C++
------------------------------------------------------------------------- */

#include "mpi.h"
#include "math.h"
#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "mapreduce.h"
#include "keyvalue.h"

int main(int narg, char **args) {
    MPI_Init(&narg, &args);

    int myid, nprocs;
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    MPI_Comm_size(MPI_COMM_WORLD, &nprocs);

    /*  Number of bins: k,
     k = math.ceil ( max(x) - min(x) / h )
     Rice Rule: k = floor(2n^1/3)
                h = (max(x) - min(x)) / 2n^1/3
      */
}


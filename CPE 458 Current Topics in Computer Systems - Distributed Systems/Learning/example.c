#include 'mpi.h'
#include <stdio.h>

int main(int argc, char *argv[]) {
     MPI_Init(&argc, &argv);
          MPI_Comm_size(MPI_COMM_WORLD, &size);
          MPI_Comm_rank(MPI_COMM_WORLD, &rank);
          printf(“I am %d of $d\n”, rank, size);
     MPI_Finalize();
     return 0;
}
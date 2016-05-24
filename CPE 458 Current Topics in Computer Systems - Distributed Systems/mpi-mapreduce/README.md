# MapReduce MPI

## Summary
We will use the processing capabilities of the MPAC Lab to analyze real stock data to observe statistical trends for three values: Volume Traded, Adjusted Closing Price, and Daily Differential (Closing Price - Opening Price). For each dataset, you will compute a histogram for each of these three values.

## Build
go to ./mrmpi-7Apr14/user
make -f Makefile.mpicc

## Run
./stock_histrograms [file]

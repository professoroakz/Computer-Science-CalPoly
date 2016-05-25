# MapReduce MPI

## Summary
We will use the processing capabilities of the MPAC Lab to analyze real stock data to observe statistical trends for three values: Volume Traded, Adjusted Closing Price, and Daily Differential (Closing Price - Opening Price). For each dataset, you will compute a histogram for each of these three values.

## Build
1. go to ./mrmpi-7Apr14/user
2. make -f Makefile.mpicc

## Run
./stock_histrograms [file] [flag]
flags:	v = Volume Traded (default)
	a = Adjusted Closing Price
	d = Daily Differential (Closing Price - Opening Price)

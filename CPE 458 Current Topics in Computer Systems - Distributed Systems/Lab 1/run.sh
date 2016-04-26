ssh abahceci@unix11.csc.calpoly.edu
ssh 127x01

cd ~/Courses/distsys/NPB3.3.1/NPB3.3-MPI
make clean
make BT CLASS=B
mpirun -mca btl_tcp_if_include eth0 -np 2 --map-by ppr:1:node -host 127x02,127x03 ./bin/bt.B.2 > ~/Courses/distsys/NPB3.3.1/NPB3.3-MPI/output/BT_B_2.txt
sleep 60
# make clean
# make BT CLASS=B NPROCS=4
mpirun -mca btl_tcp_if_include eth0 -np 4 --map-by ppr:1:node -host 127x02,127x03,127x04,127x05 ./bin/bt.B.4 > ~/Courses/distsys/NPB3.3.1/NPB3.3-MPI/output/BT_B_4.txt
#sleep 120
# make clean
# make BT CLASS=B NPROCS=8

#mpirun -mca btl_tcp_if_include eth0 -np 8 --map-by ppr:1:node -host 127x02,127x03,127x04,127x05,127x06,127x07,127x08,127x09 ./bin/bt.B.8 > ~/Courses/distsys/NPB3.3.1/NPB3.3-MPI/output/BT_B_8.txt

# make clean
# make BT CLASS=B NPROCS=16
# mpirun -mca btl_tcp_if_include eth0 -np 16 --map-by ppr:1:node -host 127x02,127x03,127x04,127x05,127x06,127x07,127x08,127x09,127x10,127x11,127x12,127x13,127x14,127x15,127x16,127x17 ./bin/bt.B.16 > ~/Courses/distsys/NPB3.3.1/NPB3.3-MPI/output/BT_B_16.txt

echo "BT Benchmarks DONE."
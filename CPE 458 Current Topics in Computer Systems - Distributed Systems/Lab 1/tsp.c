#include <mpi.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>

struct city{
   int citinumber;
   double x;
   double y;
};
int n_cities = 0;
#define MAX_CITIES 2000
struct city *pArray_Cities[MAX_CITIES];
struct city *pTemp_Array_Cities[MAX_CITIES];
struct city *pLowestTotalArray[MAX_CITIES];

void newArray()
{
    int i1;
    int i2;
    int i3;
    int i = 0;
    int j = 0;

    for (;;)
    {
     for(;;)
     {
      i1 = rand() % n_cities;
      if ( i1 != 0 && i1 != n_cities -1 )
        break;
     }
     for(;;)
     {
      i2 = rand() % n_cities;
      if ( i2 != 0 && i2 != n_cities -1 )
        break;
     }
     if ( i1 != i2 )
      break;
    }

    if ( i1 > i2 )
    {
      i3 = i1;
      i1 = i2;
      i2 = i3;
    }

    for ( j=0,i = 0; i < i1; i++)
    {
      pTemp_Array_Cities[j++] = pArray_Cities[i];
    }
    pTemp_Array_Cities[j++] = pArray_Cities[i2];
    for ( i = i2-1; i >= i1; i--)
    {
      pTemp_Array_Cities[j++] = pArray_Cities[i];
    }
    for ( i = i2+1; i < n_cities; i++)
    {
      pTemp_Array_Cities[j++] = pArray_Cities[i];
    }
    for ( i = 0; i < n_cities; i++)
    {
      pArray_Cities[i] = pTemp_Array_Cities[i];
    }
}

double current_total = 0.0;
double lowest_total = 0.0;
double calculateTotal()
{
   double total = 0;
   int i = 0;
   struct city *pC1;
   struct city *pC2;

   for ( i = 0; i < n_cities-1; i++)
   {
      pC1 = pArray_Cities[i];
      pC2 = pArray_Cities[i+1];
      total += sqrt(pow(pC1->x - pC2->x,2) + pow(pC1->y - pC2->y,2));
      printf("%f Cost %d, %d\n", total, pC1->citinumber,
                                     pC2->citinumber);
   }
   pC1 = pArray_Cities[n_cities-1];
   pC2 = pArray_Cities[0];
   total += sqrt(pow(pC1->x - pC2->x,2) + pow(pC1->y - pC2->y,2));
   printf("Total Cost %f Cost %d, %d\n", total, pC1->citinumber,
                                     pC2->citinumber);

   return total;
}

double recalculate_total(struct city *pC1, struct city *pC2,
                            struct city *pC3, struct city *pC4)
{
   double d1;
   double d2;
   double d3;
   double d4;

   d1 = sqrt(pow(pC1->x - pC2->x,2) + pow(pC1->y - pC2->y,2));
   d2 = sqrt(pow(pC2->x - pC3->x,2) + pow(pC2->y - pC3->y,2));
   d3 = sqrt(pow(pC3->x - pC4->x,2) + pow(pC3->y - pC4->y,2));
   d4 = sqrt(pow(pC1->x - pC4->x,2) + pow(pC1->y - pC4->y,2));
   current_total += ( d1 + d2 ) - ( d3 + d4 );

   if ( current_total < lowest_total )
   {
       lowest_total = current_total;
       return(lowest_total);
   }
   else
   {
      return(-1);
   }
}
void print(struct city *p);
void printArray( struct city *pArray[])
{
   int i = 0;
   struct city *pCity;
   for ( i =0; i < n_cities; i++)
   {
      pCity = pArray[i];
      print(pCity);
   }
}
void print(struct city *p)
{
    printf("Citinumber = %d, X co-ordinate = %lf, Y Coordinate = %lf\n",
               p->citinumber, p->x, p->y);
}
int main(int argc, char** argv) {
    struct city *pCity;
    int index = 0;
    int i = 0;
    double x;
    double y;
    int number = 0;
    char s[80];
    FILE *fp = fopen(argv[1], "r");

    if ( ( fp != ( FILE * ) NULL ) )
    {
       while ( fgets(s,sizeof(s),fp) != ( char * ) NULL )
       {
           if ( strstr(s,"NODE") != ( char * ) NULL )
               break;
           printf("%s\n", s);
       }
       while ( ( i = fscanf(fp," %d %lf %lf", &number, &x, &y)) == 3 )
       {
          pCity = malloc(sizeof(struct city));
          pCity->x = x;
          pCity->y = y;
          pCity->citinumber = number;
          pArray_Cities[n_cities] = pCity;
          n_cities++;
       }
    }
    fclose(fp);

    srand(getpid());

    calculateTotal();
    for ( i = 0; i < 500; i++)
    {
      newArray();
      /*** Call recalculate ****/
    }

    // Initialize the MPI environment
    MPI_Init(NULL, NULL);

    // Get the number of processes
    int world_size;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Get the rank of the process
    int world_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    // Get the name of the processor
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    int name_len;
    MPI_Get_processor_name(processor_name, &name_len);

    // Print off a hello world message
    printf("Hello world from processor %s, rank %d"
           " out of %d processors\n",
           processor_name, world_rank, world_size);

    // Finalize the MPI environment.
    MPI_Finalize();

}


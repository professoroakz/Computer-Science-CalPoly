#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/stat.h>

int main(int argc, char *argv[])
{
   int rvalue = EXIT_FAILURE;
   if (argc == 2)
   {
      rvalue=chdir(argv[1]);
   }
   printf("errno: %d \n", errno);
   printf("argv 0 1: %s      .....  %s\n", argv[0], argv[1]);
   return rvalue;
}
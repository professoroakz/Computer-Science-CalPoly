#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>


char *cpyStr20(char *str)
{
   int i;
   char cpy[21];

   for (i = 0; str[i] != ‘\0’ && i < 20; i++)
   {
      cpy[i] = str[i];
   }
   cpy[i] = ‘\0’;
   return cpy;
}
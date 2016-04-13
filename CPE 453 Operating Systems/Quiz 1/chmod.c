#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/stat.h>

int main(int argc, char **argv)
{
    int i;
    i = strtol(argv[1], 0, 8);
    if (chmod (argv[2],i) < 0)
    {
        fprintf(stderr, "%s: error in chmod(%s, %s) - %d (%s)\n",
                argv[0], argv[1], argv[0], errno, strerror(errno));
        exit(1);
    }
    return(0);
}
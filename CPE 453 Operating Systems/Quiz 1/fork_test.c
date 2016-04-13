/* After a fork(), parent and child processes share the same global variables
 (i.e. if you change the variable in the child, it will change in the parent). */
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

int i = 0;

int main(int argc, char const *argv[])
{
    while (i < 3)
    {
        fork();
        printf("pid = %d, i = %d\n", getpid(), i);
        ++i;
    }

    char *str = (char *)(int *)malloc(10 * sizeof(int));
    printf("sizeof str: %lu\n", sizeof(str));
    printf("sizeof int: %lu\n", sizeof(int));
    int nums[5];
    nums[7] = 10;

    return 0;
}
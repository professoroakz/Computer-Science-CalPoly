#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>

int value = 5;
int main() {
    pid_t pid;
    pid = fork();
    if (pid == 0) { /* child process */
    printf("yas bitch my pid is %d", pid);
        value += 15;
        return 0;
    } else if (pid > 0) { /* parent process */
        printf("omg i hate to wait!");
        wait(NULL);
        printf("PARENT: value = %d",value); /* LINE A */
        return 0;
    }
}
#include "lwp.h"
#include <stdlib.h>
#include <stdio.h>

#define STACK_SIZE 4196
#define MAX_THREADS 10

/* crocess table */
lwp_context lwp_ptable[LWP_PROC_LIMIT];
/* current number of LWPs */
int lwp_procs = 0;
/* current number of running LWPs */
int lwp_running = 0;
/* initial stack pointer */
ptr_int_t init_sp;

/* currently running lwp */
lwp_context *current_lwp;

/* current scheduling func */
schedfun *sched_fun;



int new_lwp(lwpfun func, void *args, size_t stack_size) {
    return 0;
}

void lwp_exit() {

}

int lwp_getpid() {
    return 0;
}

void lwp_yield() {

}

void lwp_start() {

}

void lwp_stop() {

}

void lwp_set_scheduler( ) {

}
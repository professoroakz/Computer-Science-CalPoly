#include "lwp.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define STACK_SIZE 4196
#define MAX_THREADS 10


typedef struct node {
    int index;
    struct node *next;
} node;


int process_table[MAX_THREADS];
int current_process = 0;
uint32_t next_pid = 0;
struct node *root;

void * stack_ptr;


void queue_init() {
    root->index = 0;
    root->next = NULL;
}

int queue_insert() {
    return 0;
}

int queue_delete() {
    return 0;
}


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
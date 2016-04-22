#include "lwp.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define STACK_SIZE 4196
#define MAX_THREADS 10


typedef struct node {
    struct context_st *context;
    struct node *next;
} node;

struct node *root;

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



void queue_init() {
    /* context_st params */
    root->context->pid = 0;
    root->context->stack = malloc(sizeof(STACK_SIZE));
    root->context->stacksize = sizeof(root->context->stack);
    root->context->sp = NULL;

    /* Create initial next process */
    struct node *nxt = malloc(sizeof(struct node));
    root->next = nxt;
}

int queue_insert(lwpfun func, void *args, size_t stack_size) {
    /* return 0 if maximum LWPs active */
    if(sizeof(lwp_ptable) == LWP_PROC_LIMIT)
        return 0;

    if(root->next == NULL) {
        queue_init();
    } else {
        struct node *current = root;
        while(current->next != NULL) {
            struct node *new = malloc(sizeof(node));
            new->context->stack = malloc(sizeof(STACK_SIZE));
            new->context->stacksize = sizeof(new->context->stack);
            new->context->pid = current->context->pid + 1;
            current->next = new;
        }
        return 1;
    }
    return -1; // this will never happen
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
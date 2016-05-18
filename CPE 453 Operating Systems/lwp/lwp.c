/*
* CPE 453 OS - Lab 1 Threads
* Implementation of a simplistic threading system.
*
* @author Oktay Bahceci
*/
#include "lwp.h"
#include <stdlib.h>
#include <stdio.h>

#define STACK_SIZE 4196

/* crocess table */
lwp_context lwp_ptable[LWP_PROC_LIMIT];
/* current number of LWPs */
int lwp_procs = 0;
/* currently running lwp */
int lwp_running = 0;
/* next pid */
int next_pid = 0;
/* initial stack pointer */
ptr_int_t init_sp;
/* currently running lwp */
lwp_context *current_lwp;
/* current scheduling func */
schedfun sched_fun = NULL;

/*  Creates a new lightweight process.
    As specified,
    * allocate memory for the new process in stacksize words
    * push one argument to the stack
    * push the ret adress to the stack
    * push bogus base pointer
    * push adress of bp as new bp
    * create lwp_context struct
    * insert to the lwp_ptable

    @params
    lwpfun func         the function to be called by the process
    void *args          the arguments for the function
    size_t stack_size   the stack size for the prcoess

    @return
    upon success, pid of the lwp created
    upon failure, -1
*/
int new_lwp(lwpfun func, void *args, size_t stack_size) {
    /* exit instantly if limit is reached */
    if(lwp_procs == LWP_PROC_LIMIT) return -1;

    ptr_int_t *stack_ptr, *base_ptr, *stack = malloc(sizeof(ptr_int_t) * stack_size);
    lwp_context new_lwp;

    /* point to current lwp stack */
    stack_ptr = stack + stack_size;
    /* push args */
    *stack_ptr = (ptr_int_t) args;
    /* adjust sp for lwp_exit */
    stack_ptr--;
    /* push exit func */
    *stack_ptr = (ptr_int_t) lwp_exit;
    /* adjust sp for args */
    stack_ptr--;
    /* push ret adress */
    *stack_ptr = (ptr_int_t) func;
    /* adjust after pushing ret adress */
    stack_ptr--;
    /* bogus base_ptr */
    *stack_ptr = 7;
    /* let base_ptr have this value */
    base_ptr = stack_ptr;
    /* adjust stack_ptr down */
    stack_ptr--;

    /* push bogus registers */
    int i;
    for(i = 1; i < 7; i++) {
        *stack_ptr = i;

        stack_ptr--;
    }
    /* push base ptr */
    *stack_ptr = (ptr_int_t) base_ptr;

    /* update lwp_running */
    lwp_running = lwp_procs;

    /* assign entry values for new process */
    new_lwp.pid         = ++lwp_procs;
    new_lwp.stack       = stack;
    new_lwp.stacksize   = stack_size;
    new_lwp.sp          = stack_ptr;

    lwp_ptable[new_lwp.pid - 1] = new_lwp;

    return new_lwp.pid;
}

/**
 * Terminates the current LWP, frees its resources, and moves all the others up
 * in the process table.  If there are no other threads, calls `lwp_stop()`.
 */
void lwp_exit() {
    free(lwp_ptable[lwp_running].stack);

    int i = lwp_running + 1;
    /* shift ptable by 1 */
    for(; i < lwp_procs; i++) {
        lwp_ptable[i - 1] = lwp_ptable[i];
    }

    lwp_procs--;

    /* if no more threads, exit */
    if(lwp_procs == 0) {
        lwp_stop();
    /* scheduling - round robin */
    } else {
        if(sched_fun == NULL) {
            lwp_running = 0;
        } else {
            lwp_running = sched_fun();
        }
        SetSP(lwp_ptable[lwp_running].sp);

        RESTORE_STATE();
    }
}

/**
 * returns the pid of the calling LWP. Undefined if not called by a LWP.
 */
int lwp_getpid() {
    if(lwp_running > -1) {
        return lwp_ptable[lwp_running].pid;
    } else {
        return (int)NULL;
    }
}

/**
 * Yields control to another LWP.  Which one depends on the scheduler.  Saves
 * the current LWP's context, picks the next one, restores that thread's
 * context, and returns.
 */
void lwp_yield() {
    /* save current state */
    SAVE_STATE();

    /* save sp */
    GetSP(current_lwp->sp);

    /* round-robin scheduling */
    if(sched_fun == NULL) {
        lwp_running++;
        if(lwp_running == lwp_procs) {
            lwp_running = 0;
        } else {
            lwp_running = sched_fun();
        }
        SetSP(lwp_ptable[lwp_running].sp);
        RESTORE_STATE();
    }

    SetSP(current_lwp->sp);

    RESTORE_STATE();
}

/**
 * Starts the LWP system.  Saves the original context and stack pointer (for
 * `lwp_stop()` to use later), picks a LWP and start it running.  If there are
 * no LWPs, returns immediately.
 */
void lwp_start() {
    /* if lwp_procs == 0 */
    if(!lwp_procs)
        return;

    /* save current state */
    SAVE_STATE();

    /* save sp for later */
    GetSP(init_sp);

    /* round-robin scheduling */
    if(sched_fun == NULL) {
        lwp_running = 0;
    } else {
        lwp_running = sched_fun();
    }

    /* save the sp */
    SetSP(lwp_ptable[lwp_running].sp);

    /* continue */
    RESTORE_STATE();
}

/**
 * Stops the LWP system, restores the original stack pointer and returns to
 * that context (wherever `lwp_start()` was called from).  Does not destory any
 * existing contexts, and thread processing will be restarted by a call to
 * `lwp_start()`.
 */
void lwp_stop() {
    /* save current state */
    SAVE_STATE();

    /* reset sp to init_sp */
    SetSP(init_sp);

    /* continue */
    RESTORE_STATE();
}

/**
 * Causes the LWP package to use the function `scheduler` to choose the next
 * process to run.  `(*scheduler)()` must return an integer in the range
 * 0...lwp_procs - 1, representing an index into `lwp_ptable`, or -1 if there
 * is no thread to schedule.
 * If `scheduler` is null or has never been set, the scheduler should do
 * round-robin scheduling.
 */
void lwp_set_scheduler(schedfun sched) {
    if(!sched_fun)
        sched_fun = malloc(sizeof(schedfun));
    sched_fun = sched;
}
/*
 * snake:  This is a demonstration program to investigate the viability
 *         of a curses-based assignment.
 *
 * Author: Dr. Phillip Nico
 *         Department of Computer Science
 *         California Polytechnic State University
 *         One Grand Avenue.
 *         San Luis Obispo, CA  93407  USA
 *
 * Email:  pnico@csc.calpoly.edu
 *
 * Revision History:
 *         $Log: main.c,v $
 *         Revision 1.2  2004-04-13 12:31:50-07  pnico
 *         checkpointing with listener
 *
 *         Revision 1.1  2004-04-13 09:53:55-07  pnico
 *         Initial revision
 *
 *         Revision 1.1  2004-04-13 09:52:46-07  pnico
 *         Initial revision
 *
 */

#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <ncurses.h>
#include <signal.h>
#include <sys/time.h>
#include "snakes.h"
#include "lwp.h"

#define MAXSNAKES  100
#define INITIALSTACK 2048

typedef void (*sigfun)(int signum);

static void install_handler(int sig, sigfun fun);
static int AlwaysZero();

int main(int argc, char *argv[]){
  int i,cnt,err;
  snake s[MAXSNAKES];

  err = 0;
  for (i=1;i<argc;i++) {                /* check options */
    if ( !strcmp(argv[i],"-z") ){ /* -z = schedule element 0 */
      lwp_set_scheduler(AlwaysZero);
    } else {
      fprintf(stderr,"%s: unknown option\n",argv[i]);
      err++;
    }
  }
  if ( err ) {
    fprintf(stderr,"usage: %s [-z]\n",argv[0]);
    exit(err);
  }

  install_handler(SIGINT, kill_snake);   /* SIGINT will kill a snake */
  install_handler(SIGQUIT, lwp_stop);    /* SIGQUIT will stop the system */

  start_windowing();            /* start up curses windowing */

  /* Initialize Snakes */
  cnt = 0;
  /* snake new_snake(int y, int x, int len, int dir, int color) ;*/

  s[cnt++] = new_snake( 8,30,10, E,1);/* each starts a different color */
  s[cnt++] = new_snake(10,30,10, E,2);
  s[cnt++] = new_snake(12,30,10, E,3);
  s[cnt++] = new_snake( 8,50,10, W,4);
  s[cnt++] = new_snake(10,50,10, W,5);
  s[cnt++] = new_snake(12,50,10, W,6);
  s[cnt++] = new_snake( 4,40,10, S,7);

  /* Draw each snake */
  draw_all_snakes();

  /* turn each snake loose as an individual LWP */
  for(i=0;i<cnt;i++) {
    s[i]->lw_pid = new_lwp((lwpfun)run_snake,(void*)(s+i),INITIALSTACK);
  }

  lwp_start();                     /* returns when the last lwp exits */

  end_windowing();              /* close down curses windowing */

  printf("Goodbye.\n");         /* Say goodbye, Gracie */
  return err;
}

int AlwaysZero() {
  /* A scheduler that always run the first one */
  return 0;
}

void install_handler(int sig, sigfun fun){
  /* use sigaction to install a signal handler */
  struct sigaction sa;

  sa.sa_handler = fun;
  sigemptyset(&sa.sa_mask);
  sa.sa_flags = 0;


  if ( sigaction(sig,&sa,NULL) < 0 ) {
    perror("sigaction");
    exit(-1);
  }
}


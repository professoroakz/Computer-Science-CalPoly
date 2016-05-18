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

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "lwp.h"

#define MAXSNAKES  100
#define INITIALSTACK 2048

typedef void (*sigfun)(int signum);
static void indentnum(void *num);

int AlwaysZero() {
  /* A scheduler that always run the first one */
  return 0;
}

int main(int argc, char *argv[]){
  ptr_int_t i;

  for (i=1;i<argc;i++) {                /* check options */
    if ( !strcmp(argv[i],"-z") ){ /* -z = schedule element 0 */
      lwp_set_scheduler(AlwaysZero);
    } else {
      fprintf(stderr,"%s: unknown option\n",argv[i]);
      exit(-1);
    }
  }

  printf("Launching LWPS\n");

  /* spawn a number of individual LWPs */
  for(i=1;i<=5;i++) {
    new_lwp(indentnum,(void*)i,INITIALSTACK);
  }

  lwp_start();                     /* returns when the last lwp exits */

  printf("Back from LWPS.\n");
  return 0;
}

static void indentnum(void *num) {
  /* print the number num num times, indented by 5*num spaces
   * Not terribly interesting, but it is instructive.
   */
  ptr_int_t howfar,i;

  howfar=(ptr_int_t)num;              /* interpret num as an integer */
  for(i=0;i<howfar;i++){
    printf("%*"PTR_INT_T_FMT"\n",(int)howfar*5,howfar);
    lwp_yield();                /* let another have a turn */
  }
  lwp_exit();                   /* bail when done.  This should
                                 * be unnecessary if the stack has
                                 * been properly prepared
                                 */
}


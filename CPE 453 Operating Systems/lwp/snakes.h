#ifndef SNAKEH
#define SNAKEH

typedef enum {NW,N,NE,W,E,SW,S,SE} direction;
#define NUMDIRS 8

typedef struct point_t {
  int x;
  int y;
} sn_point;

typedef struct snake_st {
  direction       dir;
  int             len;
  int             color;        /* color pair to use if colored snakes */
  sn_point        *body;
  int             lw_pid;       /* useful for playing with scheduling */
  struct snake_st *others;      /* a utility link to find all snakes again */
} *snake;

/* Colors range from 1 (blue on black) to 8 ( black on black).
 */
#define MAX_VISIBLE_SNAKE 7

extern int          start_windowing();
extern void         end_windowing();
extern snake        new_snake(int y, int x, int len, int dir, int color) ;
extern void         free_snake(snake s);
extern void         draw_all_snakes();
extern void         run_snake(snake *s);
extern void         run_hungry_snake(snake *s);
extern void         kill_snake();
extern unsigned int get_snake_delay();
extern void         set_snake_delay(unsigned int msec);
extern snake        snakeFromLWpid(int lw_pid);

#endif

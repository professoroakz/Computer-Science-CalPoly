PLAT=$(shell uname -i)
LWP-EX=liblwp-$(PLAT).a
SN-EX=libsnakes-$(PLAT).a
LWP=liblwp.a

all: $(LWP)
examples: $(LWP-EX) lwp.h numbersmain-ex snakemain-ex hungrymain-ex numbersmain snakemain hungrymain

snakemain: snakemain.c snakes.h $(LWP) $(SN)
	cc -Wall -Werror -g -o $@ $< $(LWP) $(SN-EX) -lncurses

snakemain-ex: snakemain.c snakes.h $(LWP-EX) $(SN-EX)
	cc -Wall -Werror -g -o $@ $< $(LWP-EX) $(SN-EX) -lncurses

hungrymain: hungrymain.c snakes.h $(LWP) $(SN-EX)
	cc -Wall -Werror -g -o $@ $< $(LWP) $(SN-EX) -lncurses

hungrymain-ex: hungrymain.c snakes.h $(LWP-EX) $(SN-EX)
	cc -Wall -Werror -g -o $@ $< $(LWP-EX) $(SN-EX) -lncurses

numbersmain: numbersmain.c lwp.h $(LWP)
	cc -Wall -Werror -g -o $@ $< $(LWP)

numbersmain-ex: numbersmain.c lwp.h $(LWP-EX)
	cc -Wall -Werror -g -o $@ $< $(LWP-EX)

$(LWP): lwp.o smartalloc.o
	ar r $@ lwp.o
	ranlib $@

lwp.o: lwp.c lwp.h
	cc -Wall -Werror -g -c -o $@ $<
	cc -S $<

smartalloc.o: smartalloc.c
	cc -Wall -Werror -g -c -o $@ $<

clean:
	-rm -f numbersmain snakemain hungrymain $(LWP) lwp.o liblwp.o numbersmain-ex snakemain-ex hungrymain-ex
	-rm -f lwp.s smartalloc.o


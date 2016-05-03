import argparse
import sys
# init method

jobs = {}
count = 0
sched_alg = ""
quantum_rr = -1

# read input

# fix constructs
# do rr, srjn, fifo etc algorithms

def fifo():
    process_queue = []
    n = len(jobs)
    w_time = 0
    for i in xrange(n):
        process_queue.append([])
        process_queue[i].append(jobs[n][1]) # arrival

        w_time += process_queue[i][1]

        process_queue[i].append(jobs[n][0]) # burst

def srjn():
    pass

def rr():
    pass


def print_current_job(job_num, response_time, turnaround_time, wait_time):
    print('Job:' + '{:3d}'.format(34) + ' -- '
        + 'Response: ' + '{:3.2f}'.format(3.224)
        + ' Turnaround: ' + '{:3.2f}'.format(3.224)
        + ' Wait: ' + '{:3.2f}'.format(3.423))


def print_all_jobs(response_time, turnaround_time, wait_time):
    print('Average:' + ' -- '
        + 'Response: ' + '{:3.2f}'.format(response_time)
        + ' Turnaround: ' + '{:3.2f}'.format(turnaround_time)
        + ' Wait: ' + '{:3.2f}'.format(wait_time))

def sched_alg():
    if alg is "SRJN":
        srjn()
    elif alg is "RR":
        rr()
    else:
        fifo()

def check_args(algorithm, quantum):
    alg = ""
    quantum_rr = 0

    if algorithm is None:
        alg = "RR"
    else:
        alg = algorithm

    if quantum_rr is None:
        quantum_rr = 1
    else:
        quantum_rr = quantum

    return (alg, quantum_rr)


def read_input(args, jobs, count):
    try:
        with open(args.inputfile) as f:
            jobs = [map(int, line.split()) for line in f]
            return jobs
    except IOError as e:
        print "I/O error({0}): {1}".format(e.errno, e.strerror)
        sys.exit(0)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('inputfile', help='inputfile')
    parser.add_argument('-p', nargs=1)
    parser.add_argument('-q', nargs=1)

    args = parser.parse_args()

    count = 0

    jobs = read_input(args, jobs, count)

    # print("~ Filename: {}".format(args.inputfile))
    # print("~ -p: {}".format(args.p))
    # print("~ -q: {}".format(args.q))

    sched_alg, quantum_rr = check_args(args.p, args.q)

    print(sched_alg, quantum_rr)

    for (key, val) in jobs:
        print(key, val)

    jobs = sorted(jobs, key=lambda tup: tup[1])


if __name__ == '__main__':
    main()
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

# jobs(job_run_time, arrival_time)

def fifo():
    global jobs
    joblist = sorted(jobs, key=lambda tup: tup[1])

    wait_times = []
    wait_times.append(0) #wt first process is 0

    response_times = []
    response_times.append(1)

    t_times = [] #turnaround

    total_response_time = 0
    total_turnaround_time = 0
    total_wait_time = 0

    for idx, job in enumerate(joblist, start=1):
        wait_times.append(0)
        response_times.append(1)

        for idx2, job2 in enumerate(joblist, start=0):
            if idx == idx2:
                break
            wait_times[idx] += job2[0]
            response_times[idx] = wait_times[idx]

    for idx, job in enumerate(joblist):
        t_times.append(job[0] + wait_times[idx])
        print job[0], wait_times[idx]
        total_wait_time += wait_times[idx]
        total_turnaround_time += t_times[idx]

    for x in range(0, len(jobs)):
        print_current_job(jobs[x][1], response_times[x], t_times[x], wait_times[x])

    print_all_jobs(sum(response_times)/len(joblist), total_turnaround_time/len(joblist), total_wait_time/len(joblist))

def srjn():
    pass

def rr():
    pass


def print_current_job(job_num, response_time, turnaround_time, wait_time):
    print('Job:' + '{:3d}'.format(job_num) + ' -- '
        + 'Response: ' + '{:3.2f}'.format(response_time)
        + ' Turnaround: ' + '{:3.2f}'.format(turnaround_time)
        + ' Wait: ' + '{:3.2f}'.format(wait_time))


def print_all_jobs(response_time, turnaround_time, wait_time):
    print('Average:' + ' -- '
        + 'Response: ' + '{:3.2f}'.format(response_time)
        + ' Turnaround: ' + '{:3.2f}'.format(turnaround_time)
        + ' Wait: ' + '{:3.2f}'.format(wait_time))

def sched_algorithm(alg):
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


def read_input(args):
    global jobs
    try:
        with open(args.inputfile) as f:
            jobs = [map(int, line.split()) for line in f]
    except IOError as e:
        print "I/O error({0}): {1}".format(e.errno, e.strerror)
        sys.exit(0)


def main():
    global jobs

    parser = argparse.ArgumentParser()
    parser.add_argument('inputfile', help='inputfile')
    parser.add_argument('-p', nargs=1)
    parser.add_argument('-q', nargs=1)

    args = parser.parse_args()

    count = 0

    read_input(args)

    # print("~ Filename: {}".format(args.inputfile))
    # print("~ -p: {}".format(args.p))
    # print("~ -q: {}".format(args.q))

    sched_alg, quantum_rr = check_args(args.p, args.q)

    print(sched_alg, quantum_rr)

    for (key, val) in jobs:
        print(key, val)

    jobs = sorted(jobs, key=lambda tup: tup[1])
    sched_algorithm(sched_alg)


if __name__ == '__main__':
    main()
from __future__ import division
import argparse
import sys
# init method

jobs = {}
processes = []
count = 0
sched_alg = ""
quantum_rr = -1
num_processes = 0

class Process:
    arrival_time = 0
    burst_time = 0
    number = 0

    wait_time = 0
    response_time = 0
    turnaround_time = 0
    checked = 0

    remaining = 0

    def __init__(self, number, burst_time, arrival_time):
        self.burst_time = burst_time
        self.arrival_time = arrival_time
        self.number = number
        self.remaining = self.burst_time


def fifo():
    global processes

    jobs = sorted(processes, key = lambda x: x.number)

    time = 0

    for j in jobs:
        if time == 0:
            j.wait_time = 0
            j.response_time = j.wait_time + 1
            time += j.burst_time + j.response_time
            j.turnaround_time = time - j.response_time
        else:
            j.wait_time = time
            j.response_time = j.wait_time
            time += j.burst_time
            j.turnaround_time = time - j.response_time

    for x in jobs:
        print_current_job(x.number, x.response_time, x.turnaround_time, x.wait_time)

    avg_response_time = sum([x.response_time for x in jobs]) / len(jobs)
    avg_turnaround_time = sum([x.turnaround_time for x in jobs]) / len(jobs)
    avg_wait_time = sum([x.wait_time for x in jobs]) / len(jobs)

    print_all_jobs(avg_response_time, avg_turnaround_time, avg_wait_time)


def srjn():
    jobs = processes
    time = 0
    counter = 0
    flag = False
    done = False
    left = len(jobs)
    done_jobs = []

    for j in jobs:
        if time == 0:
            time += 1
            j.remaining -= 1

        if j in done_jobs:
            continue

        for j2 in jobs:
            if j2 in done_jobs:
                continue

            if j.burst_time < j2.burst_time:
                current = j
            else:
                current = j2

            time += current.remaining
            current.remaining = 0
            flag = True

            if current.remaining == 0 and flag == True:
                current.wait_time = time - current.burst_time - current.arrival_time
                current.response_time = current.wait_time + 1
                current.turnaround_time = time - current.arrival_time
                print_current_job(current.number, current.response_time, current.turnaround_time, current.wait_time)
                done_jobs.append(current)

            if len(done_jobs) == len(jobs):
                done = True
            else:
                break

    if done == True:
        avg_response_time = sum([x.response_time for x in jobs]) / len(jobs)
        avg_turnaround_time = sum([x.turnaround_time for x in jobs]) / len(jobs)
        avg_wait_time = sum([x.wait_time for x in jobs]) / len(jobs)
        print_all_jobs(avg_response_time, avg_turnaround_time, avg_wait_time)

def rr():
    global quantum_rr, jobs

    jobs = sorted(processes, key = lambda x: x.number)

    time = 0
    counter = 0
    flag = False
    left = len(jobs)

    while left != 0:
        for j in jobs:
            if j.remaining <= quantum_rr and j.remaining > 0:
                time += j.remaining
                j.remaining = 0
                flag = True
            elif j.remaining > 0:
                j.remaining -= quantum_rr
                time += quantum_rr
            if j.remaining == 0 and flag == True:
                left -= 1
                j.wait_time = time - j.burst_time - j.arrival_time
                j.response_time = j.wait_time + 1
                j.turnaround_time = time - j.arrival_time
                print_current_job(j.number, j.response_time, j.turnaround_time, j.wait_time)

    avg_response_time = sum([x.response_time for x in jobs]) / len(jobs)
    avg_turnaround_time = sum([x.turnaround_time for x in jobs]) / len(jobs)
    avg_wait_time = sum([x.wait_time for x in jobs]) / len(jobs)
    print_all_jobs(avg_response_time, avg_turnaround_time, avg_wait_time)


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
    alg_str = "".join(alg)
    if alg_str == "SRJN":
        srjn()
    elif alg_str == "RR":
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
    global jobs, quantum_rr, processes, num_processes

    parser = argparse.ArgumentParser()
    parser.add_argument('inputfile', help='inputfile')
    parser.add_argument('-p', nargs=1)
    parser.add_argument('-q', nargs=1)

    args = parser.parse_args()

    read_input(args)

    sched_alg, quantum_rr = check_args(args.p, args.q)

    count = 0
    for job in jobs:
        p = Process(count, job[0], job[1])
        processes.append(p)
        count += 1

    sched_algorithm(sched_alg)


if __name__ == '__main__':
    main()
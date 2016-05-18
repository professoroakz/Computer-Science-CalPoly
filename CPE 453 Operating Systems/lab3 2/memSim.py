from __future__ import division
import argparse
import sys
import struct
import collections
import UserDict
import os.path

# The process has multiple frames in a page
# process > page > frame

backing_store_bin = "BACKING_STORE.bin"

class FifoCache(object, UserDict.DictMixin):
    def __init__(self, num_entries, dct=( )):
        self.num_entries = num_entries
        self.dct = dict(dct)
        self.lst = []

    def get(self, key):
        try:
            return self.dct[key]
        except KeyError:
            return None

    def set(self, key, value):
        dct = self.dct
        lst = self.lst
        if key in dct:
            lst.remove(key)
        dct[key] = value
        lst.append(key)
        if len(lst) > self.num_entries:
            del dct[lst.pop(0)]

    def pop(self, key):
        self.dct.pop(key)
        self.lst.remove(key)

# element in the head of the list is the least-used-item
class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = collections.OrderedDict()

    def get(self, key):
        try:
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        except KeyError:
            return None

    def set(self, key, value):
        try:
            self.cache.pop(key)
        except KeyError:
            if len(self.cache) >= self.capacity:
                self.cache.popitem(last=False)
        self.cache[key] = value

class OPTCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = collections.OrderedDict()
        self.priority = {}
        self.victims = []

    def get(self, key):
        try:
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        except KeyError:
            return None

    def set(self, key, value):
        try:
            self.cache.pop(key)
        except KeyError:
            if len(self.cache) >= self.capacity:
                self.pop_opt()
        self.cache[key] = value

    def pop_opt(self):
        for v in victims:
            c = self.cache.get(v[0])
            if c is None:
                continue
            else:
                self.cache.pop(v[0])

    def prepare_opt(self, input_adresses):
        service_number = 0
        for a in input_adresses:
            page_number, offset = mask_logical_adress(a)
            try:
                ip = self.priority[page_number]
                self.priority[page_number].num_requests += 1
            except KeyError:
                ip = InsignificantPage(page_number, 1, service_number)
                self.priority[page_number] = ip
            service_number += 1

    def calc_opt(self):
        for (i, v) in self.priority.iteritems():
            self.victims.append((v.page_number, v.num_requests, v.service_number))
        self.victims = sorted(self.victims, key=lambda x: (x[1], -x[2]))
        # sample output:
        # page_num, num_requests, service_number
        # 204 1 29
        # 48 1 27
        # 227 1 25
        # 188 1 24
        # 133 1 23
        # 82 1 22
        # 210 1 20
        # 141 1 19
        # 50 1 10
        # 71 1 9
        # 117 1 2
        # 244 1 1
        # 66 1 0
        # 136 2 17
        # 253 2 8
        # 189 2 7
        # 95 2 6
        # 112 2 5
        # 156 2 4
        # 209 2 3
        # 91 4 18

class VirtualMemory:
    # return (page_number,frame) with the help of page
    def tlb_get(self, page):
        self.tlb_requests += 1
        if self.tlb.get(page) is None:
            self.tlb_miss += 1
            return None
        else:
            return self.tlb.get(page) # Page(index, frame)

    def page_table_get(self, page_num):
        self.page_requests += 1
        tlb_miss = 0

        page = self.tlb_get(page_num)
        if page is None:
            tlb_miss = 1
        else:
            return page

        page = self.pages.get(page_num)
        if page is None and tlb_miss == 1:
            self.page_number += 1
            self.page_misses += 1

            frame = read_bin_frame(page_num)
            p = Page(self.page_number, frame)
            self.pages.set(page_num, p)
            self.tlb.set(page_num, p)
            return p
        else:
            return page

    def handle_opt(self, adresses):
        self.pages.prepare_opt(adresses)
        self.pages.calc_opt()

    def handle_request(self, logical_adress):
        self.num_requests += 1
        page_number, offset = mask_logical_adress(logical_adress)
        page = self.page_table_get(page_number)
        byte_referenced = get_byte_value(page.frame, offset)
        self.print_adress(logical_adress, byte_referenced, page.page_number, page.frame)

    def print_adress(self, adress, value, frame_number, entire_frame):
        print("{0}, {1}, {2}, {3}").format(adress, value, frame_number, entire_frame.encode("hex").upper())

    def print_stats(self):
        print("Number of Translated Adresses = {0}").format(self.num_requests)
        print("Page Faults = {0}").format(self.page_misses)
        print("Page Fault Rate = {:3.3f}").format(self.page_misses / (self.page_misses + self.page_requests))
        print("TLB Hits = {0}").format(self.tlb_requests - self.tlb_miss)
        print("TLB Misses = {0}").format(self.tlb_miss)
        print("TLB Hit Rate = {:3.3f}").format((self.tlb_requests - self.tlb_miss) / (self.tlb_requests + self.tlb_miss))

     # fuck, page is not phys frame number, add a frame to the tlb, frame number is index of the entry tlb
    def __init__(self, alg, frames):
        self.algs = {"FIFO": 1, "LRU": 2, "OPT": 3}
        self.repl_alg = alg

        self.num_frames = frames
        self.num_requests = 0

        self.page_number = -1
        self.page_requests = 0
        self.page_misses = 0

        if self.algs[alg] == 1:
            self.pages = FifoCache(frames, {})
        elif self.algs[alg] == 2:
            self.pages = LRUCache(frames)
        elif self.algs[alg] == 3:
            self.pages = OPTCache(frames)

        self.tlb = FifoCache(16, {})
        self.tlb_requests = 0
        self.tlb_miss = 0

class InsignificantPage:
    def __init__(self, page_number, num_requests, service_number):
        self.page_number = page_number
        self.num_requests = num_requests
        self.service_number = service_number

class Page:
    def __init__(self, page_number, frame):
        self.page_number = page_number
        self.frame = frame

class TLBEntry:
    def __init__(self, page, frame):
        self.page_number = page
        self.frame_number = frame


def get_byte_value(frame, offset):
    return int(frame[offset].encode('hex'), 16)

# @param adress: 32-bit integer representing a logical adress
# @return page, offset
def mask_logical_adress(adress):
    return (adress >> 8), (adress & 0xFF)

# Binary file reading
def read_bin_frame(frame):
    file = open(backing_store_bin, 'rb')
    record_size = 256
    file.seek(record_size * frame) # number indexed by 0
    return file.read(record_size) # returns in hex

def create_vm(infile, frames, alg):
    return VirtualMemory(alg, frames)

def parse_args(args):
    return [int(line) for line in args["rsf"]], args["frames"], args["pra"]

# for a single adress, int from file
# mask_logical_adress(adress)


def main():
    global backing_store_bin
    repl_algs = ["FIFO", "OPT", "LRU"]

    # BACKING_STORE.bin
    if not os.path.isfile(backing_store_bin) == True:
        print("BACKING_STORE.bin cannot be found, terminating.")
        sys.exit(0)

    # Args for infile, framesize, page-replacement algorithm
    parser = argparse.ArgumentParser(description="Virtual Memory Simulator")
    parser.add_argument("rsf", metavar="RSF", type=argparse.FileType(), help="Reference sequence file")
    parser.add_argument("--frames", metavar="F", type=int, default=256, help="Number of frames in main memory")
    parser.add_argument("--pra", metavar="PRA", type=str, default="FIFO", help="Page replacement algorithm")

    args = vars(parser.parse_args())
    input_adresses, num_frames, replacement_algorithm = parse_args(args)

    if num_frames < 0 or num_frames > 256:
        num_frames = 256

    if replacement_algorithm not in repl_algs:
        replacement_algorithm = "FIFO"

    print replacement_algorithm

    vm = VirtualMemory(replacement_algorithm, num_frames)

    for adress in input_adresses:
        vm.handle_request(adress)

    vm.print_stats()


if __name__ == '__main__':
    main()
from textwrap import wrap
from sys import argv

script, filename = argv
dict = {}
txt = open(filename)


#list = ['A','C','G','T']
#print list[::-1] # reverse
#print list

def init():
    codons = open("codons.txt")
    for pair in codons:
        split = pair.split(": ")
        dict[split[0]] = split[1].strip()
    #print dict
    return


def complement(str):
    str = str.replace('A', 'Q').replace('C', 'X').replace('G', 'Y').replace('T', 'Z')
    str = str.replace('Q', 'T').replace('X', 'G').replace('Y', 'C').replace('Z', 'A')
    return str[::-1]


def merge(str1, str2):
    max = 0
    for i in range(4, min(len(str1), len(str2)) + 1):
        if str1[-i:] == str2[:i]:
            max = i
            print max
    if not max:
        print "Cannot merge"
        return ""
    return str1[:-max] + str2

def translate(str, frame):
    result = ""
    input = wrap(str[frame:], 3)
    if len(input[-1]) < 3:
        input = input[:-1]
    for codon in input:
        result += dict[codon]
    return result

init()
#for sequence in txt:
#    print sequence
#    print "Complement: " + complement(sequence) + '\n'
#    print "Translate: " + translate(sequence, 1) + '\n'

print merge("OMGROFL", "ROFLOMG")
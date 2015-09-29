from textwrap import wrap
from sys import argv
from itertools import permutations

script, filename = argv
dict = {}
sequences = []
possibleWords = []
correctWords = []
txt = open(filename)
with open("/usr/share/dict/words") as englishDict:
    englishWords = englishDict.readlines()


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
           # print max
    if not max:
        #print "Cannot merge"
        return None
    return str1[:-max] + str2

def translate(str, frame):
    result = ""
    input = wrap(str[frame - 1:], 3)
    if len(input[-1]) < 3:
        input = input[:-1]
    for codon in input:
        if dict[codon] == "-":
            break
        result += dict[codon]
    return result

# Code from http://www.peterbe.com/plog/uniqifiers-benchmark
def unique(seq):
   seen = {}
   result = []
   for item in seq:
       if item in seen: continue
       seen[item] = 1
       result.append(item)
   return result

init()

for sequence in txt:
    if not sequence.isspace():
        sequences += sequence.strip(),
        sequences += complement(sequence.strip()),
permutatedSequences = list(permutations(sequences, 2))
#print permutatedSequences
for pair in permutatedSequences:
    merged = merge(pair[0], pair[1])
    if merged:
        for frame in range(1, 4):
            w = translate(merged, frame)
            if len(w) > 1:
                possibleWords += translate(merged, frame),
#print possibleWords

for possibility in unique(possibleWords):
    for word in englishWords:
        #print possibility
        if possibility.lower() == word.strip():
            correctWords += possibility,
        elif possibility.strip()[-1] == "S" and possibility.lower()[:-1] == word.strip(): # handle plural words
            correctWords += possibility,
print "Solutions: " + str(correctWords)

#    print "Complement: " + complement(sequence) + '\n'
#    print "Translate: " + translate(sequence, 1) + '\n'
#sprint merge("OMGROFL", "ROFLOMG")
#print merge("LOLFAK", "WHATTHEHELL")
## Global Alignment
### Scoring function - Determine how good the global alignment is (quality)
### Align every single character from one string to another


## Local Alignment
### String s, p - Try to find a portion of s and p where the global alignment is really good
* _____---________ in ______________---___
 * Alignment Cost - Cost and score of the alignment

 * Three things to identify:
  * Match: cost 5
  * Mismatch: cost -4
  * Insertion/Delection: cost -3
  * Gap penalty: -0.25
      - Gap of k characters
      - gap penalty = id + (k-1)**g

### Example: s1: ATTGAT, s2: ATGCAA
### Build matrix:
A   A   T   C   G   _
T   5   -4 -4  -3  -3
C   
G
_

#### Score: 10 (5+5-3+5-3+5-4)

### Mismatch means Mutation
## TT_A_TT (two different spots (not recurring), two mutations)
## TT_ATTT (one spot, one mutation)
s
### Maximizing Algorithm
## A[i, j] = max(A[i-1, j-1] + sub(si, pi), A[i-1, j] + id, A[i, j-1] + id)
## A[0,j] = id*j
## A[i, 0] = id*i
## sub is substitution matrix

## Convo with Bio majors: What input?
### Local alignment
### Input: 
    * Query file, one sequence "ATCG"
    * Batch file, multiple sequences. Highest score first, ascending Order
    * Gap penalty
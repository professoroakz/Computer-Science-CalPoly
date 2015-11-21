//  gap = id + (l-1)*e;
//  id:  gap opening penalty  (id < 0)
//  k:  length of the gap
//  g:  gap extension penalty (id < g < 0)

public class SequenceAlignment {
    String query, sequence;
    int[][] matrix;
    int pamOrBlossum = 0; // 1: Pam, 2: Blossum

    public SequenceAlignment(String query, String sequence, int pamOrBlossum) {
        this.pamOrBlossum = pamOrBlossum;
        this.query = query.toLowerCase();
        this.sequence = sequence.toLowerCase();

        /* Local alignment: Initialize matrix to 0 */
        matrix = new int[query.length() + 1][sequence.length() + 1];
        for(int i = 0; i < query.length(); i++)
            for(int j = 0; j < sequence.length(); j++)
                matrix[i][j] = 0;
    }

    /*  */
    public void localAlignment() {
        System.out.println("no");
    }


}
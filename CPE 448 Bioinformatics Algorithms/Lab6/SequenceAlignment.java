public class SequenceAlignment {
    String query, sequence;
    double[][] matrix;
    double pamOrBlossum = 0; // 1: Pam, 2: Blossum
    double gapPenalty = 0.25;
    double substitution = -3; // remove or add
    double match = 5;

    public SequenceAlignment(String query, String sequence, int pamOrBlossum, double gapPenalty) {
        this.gapPenalty = gapPenalty;
        this.pamOrBlossum = pamOrBlossum; // irrelevant for local alignment
        this.query = query.toLowerCase();
        this.sequence = sequence.toLowerCase();

        /* Local alignment: Initialize matrix to 0 */
        matrix = new double[query.length() + 1][sequence.length() + 1];
        for(int i = 0; i < query.length(); i++)
            for(int j = 0; j < sequence.length(); j++)
                matrix[i][j] = 0;
    }

    public void smithWaterman(){
        for(int i = 1; i < query.length(); i++){
            for (int j = 1; j < sequence.length(); j++) {
                double leftScore = matrix[i][j-1] + gapPenalty; // insertion
                double upScore = matrix[i-1][j] + gapPenalty;
                double diagScore = (matrix[i-1][j-1] + ((query.charAt(i-1) == sequence.charAt(j-1)) ? match : substitution));
                matrix[i][j] = Math.min(leftScore, Math.min(upScore, diagScore));
            }
        }
    }

    public void printMatrix(){
        for(int i = 0; i < query.length(); i++){
            for(int j = 0; j < sequence.length(); j++){
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SequenceAlignment sa = new SequenceAlignment("ACGT", "AGTTCACACACACAGACAGACGT", 0, 0.25);
        sa.smithWaterman();
        sa.printMatrix();
    }

}
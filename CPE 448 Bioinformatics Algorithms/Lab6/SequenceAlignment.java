import java.util.*;
import java.io.*;

public class SequenceAlignment {
    double[][] pam250;
    double[][] blosum62;

    String outputFileName;
    double gapPenalty;

    Writer writer;
    File queryFile, batchFile;
    Scanner queryScanner;
    Scanner batchScanner;

    String query = "";
    double[][] matrix;
    double pamOrBlossum = 0; // 1: Pam, 2: Blossum
    double substitution = -3; // remove or add
    double match = 5;

    public SequenceAlignment(double gapPenalty, String outputName) {
        this.gapPenalty = gapPenalty;
        outputFileName = outputName;
    }

    public boolean readFiles(File query, File batch) {
        queryFile = query;
        batchFile = batch;

        try {
            queryScanner = new Scanner(queryFile);

            queryScanner.nextLine(); // get rid of top line

            while(queryScanner.hasNextLine()) {
                this.query += queryScanner.nextLine().toUpperCase();
            }
System.out.println(query);
        } catch(FileNotFoundException e) {
            System.out.println("Query file " + queryFile.getName() + "is missing.");
            return false;
        }

        try {
            batchScanner = new Scanner(batchFile);
        } catch(FileNotFoundException e) {
            System.out.println("Batch file " + batchFile.getName() + "is missing.");
            return false;
        }

        try {
            writer = new PrintWriter(outputFileName);
        } catch(FileNotFoundException e) {
            System.out.println("Error when creating " + outputFileName);
            return false;
        }

        return true;
    }

    public void run() {
        
    }

    // public SequenceAlignment(String query, String sequence, int pamOrBlossum, double gapPenalty) {
    //     this.gapPenalty = gapPenalty;
    //     this.pamOrBlossum = pamOrBlossum; // irrelevant for local alignment
    //     this.query = query.toLowerCase();
    //     this.sequence = sequence.toLowerCase();

    //     /* Local alignment: Initialize matrix to 0 */
    //     matrix = new double[query.length() + 1][sequence.length() + 1];
    //     for(int i = 0; i < query.length(); i++)
    //         for(int j = 0; j < sequence.length(); j++)
    //             matrix[i][j] = 0;

        
    // }

    // public void smithWaterman(){
    //     for(int i = 1; i < query.length(); i++){
    //         for (int j = 1; j < sequence.length(); j++) {
    //             double leftScore = matrix[i][j-1] + gapPenalty; // insertion
    //             double upScore = matrix[i-1][j] + gapPenalty;
    //             double diagScore = (matrix[i-1][j-1] + ((query.charAt(i-1) == sequence.charAt(j-1)) ? match : substitution));
    //             matrix[i][j] = Math.min(leftScore, Math.min(upScore, diagScore));
    //         }
    //     }
    // }

    // public void printMatrix(){
    //     for(int i = 0; i < query.length(); i++){
    //         for(int j = 0; j < sequence.length(); j++){
    //             System.out.print(matrix[i][j] + "\t");
    //         }
    //         System.out.println();
    //     }
    // }

    // public static void main(String[] args) {
    //     SequenceAlignment sa = new SequenceAlignment("ACGT", "AGTTCACACACACAGACAGACGT", 0, 0.25);
    //     sa.smithWaterman();
    //     sa.printMatrix();
    // }

    public void buildMatrices() {
        pam250 =  new double [][] {
                        {13,    6,    9,    9,    5,   8,   9,  12,   6,   8,   6,   7,   7,   4,  11,  11,  11,   2,   4,   9},
                        { 3,   17,    4,    3,    2,   5,   3,   2,   6,   3,   2,   9,   4,   1,   4,   4,   3,   7,   2,   2},
                        { 4,    4,    6,    7,    2,   5,   6,   4,   6,   3,   2,   5,   3,   2,   4,   5,   4,   2,   3,   3},
                        { 5,    4,    8,   11,    1,   7,  10,   5,   6,   3,   2,   5,   3,   1,   4,   5,   5,   1,   2,   3},
                        { 2,    1,    1,    1,   52,   1,   1,   2,   2,   2,   1,   1,   1,   1,   2,   3,   2,   1,   4,   2},
                        { 3,    5,    5,    6,    1,  10,   7,   3,   7,   2,   3,   5,   3,   1,   4,   3,   3,   1,   2,   3},
                        { 5,    4,    7,   11,    1,   9,  12,   5,   6,   3,   2,   5,   3,   1,   4,   5,   5,   1,   2,   3},
                        {12,    5,   10,   10,    4,   7,   9,  27,   5,   5,   4,   6,   5,   3,   8,  11,   9,   2,   3,   7},
                        { 2,    5,    5,    4,    2,   7,   4,   2,  15,   2,   2,   3,   2,   2,   3,   3,   2,   2,   3,   2},
                        { 3,    2,    2,    2,    2,   2,   2,   2,   2,  10,   6,   2,   6,   5,   2,   3,   4,   1,   3,   9},
                        { 6,    4,    4,    3,    2,   6,   4,   3,   5,  15,  34,   4,  20,  13,   5,   4,   6,   6,   7,  13},
                        { 6,   18,   10,    8,    2,  10,   8,   5,   8,   5,   4,  24,   9,   2,   6,   8,   8,   4,   3,   5},
                        { 1,    1,    1,    1,    0,   1,   1,   1,   1,   2,   3,   2,   6,   2,   1,   1,   1,   1,   1,   2},
                        { 2,    1,    2,    1,    1,   1,   1,   1,   3,   5,   6,   1,   4,  32,   1,   2,   2,   4,  20,   3},
                        { 7,    5,    5,    4,    3,   5,   4,   5,   5,   3,   3,   4,   3,   2,  20,   6,   5,   1,   2,   4},
                        { 9,    6,    8,    7,    7,   6,   7,   9,   6,   5,   4,   7,   5,   3,   9,  10,   9,   4,   4,   6},
                        { 8,    5,    6,    6,    4,   5,   5,   6,   4,   6,   4,   6,   5,   3,   6,   8,  11,   2,   3,   6},
                        { 0,    2,    0,    0,    0,   0,   0,   0,   1,   0,   1,   0,   0,   1,   0,   1,   0,  55,   1,   0},
                        { 1,    1,    2,    1,    3,   1,   1,   1,   3,   2,   2,   1,   2,  15,   1,   2,   2,   3,  31,   2},
                        { 7,    4,    4,    4,    4,   4,   4,   4,   5,   4,  15,   10,   4,  10,   5,   5,   5,  72,   4,  17}};
        blosum62 = new double [][] {
                        { 4, -1, -2, -2,  0, -1, -1,  0, -2, -1, -1, -1, -1, -2, -1,  1,  0, -3, -2,  0},
                        {-1,  5,  0, -2, -3,  1,  0, -2,  0, -3, -2,  2, -1, -3, -2, -1, -1, -3, -2, -3},
                        {-2,  0,  6,  1, -3,  0,  0,  0,  1, -3, -3,  0, -2, -3, -2,  1,  0, -4, -2, -3},
                        {-2, -2,  1,  6, -3,  0,  2, -1, -1, -3, -4, -1, -3, -3, -1,  0, -1, -4, -3, -3},
                        { 0, -3, -3, -3,  9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1},
                        {-1,  1,  0,  0, -3,  5,  2, -2,  0, -3, -2,  1,  0, -3, -1,  0, -1, -2, -1, -2},
                        {-1,  0,  0,  2, -4,  2,  5, -2,  0, -3, -3,  1, -2, -3, -1,  0, -1, -3, -2, -2},
                        { 0, -2,  0, -1, -3, -2, -2,  6, -2, -4, -4, -2, -3, -3, -2,  0, -2, -2, -3, -3},
                        {-2,  0,  1, -1, -3,  0,  0, -2,  8, -3, -3, -1, -2, -1, -2, -1, -2, -2,  2, -3},
                        {-1, -3, -3, -3, -1, -3, -3, -4, -3,  4,  2, -3,  1,  0, -3, -2, -1, -3, -1,  3},
                        {-1, -2, -3, -4, -1, -2, -3, -4, -3,  2,  4, -2,  2,  0, -3, -2, -1, -2, -1,  1},
                        {-1,  2,  0, -1, -3,  1,  1, -2, -1, -3, -2,  5, -1, -3, -1,  0, -1, -3, -2, -2},
                        {-1, -1, -2, -3, -1,  0, -2, -3, -2,  1,  2, -1,  5,  0, -2, -1, -1, -1, -1,  1},
                        {-2, -3, -3, -3, -2, -3, -3, -3, -1,  0,  0, -3,  0,  6, -4, -2, -2,  1,  3, -1},
                        {-1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4,  7, -1, -1, -4, -3, -2},
                        { 1, -1,  1,  0, -1,  0,  0,  0, -1, -2, -2,  0, -1, -2, -1,  4,  1, -3, -2, -2},
                        { 0, -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1,  1,  5, -2, -2,  0},
                        {-3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1,  1, -4, -3, -2, 11,  2, -3},
                        {-2, -2, -2, -3, -2, -1, -2, -3,  2, -1, -1, -2, -1,  3, -3, -2, -2,  2,  7, -1},
                        { 0, -3, -3, -3, -1, -2, -2, -3, -3,  3,  1, -2,  1, -1, -2, -2,  0, -3, -1,  4}};
    }
}

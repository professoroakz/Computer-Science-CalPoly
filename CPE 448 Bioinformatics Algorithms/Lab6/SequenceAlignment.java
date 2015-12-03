import java.util.*;
import java.io.*;

public class SequenceAlignment {
    double[][] pam250;
    double[][] blosum62;
    HashMap<Character, Integer> letterIdx;
    ArrayList<Alignment> alignments;

    String outputFileName;
    double gapPenalty;

    PrintWriter writer;
    File queryFile, batchFile;
    Scanner queryScanner;
    Scanner batchScanner;

    int iPos = -1;
    int jPos = -1;

    String query = "";
    double[][] matrix;
    double[][] uMatrix; // 1 = Up; 0 = Diagonal; -1 = Left
    int pamOrBlosum = 0; // 1: Pam, 2: Blossum

    public SequenceAlignment(double gapPenalty, String outputName, int pamBlosum) {
        this.gapPenalty = gapPenalty;
        outputFileName = outputName;
        pamOrBlosum = pamBlosum;
        buildMatrices();

        alignments = new ArrayList<Alignment>();

        letterIdx = new HashMap<Character, Integer>();
        letterIdx.put('A',0);
        letterIdx.put('R',1);
        letterIdx.put('N',2);
        letterIdx.put('D',3);
        letterIdx.put('C',4);
        letterIdx.put('Q',5);
        letterIdx.put('E',6);
        letterIdx.put('G',7);
        letterIdx.put('H',8);
        letterIdx.put('I',9);
        letterIdx.put('L',10);
        letterIdx.put('K',11);
        letterIdx.put('M',12);
        letterIdx.put('F',13);
        letterIdx.put('P',14);
        letterIdx.put('S',15);
        letterIdx.put('T',16);
        letterIdx.put('W',17);
        letterIdx.put('Y',18);
        letterIdx.put('V',19);
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
        String line = "";
        String sequenceLine = "";
        String currentSequence = "";

        double score = 0;
        int pos = -1;

        while(batchScanner.hasNextLine()) {
            line = batchScanner.nextLine().toUpperCase();

            if(line.indexOf(">") > -1) {
                sequenceLine = line;
                line = "";
                currentSequence = "";
            } else {
                currentSequence += line;
                while(batchScanner.hasNextLine()) {
                    line = batchScanner.nextLine().toUpperCase();

                    if(line.trim().length() > 0) {
                        currentSequence += line;
                    } else {
                        break;
                    }
                }

                createMatrix(query, currentSequence);
                score = smithWaterman(query, currentSequence);
                pos = recoverPosition(query, currentSequence);

                alignments.add(new Alignment(sequenceLine, score, pos));

            }
        }

        Collections.sort(alignments);
        Collections.reverse(alignments);

        doOutput();
    }

    public void doOutput() {
        writer.print("Matrix used: ");
        writer.println(pamOrBlosum == 1 ? "Pam\n" : "Blosum\n");
        writer.println("GapPenalty: " + gapPenalty + "\n");

        writer.println("Query sequence: " + query + "\n");

        for(Alignment a : alignments) {
            if(!Double.isNaN(a.score)) {
                writer.println(a.sequenceName);
                writer.println(a.score + ", " + a.position + "\n");
            } else {
                writer.print(a.score + " ::::::: ");
                writer.println("There is some error with sequence: " + a.sequenceName);

            }
        }

        writer.flush();
        try {
            writer.close();
        } catch(Exception e) {
            // do nothing
        }
    }

    public void createMatrix(String query, String sequence) {
        matrix = new double[query.length() + 1][sequence.length() + 1];
        for(int i = 0; i < query.length() + 1; i++) {
            for(int j = 0; j < sequence.length() + 1; j++) {
                matrix[i][j] = 0;
            }
        }

        uMatrix = new double[query.length() + 1][sequence.length() + 1];
        for(int i = 0; i < query.length() + 1; i++) {
            for(int j = 0; j < sequence.length() + 1; j++) {
                uMatrix[i][j] = -2;
            }
        }
    }

    public void printMatrix(String query, String sequence) {
        for(int i = 0; i < query.length() + 1; i++) {
            for(int j = 0; j < sequence.length() + 1; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public double smithWaterman(String query, String sequence) {
        double[][] subMatrix = (pamOrBlosum == 1 ? pam250 : blosum62);
        double bestVal = 0;
        int bestIPos = -1;
        int bestJPos = -1;

        double leftScore;
        double upScore;
        double diagScore;

        for(int i = 1; i < query.length() + 1; i++) {
            for(int j = 1; j < sequence.length() + 1; j++) {
                leftScore = matrix[i][j-1] + gapPenalty;
                upScore = matrix[i-1][j] + gapPenalty;
                try {
                    diagScore = matrix[i-1][j-1] + 
                        subMatrix[letterIdx.get(query.charAt(i-1))][letterIdx.get(sequence.charAt(j-1))];
                } catch(ArrayIndexOutOfBoundsException e) {
                    return Float.NaN;
                } catch(NullPointerException e) {
                    return Float.NaN;
                }
                

                matrix[i][j] = Math.max(0, Math.max(leftScore, Math.max(upScore, diagScore)));

                if(leftScore > upScore && leftScore > diagScore) {
                    uMatrix[i][j] = -1;
                } else if(upScore > leftScore && upScore > diagScore) {
                    uMatrix[i][j] = 1;
                } else if(diagScore > upScore && diagScore > leftScore) {
                    uMatrix[i][j] = 0;
                }

                if(matrix[i][j] > bestVal) {
                    bestVal = matrix[i][j];
                    bestIPos = i;
                    bestJPos = j;
                }
            }
        }

        iPos = bestIPos;
        jPos = bestJPos;
        return bestVal;
    }

    public int recoverPosition(String query, String sequence) {
        int i = iPos;
        int j = jPos;

        while(matrix[i][j] > 0) {
            if(uMatrix[i][j] == 0) {
                i--;
                j--;
            } else if(uMatrix[i][j] == -1) {
                j--;
            } else {
                i--;
            }
        }
        return j + 1; // offset the extra row added for matrix
    }

    public void buildMatrices() {
        pam250 = new double [][] {
            { 2,-2, 0, 0,-2, 0, 0, 1,-1,-1,-2,-1,-1,-4, 1, 1, 1,-6,-3, 0}, //A
            {-2, 6, 0,-1,-4, 1,-1,-3, 2,-2,-3, 3, 0,-4, 0, 0,-1, 2,-4,-2}, //R
            { 0, 0, 2, 2,-4, 1, 1, 0, 2,-2,-3, 1,-2,-4,-1, 1, 0,-4,-2,-2}, //N
            { 0,-1, 2, 4,-5, 2, 3, 1, 1,-2,-4, 0,-3,-6,-1, 0, 0,-7,-4,-2}, //D
            {-2,-4,-4,-5, 4,-5,-5,-3,-3,-2,-6,-5,-5,-4,-3, 0,-2,-8, 0,-2}, //C
            { 0, 1, 1, 2,-5, 4, 2,-1, 3,-2,-2, 1,-1,-5, 0,-1,-1,-5,-4,-2}, //Q
            { 0,-1, 1, 3,-5, 2, 4, 0, 1,-2,-3, 0,-2,-5,-1, 0, 0,-7,-4,-2}, //E
            { 1,-3, 0, 1,-3,-1, 0, 5,-2,-3,-4,-2,-3,-5,-1, 1, 0,-7,-5,-1}, //G
            {-1, 2, 2, 1,-3, 3, 1,-2, 6,-2,-2, 0,-2,-2, 0,-1,-1,-3, 0,-2}, //H
            {-1,-2,-2,-2,-2,-2,-2,-3,-2, 5, 2,-2, 2, 1,-2,-1, 0,-5,-1, 4}, //I
            {-2,-3,-3,-4,-6,-2,-3,-4,-2, 2, 6,-3, 4, 2,-3,-3,-2,-2,-1, 2}, //L
            {-1, 3, 1, 0,-5, 1, 0,-2, 0,-2,-3, 5, 0,-5,-1, 0, 0,-3,-4,-2}, //K
            {-1, 0,-2,-3,-5,-1,-2,-3,-2, 2, 4, 0, 6, 0,-2,-2,-1,-4,-2, 2}, //M
            {-4,-4,-4,-6,-4,-5,-5,-5,-2, 1, 2,-5, 0, 9,-5,-3,-2, 0, 7,-1}, //F
            { 1, 0,-1,-1,-3, 0,-1,-1, 0,-2,-3,-1,-2,-5, 6, 1, 0,-6,-5,-1}, //P
            { 1, 0, 1, 0, 0,-1, 0, 1,-1,-1,-3, 0,-2,-3, 1, 3, 1,-2,-3,-1}, //S
            { 1,-1, 0, 0,-2,-1, 0, 0,-1, 0,-2, 0,-1,-2, 0, 1, 3,-5,-3, 0}, //T
            {-6, 2,-4,-7,-8,-5,-7,-7,-3,-5,-2,-3,-4, 0,-6,-2,-5,17, 0,-6}, //W
            {-3,-4,-2,-4, 0,-4,-4,-5, 0,-1,-1,-4,-2, 7,-5,-3,-3, 0,10,-2}, //Y
            { 0,-2,-2,-2,-2,-2,-2,-1,-2, 4, 2,-2, 2,-1,-1,-1, 0,-6,-2, 4}};//V

        blosum62 = new double [][] {
            { 4,-1,-2,-2, 0,-1,-1, 0,-2,-1,-1,-1,-1,-2,-1, 1, 0,-3,-2, 0}, //A
            {-1, 5, 0,-2,-3, 1, 0,-2, 0,-3,-2, 2,-1,-3,-2,-1,-1,-3,-2,-3}, //R
            {-2, 0, 6, 1,-3, 0, 0, 0, 1,-3,-3, 0,-2,-3,-2, 1, 0,-4,-2,-3}, //N
            {-2,-2, 1, 6,-3, 0, 2,-1,-1,-3,-4,-1,-3,-3,-1, 0,-1,-4,-3,-3}, //D
            { 0,-3,-3,-3, 9,-3,-4,-3,-3,-1,-1,-3,-1,-2,-3,-1,-1,-2,-2,-1}, //C
            {-1, 1, 0, 0,-3, 5, 2,-2, 0,-3,-2, 1, 0,-3,-1, 0,-1,-3,-2,-2}, //Q
            {-1, 0, 0, 2,-4, 2, 5,-2, 0,-3,-3, 1,-2,-3,-1, 0,-1,-3,-2,-2}, //E
            { 0,-2, 0,-1,-3,-2,-2, 6,-2,-4,-4,-2,-3,-3,-2, 0,-2,-2,-3,-3}, //G
            {-2, 0, 1,-1,-3, 0, 0,-2, 8,-3,-3,-1,-2,-1,-2,-1,-2,-2, 2,-3}, //H
            {-1,-3,-3,-3,-1,-3,-3,-4,-3, 4, 2,-3, 1, 0,-3,-2,-1,-3,-1, 3}, //I
            {-1,-2,-3,-4,-1,-2,-3,-4,-3, 2, 4,-2, 2, 0,-3,-2,-1,-2,-1, 1}, //L
            {-1, 2, 0,-1,-3, 1, 1,-2,-1,-3,-2, 5,-1,-3,-1, 0,-1,-3,-2,-2}, //K
            {-1,-1,-2,-3,-1, 0,-2,-3,-2, 1, 2,-1, 5, 0,-2,-1,-1,-1,-1, 1}, //M
            {-2,-3,-3,-3,-2,-3,-3,-3,-1, 0, 0,-3, 0, 6,-4,-2,-2, 1, 3,-1}, //F
            {-1,-2,-2,-1,-3,-1,-1,-2,-2,-3,-3,-1,-2,-4, 7,-1,-1,-4,-3,-2}, //P
            { 1,-1, 1, 0,-1, 0, 0, 0,-1,-2,-2, 0,-1,-2,-1, 4, 1,-3,-2,-2}, //S
            { 0,-1, 0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1, 1, 5,-2,-2, 0}, //T
            {-3,-3,-4,-4,-2,-2,-3,-2,-2,-3,-2,-3,-1, 1,-4,-3,-2,11, 2,-3}, //W
            {-2,-2,-2,-3,-2,-1,-2,-3, 2,-1,-1,-2,-1, 3,-3,-2,-2, 2, 7,-1}, //Y
            { 0,-3,-3,-3,-1,-2,-2,-3,-3, 3, 1,-2, 1,-1,-2,-2, 0,-3,-1, 4}};//V
    }
}

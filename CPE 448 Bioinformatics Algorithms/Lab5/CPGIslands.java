import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;

public class CPGIslands {

          File file;
          Scanner scan;
          PrintWriter writer;

          String seq = "";
          String seqToUpper = "";

          int windowSize = 200;
          int stepSize = 1;

          int gcCount;
          int cCount;
          int gCount;

          int currentCpG;
          int oldCpG;
          int currentStartIndex;
          int currentEndIndex;

          int observedExpected;

          public CPGIslands(int window, int step) {
               gcCount = 0;
               windowSize = window;
               stepSize = step;
          }

          public void readFile(File file) {
               this.file = file;

               try {
                    scan = new Scanner(this.file);
                    while(scan.hasNextLine()) {
                         seq += scan.nextLine();
                    }
                    seqToUpper = seq.toUpperCase();
                    System.out.println(seq);
                    System.out.println(seq.length());
               }
               catch(FileNotFoundException e) {
                    System.out.println("File " + file.getName() + " is missing.");
               }
          }

          /*
           * @deprecated Replaced by #readFile(File file).
           */
          @Deprecated
          public void readFile(String fileName){
               file = new File(fileName);
               try {
                    scan = new Scanner(file);
                    scan.nextLine();
                    while(scan.hasNextLine()) {
                         seq += scan.nextLine();
                    }

               }
               catch(FileNotFoundException e) {
                    System.out.println("File " + " is missing.");
               }
          }


          public void readRollingGCCount() {
               try {
                    writer = new PrintWriter("output.txt");
               }
               catch(FileNotFoundException e) {
                    System.out.println("Error when writing to output.txt");
                    System.exit(1);
               }


               writer.println("start,end,observed/expected value,%GC");

               int fullSeqLength = seqToUpper.length();
               int currentLocation = 1;
               int currentEndLocation = windowSize;
               gcCount = 0;

               if(currentEndLocation > fullSeqLength) {
                    currentEndLocation = fullSeqLength - 1;
               }

               while(currentEndLocation <= fullSeqLength - 1) {


                    calcCPGIslands(currentLocation, currentEndLocation, fullSeqLength - 1);

                    currentLocation += stepSize;
                    currentEndLocation += stepSize;

               }

               writer.flush();
               System.out.println("Congratulations! Your file is successfully downloaded to 'output.txt'");
               try {
                    writer.close();
               }
               catch(Exception e) {
                    // don't do anything. We just want to close this.
               }
          }

          public void calcCPGIslands(int startPos, int endPos, int fullSeqLength) {
               for(int i = startPos; i < endPos; i++) {
                    if(seqToUpper.charAt(i-1) == 'C' && seqToUpper.charAt(i) == 'G') {
                         currentCpG++;
                    }
               }

               for(int i = startPos; i < endPos; i++) {
                    if(seqToUpper.charAt(i) == 'G') {
                              gCount++;
                    }
                    if(seqToUpper.charAt(i) == 'C') {
                         cCount++;
                    }
               }

               DecimalFormat df = new DecimalFormat("##.##");

               String formattedGC = df.format((((double)(gCount + cCount) / fullSeqLength) * 100));
               String formattedObservedExpected = df.format(((double)(currentCpG * fullSeqLength) / (double)(cCount * gCount)));

               writer.println((startPos) + "," + (endPos) + "," + formattedObservedExpected + "," + formattedGC);

               currentCpG = 0;
               gCount = 0;
               cCount = 0;
          }
}

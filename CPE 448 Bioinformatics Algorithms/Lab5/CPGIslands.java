import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;

public class CPGIslands {

          File file;
          Scanner scan;
          PrintWriter writer;
          String outputFilename;

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

          public CPGIslands(int window, int step, String output) {
               gcCount = 0;
               windowSize = window;
               stepSize = step;
               outputFilename = output;
          }

          public boolean readFile(File file) {
               this.file = file;

               try {
                    scan = new Scanner(this.file);
                    scan.nextLine(); // get rid of the > line
                    while(scan.hasNextLine()) {
                         seq += scan.nextLine();
                    }
                    seqToUpper = seq.toUpperCase();
                    // System.out.println(seq);
                    // System.out.println(seq.length());
               }
               catch(FileNotFoundException e) {
                    System.out.println("File " + file.getName() + " is missing.");
                    return false;
               }

               return true;
          }

          public void readRollingGCCount() {
               try {
                    writer = new PrintWriter(outputFilename);
               }
               catch(FileNotFoundException e) {
                    System.out.println("Error when writing to " + outputFilename);
                    System.exit(1);
               }


               writer.println("Start,End,Observed/Eexpected value,%GC");

               int fullSeqLength = seqToUpper.length();
               int currentLocation = 1;
               int currentEndLocation = windowSize;

               gcCount = 0;

               if(currentEndLocation >= fullSeqLength) {
                    currentEndLocation = fullSeqLength;
               }

               while(currentLocation <= currentEndLocation) {
                    // System.out.println("currentLoc: " + currentLocation + " - currentEndLoc: " + currentEndLocation + " - fullSeqLength: " + fullSeqLength);
                    calcCPGIslands(currentLocation, currentEndLocation, fullSeqLength);

                    currentLocation += stepSize;

                    if(currentEndLocation < fullSeqLength) {
                         // System.out.println("in?");
                         currentEndLocation += stepSize;
                    }
               }

               // while(currentEndLocation <= fullSeqLength) {

               //      calcCPGIslands(currentLocation, currentEndLocation, fullSeqLength - 1);

               //      currentLocation += stepSize;
               //      currentEndLocation += stepSize;

               // }

               writer.flush();
               System.out.println("Congratulations! Your file is successfully downloaded to '" + outputFilename + "'");
               try {
                    writer.close();
               }
               catch(Exception e) {
                    // don't do anything. We just want to close this.
               }
          }

          public void calcCPGIslands(int startPos, int endPos, int fullSeqLength) {
               for(int i = startPos; i < endPos; i++) {
                    // System.out.println("i: " + i + " : seqToUpperLen: " + seqToUpper.length() + " : startPos: " + startPos + " : endPos: " + endPos);
                    if(seqToUpper.charAt(i-1) == 'C' && seqToUpper.charAt(i) == 'G') {
                         currentCpG++;
                    }
               }

               for(int i = startPos - 1; i < endPos; i++) {
                    if(seqToUpper.charAt(i) == 'G') {
                         // System.out.println("in G: " + i);
                              gCount++;
                    }
                    if(seqToUpper.charAt(i) == 'C') {
                         // System.out.println("in C: " + i);
                         cCount++;
                    }
               }

               DecimalFormat df = new DecimalFormat("##.##");

               String formattedGC = df.format((((double)(gCount + cCount) / (endPos - startPos + 1)) * 100));
     // System.out.println("currentCPG: " + currentCpG + " - total nucleotides: " + fullSeqLength + " - Cs: " + cCount + " - Gs: " + gCount);
               String formattedObservedExpected = df.format(((double)(currentCpG * fullSeqLength) / (double)(cCount * gCount)));

               writer.println((startPos) + "," + (endPos) + "," + formattedObservedExpected + "," + formattedGC);

               currentCpG = 0;
               gCount = 0;
               cCount = 0;
          }
}

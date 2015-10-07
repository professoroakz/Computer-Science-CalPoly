import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;

public class GCCounter {

          File file;
          Scanner scan;
          PrintWriter writer;

          String seq = "";
          
          int windowSize = -1;
          int stepSize = -1;

          int gcCount;
          int nCount;
          
          public GCCounter(int window, int step) {
               gcCount = 0;
               nCount = 0;
               windowSize = window;
               stepSize = step;
          }

          public void readFile(File file) {
               this.file = file;

               try {
                    scan = new Scanner(this.file);
                    scan.nextLine();
                    while(scan.hasNextLine()) {
                         seq += scan.nextLine();
                    }
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
          
          public boolean readBasicGCCount() {

               try {
                    writer = new PrintWriter("output.txt");
               }
               catch(FileNotFoundException e) {
                    System.out.println("Error when writing to output.txt");
                    return false; // there was an error, so don't do anything more
               }
     
               for(int i = 0; i < seq.length(); i++) {
                    checkChar(seq.charAt(i));
               }
               
               writer.println("# of N's present: " + nCount); // Output the total number of N's

               DecimalFormat df = new DecimalFormat("#.#");
               String formatted = df.format(((double)gcCount / (seq.length() - nCount) * 100));
               
               writer.println("%GC overall: " + formatted + "%"); // Output the overall GC %

               return true; // the basic GC succeeds
          }

          public void readRollingGCCount() {
               writer.println("Window Size: " + windowSize + ",Step Size: " + stepSize);  // Output the window and step sizes
               writer.println("Nucleotide Position,%GC");                                 // Output the column headers

               int fullSeqLength = seq.length();
               int currentLocation = 0;
               int currentEndLocation = windowSize;
               String currentSequence;// = seq.substring(currentLocation, currentEndLocation);
               DecimalFormat df = new DecimalFormat("#.#");

               while(currentEndLocation <= fullSeqLength - 1) {
                    gcCount = 0;
                    nCount = 0;
                    currentSequence = seq.substring(currentLocation, currentEndLocation);

                    for(int i = 0; i < currentSequence.length(); i++) {
                         checkChar(currentSequence.charAt(i));
                    }

                    String formatted = df.format(((double)gcCount / (currentSequence.length() - nCount) * 100));

                    writer.println((currentLocation + 1) + "," + formatted);              // Output the nucleotide position and %GC
                                                                                          // Keep it 1-start as Requirements need

                    currentLocation += stepSize;
                    currentEndLocation += stepSize;
               }

               // check if there is still a partial sequence to compute
               if(currentLocation <= fullSeqLength - 1) {
                    gcCount = 0;
                    nCount = 0;
                    currentSequence = seq.substring(currentLocation);

                    for(int i = 0; i < currentSequence.length(); i++) {
                         checkChar(currentSequence.charAt(i));
                    }

                    // use separate String variable in case the while-loop is never entered
                    String formattedPartial = df.format(((double)gcCount / (currentSequence.length() - nCount) * 100));

                    writer.println((currentLocation + 1) + "," + formattedPartial);       // Output the nucleotide position and %GC
                                                                                          // Keep it 1-start as Requirements need
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

          private void checkChar(char currentChar) {
               if(currentChar == 'G' || currentChar == 'C' || currentChar == 'g' || currentChar == 'c') {
                    gcCount++;
               }
               else if(currentChar == 'n' || currentChar == 'N') {
                    nCount++;
               }
               // if(currentChar != 'A' && )
          }
}

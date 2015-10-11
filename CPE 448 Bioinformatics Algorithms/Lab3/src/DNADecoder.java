import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;

public class DNADecoder {

          File fastaFile;
          File gffFile;
          Scanner fastaScanner;
          Scanner gffScanner;
          PrintWriter writer;

          String seq = "";
          

          int gcCount;
          int nCount;
          
          public DNADecoder() {
               gcCount = 0;
               nCount = 0;
          }

          public boolean readFiles(File fastaFile, File gffFile) {
               this.fastaFile = fastaFile;
               this.gffFile = gffFile;

               try {
                    fastaScanner = new Scanner(this.fastaFile);
               }
               catch(FileNotFoundException e) {
                    System.out.println("FASTA file " + fastaFile.getName() + " is missing.");
                    return false;
               }

               try {
                    gffScanner = new Scanner(this.gffFile);
               }
               catch(FileNotFoundException e) {
                    System.out.println("GFF file " + gffFile.getName() + " is missing.");
                    return false;
               }

               fastaScanner.nextLine(); // Scan the first line, but we don't care about it
               while(fastaScanner.hasNextLine()) {
                    seq += fastaScanner.nextLine();
               }

               return true;

          }
          
          public boolean readBasicGCCount() {

               try {
                    writer = new PrintWriter("output.txt");
               }
               catch(FileNotFoundException e) {
                    System.out.println("Error when writing to output.txt");
                    return false; // there was an error, so don't do anything more
               }
     
               try {
                    for(int i = 0; i < seq.length(); i++) {
                         // checkChar(seq.charAt(i));
                    }
               }
               catch(Exception e) {
                    System.out.println("Invalid input. Please try another file.");
                    try {
                         writer.close();
                    }
                    catch(Exception ex) {
                         // do nothing
                    }
                    return false;
               }
               
               writer.println("# of N's present: " + nCount); // Output the total number of N's

               DecimalFormat df = new DecimalFormat("#.#");
               String formatted = df.format(((double)gcCount / (seq.length() - nCount) * 100));
               
               writer.println("%GC overall: " + formatted + "%"); // Output the overall GC %

               return true; // the basic GC succeeds
          }

}

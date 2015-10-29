import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class Controller {
     File queryFile;
     File subjectFile;
     Scanner queryScanner;
     Scanner subjectScanner;
     PrintWriter writer;
     String outputFilename;

     ArrayList<String> queries;
     String subjectSequence = "";

     public Controller(String output) {
          outputFilename = output;
          queries = new ArrayList<String>();
     }

     public boolean readFiles(File query, File subject) {
          queryFile = query;
          subjectFile = subject;

          try {
               queryScanner = new Scanner(queryFile);
          }
          catch(FileNotFoundException e) {
               System.out.println("Query FASTA file " + queryFile.getName() + " is missing.");
               return false;
          }

          try {
               subjectScanner = new Scanner(subjectFile);
          }
          catch(FileNotFoundException e) {
               System.out.println("Subject FASTA file " + subjectFile.getName() + " is missing.");
               return false;
          }

          try {
               writer = new PrintWriter(outputFilename);
          }
          catch(FileNotFoundException e) {
               System.out.println("Error when creating " + outputFilename);
               return false;
          }

          subjectScanner.nextLine();
          while(subjectScanner.hasNextLine()) {
               subjectSequence += subjectScanner.nextLine().toUpperCase();
          }

          return true;
     }

     public boolean loadQueries() {
          String currentQuery = "";
          boolean hasMoreQueries = false;
          boolean currentQueryAdded = false;
          String line;

          try {
               line = queryScanner.nextLine().toUpperCase(); // scan the first line with >
          } catch(Exception e) {
               System.out.println("Query file is empty. Please fix and run again.");
               closeOutputFile(false);
               return false;
          }

          if(line.charAt(0) != '>') {
               System.out.println("Queries incorrectly separated. Please fix and run again.");
               closeOutputFile(false);
               return false;
          }

          do {
               while(queryScanner.hasNextLine()) {
                    hasMoreQueries = false;
                    line = queryScanner.nextLine().toUpperCase();

                    if(line.trim().length() == 0) {
                         System.out.println("Empty line found in QueryFile " + queryFile.getName() + ". Please fix and run again.");
                         closeOutputFile(false);
                         return false;
                    }

                    if(line.charAt(0) == '>') {
                         if(!currentQueryAdded) {
                              addExtraSequences(currentQuery);
                         } else {
                              System.out.println("Queries incorrectly separated. Please fix and run again.");
                              closeOutputFile(false);
                              return false;
                         }
                         currentQueryAdded = true;
                         currentQuery = "";
                         // we have reached the end of this query, but we have another
                         hasMoreQueries = true;
                         break;
                    } else {
                         currentQuery += line;
                         currentQueryAdded = false;
                    }
                    
               }
          } while(hasMoreQueries && queryScanner.hasNextLine());

          // need to add the last query, but only if the last line of the file was a query
          if(!currentQueryAdded) {
               addExtraSequences(currentQuery);
          }

          return true;
     }

     public void run() {
          int startPosition;
          int[] startPositions;///*;*/ = {0}; // REMOVE THIS DECLARATION LATER! IT IS USELESS
          SuffixTree suffixTree = new SuffixTree(subjectSequence + "$");
          suffixTree.constructTree();
          
          writer.println("Query Found,Nucleotide Start,Nucleotide End");

          for(String query : queries) {
               startPositions = suffixTree.findStartPositions(query); // UNCOMMENT THIS OUT WHEN NEW METHODS ADDED TO SuffixTree.java
               if(startPositions != null) {

                    for(int i = 0; i < startPositions.length; i++) {
                         startPosition = startPositions[i] + 1;///*startPositions[i]*/0;
                         writer.print(query + ",");
                         writer.print(startPosition + ",");
                         writer.println((startPosition + query.length() - 1)); // this is to get the position of the end nucleotide
                    }                    
               }
          }

          closeOutputFile(true);
     }

     private void closeOutputFile(boolean isGoodExit) {
          try {
               writer.close();
               if(isGoodExit) {
                    System.out.println("Congratulations! Your file is successfully downloaded to '" + outputFilename + "'");
               }
          } catch(Exception e) {
               // do nothing, we want to close the file
          }
     }

     /*
      * Adds the extra sequences for wild cards and reverse compliments
      * for a given string.
      */
     private void addExtraSequences(String query) {
          // wild cards, then reverse compliments
          int numWildCards = 0;
          StringBuilder wild1 = new StringBuilder();
          StringBuilder wild2 = new StringBuilder();
          StringBuilder wild3 = new StringBuilder();
          StringBuilder wild4 = new StringBuilder();

          for(int i = 0; i < query.length(); i++) {
               switch(query.charAt(i)) {
                    case 'W': if(numWildCards == 0) {
                                   wild1.append('A'); wild2.append('T'); wild3.append('A'); wild4.append('T');
                              } else {
                                   wild1.append('T'); wild2.append('T'); wild3.append('A'); wild4.append('A');
                              }
                              numWildCards++;
                              break;

                    case 'S': if(numWildCards == 0) {
                                   wild1.append('C'); wild2.append('G'); wild3.append('C'); wild4.append('G');
                              } else {
                                   wild1.append('G'); wild2.append('G'); wild3.append('C'); wild4.append('C');
                              }
                              numWildCards++;
                              break;

                    case 'M': if(numWildCards == 0) {
                                   wild1.append('A'); wild2.append('C'); wild3.append('A'); wild4.append('C');
                              } else {
                                   wild1.append('C'); wild2.append('C'); wild3.append('A'); wild4.append('A');
                              }
                              numWildCards++;
                              break;

                    case 'K': if(numWildCards == 0) {
                                   wild1.append('G'); wild2.append('T'); wild3.append('G'); wild4.append('T');
                              } else {
                                   wild1.append('T'); wild2.append('T'); wild3.append('G'); wild4.append('G');
                              }
                              numWildCards++;
                              break;
                              
                    case 'R': if(numWildCards == 0) {
                                   wild1.append('A'); wild2.append('G'); wild3.append('A'); wild4.append('G');
                              } else {
                                   wild1.append('G'); wild2.append('G'); wild3.append('A'); wild4.append('A');
                              }
                              numWildCards++;
                              break;
                              
                    case 'Y': if(numWildCards == 0) {
                                   wild1.append('C'); wild2.append('T'); wild3.append('C'); wild4.append('T');
                              } else {
                                   wild1.append('T'); wild2.append('T'); wild3.append('C'); wild4.append('C');
                              }
                              numWildCards++;
                              break;

                    case 'A': wild1.append('A'); wild2.append('A'); wild3.append('A'); wild4.append('A'); break;
                    case 'T': wild1.append('T'); wild2.append('T'); wild3.append('T'); wild4.append('T'); break;
                    case 'C': wild1.append('C'); wild2.append('C'); wild3.append('C'); wild4.append('C'); break;
                    case 'G': wild1.append('G'); wild2.append('G'); wild3.append('G'); wild4.append('G'); break;
                    default: System.out.println("Invalid character found in a query: " + query + ". Please fix and run again.");
                             closeOutputFile(false);
                             System.exit(0);
               }
          }

          if(numWildCards == 0) {
               queries.add(query);
               queries.add(reverseCompliment(query));
          } else if(numWildCards == 1) {
               queries.add(wild1.toString());
               queries.add(wild2.toString());
               queries.add(reverseCompliment(wild1.toString()));
               queries.add(reverseCompliment(wild2.toString()));
          } else if(numWildCards == 2) {
               queries.add(wild1.toString());
               queries.add(wild2.toString());
               queries.add(wild3.toString());
               queries.add(wild4.toString());
               queries.add(reverseCompliment(wild1.toString()));
               queries.add(reverseCompliment(wild2.toString()));
               queries.add(reverseCompliment(wild3.toString()));
               queries.add(reverseCompliment(wild4.toString()));
          } else {
               System.out.println("Too many degenerate nucleotides in query: " + query + ". Please fix and run again.");
               closeOutputFile(false);
               System.exit(0);
          }
     }

     public String reverseCompliment(String sequence) {
          StringBuilder retSequence = new StringBuilder();

          for(int i = sequence.length() - 1; i >= 0; i--) {
               // System.out.println("index: " + i + " - charAt: " + sequence.charAt(i));
               switch(sequence.charAt(i)) {
               case 'A': retSequence.append('T');
                         break;
               case 'T': retSequence.append('A');
                         break;
               case 'G': retSequence.append('C');
                         break;
               case 'C': retSequence.append('G');
               }
          }
          return retSequence.toString();
     }
}

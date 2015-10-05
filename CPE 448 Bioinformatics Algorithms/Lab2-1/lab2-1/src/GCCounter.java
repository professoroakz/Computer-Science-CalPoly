import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;
// import java.io.Writer;


/* TODO:
 * I think the GUI class has to be the mother class and the 
 * child has to be the model as in the GC-count file
 * I'm not sure if I would have time to take a look tonight. 
 * but I can look at whatever's there tomorrow
 * So we basically create a constructor for the child class and 
 * instansiate it and use the methods we need
 * GCCount.calcGC(inputfile) or something
 * And that's an action listener for "Calculate" button
 * GCCount.calcGC(windowsize, framesize, inputfile) or something
 *  */

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
		 
		public void readFile(String fileName){
			//System.out.println("Input the full file name for the test file. "
				//	+ "Example: test.FASTA or test.txt");
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

			System.out.println("window: " + windowSize + " : step: " + stepSize);

	
			for(int i = 0; i < seq.length(); i++) {
				if(seq.charAt(i) == 'G' || seq.charAt(i) == 'C' || 
						seq.charAt(i) == 'g' || seq.charAt(i) == 'c') {
					gcCount++;
				}
				if(seq.charAt(i) == 'n' || seq.charAt(i) == 'N') {
					nCount++;
				}
			}
			
			writer.println("# of N's present: " + nCount); // Output the total number of N's

			DecimalFormat df = new DecimalFormat("#.#");
			String formatted = df.format(((double)gcCount / (seq.length() - nCount) * 100));
			
			// System.out.println(formatted + "%");
			writer.println("%GC overall: " + formatted + "%"); // Output the overall GC %

			return true; // the basic GC succeeds
		}

		public void readRollingGCCount() {
			writer.println("Window Size: " + windowSize + ",Step Size: " + stepSize);  // Output the window and step sizes
			writer.println("Nucleotide Position,%GC");							// Output the column headers
			
			writer.flush();
			System.out.println("Congratulations! Your file is successfully downloaded to 'output.txt'");
			try {
				writer.close();
			}
			catch(Exception e) {
				// don't do anything. we just want to close this.
			}
		}
}

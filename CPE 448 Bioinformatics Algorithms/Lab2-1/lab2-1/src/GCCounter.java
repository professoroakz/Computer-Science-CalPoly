import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;


public class GCCounter {
	
	public static void main(String args[]) {
		File file;
		Scanner input = new Scanner(System.in);
		String seq = "";
		
		int gcCount = 0;
		int nCount = 0;
		int windowSize;
		int stepSize;
		
		System.out.println("Input the full file name for the test file. "
				+ "Example: test.FASTA or test.txt");
		file = new File(input.nextLine());
		
		System.out.println("Input the window size for this test.");
		windowSize = input.nextInt();
		
		System.out.println("Input the step size for this test.");
		stepSize = input.nextInt();
		
		try {
			Scanner scan = new Scanner(file);
			scan.nextLine();
			while(scan.hasNextLine()) {
				seq += scan.nextLine();				
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("File " + args[0] + " is missing.");
		}
		
		for(int i = 0; i < seq.length(); i++) {
			if(seq.charAt(i) == 'G' || seq.charAt(i) == 'C' || 
					seq.charAt(i) == 'g' || seq.charAt(i) == 'c') {
				gcCount++;
			}
			if(seq.charAt(i) == 'n' || seq.charAt(i) == 'N') {
				nCount++;
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.#");
		String formatted = df.format(((double)gcCount / (seq.length() - nCount) * 100));
		
		System.out.println(formatted + "%");
	}
}

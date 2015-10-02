import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;


public class GCCounter {
	
	public static void main(String args[]) {
		File file = new File(args[0]);
		String seq = "";
		int gcCount = 0;
		int nCount = 0;
		
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

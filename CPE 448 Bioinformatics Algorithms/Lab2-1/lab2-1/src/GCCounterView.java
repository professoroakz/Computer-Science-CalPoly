import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.io.File;

public final class GCCounterView {
  private File file;
     
  public static void main(String... aArgs){
    GCCounterView app = new GCCounterView();
    app.buildAndDisplayGui(); 
  }
  

  private void buildAndDisplayGui(){
    JFrame frame = new JFrame("~the gui~"); 
    buildContent(frame);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  private void buildContent(JFrame aFrame){
    final JPanel panel = new JPanel();
    panel.add(new JLabel("Calculate Gene Density!"));
    
    final JTextField selectedFastaFile = new JTextField("Selected FASTA file");
    panel.add(selectedFastaFile);
    
    JButton fastaFileButton = new JButton("Select FASTA file");
    fastaFileButton.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select FASTA file");
        //Handle open button action.
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser(); 
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                //This is where a real application would open the file.
                // System.out.println("File: " + file.getName() + ".");
                selectedFastaFile.setText(file.getName());
                // open file 
            } else {
                System.out.println("Open command cancelled by user.");
            }
            System.out.println(returnVal);
        }
    });
    panel.add(fastaFileButton);

    final JTextField selectedGTFFile = new JTextField("Selected GTF file");
    panel.add(selectedGTFFile);
   
    JButton gtfFileButton = new JButton("Select GTF file");
    gtfFileButton.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select GTF file");
        //Handle open button action.
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser(); 
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                //This is where a real application would open the file.
                // System.out.println("File: " + file.getName() + ".");
                selectedGTFFile.setText(file.getName());
                // open file 
                
                
            } else {
                System.out.println("Open command cancelled by user.");
            }
            System.out.println(returnVal);
        }
    });
    panel.add(gtfFileButton);
    
    JButton ok = new JButton("Calculate!");
    // ok.addActionListener(new ShowDialog(aFrame));
    panel.add(ok);
    
    aFrame.getContentPane().add(panel);
  }
  
  private static final class ShowDialog implements ActionListener {
    ShowDialog(JFrame aFrame){
      fFrame = aFrame;
    }
    
    @Override public void actionPerformed(ActionEvent aEvent) {
      JOptionPane.showMessageDialog(fFrame, "Success! The file is outputed as output.txt");
    }
    private JFrame fFrame;
  }
}
 

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
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;

public final class GuiDriver {

  private File fastaFile;
  private File gffFile;
  // private int windowSize;
  // private int scrollSize;
     
  public static void main(String... aArgs){
    GuiDriver app = new GuiDriver();
    app.buildAndDisplayGui(); 
  }
  

  private void buildAndDisplayGui(){
    JFrame frame = new JFrame("It's over 9000!"); 
    buildContent(frame);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  private void buildContent(JFrame aFrame){
    final JPanel panel = new JPanel();

    Box headerBox = Box.createHorizontalBox();

    headerBox.add(new JLabel("DNA Decoder"));

    Box fastaBox = Box.createHorizontalBox();
    
    fastaBox.add(new JLabel("FASTA file: "));

    final JTextField chosenFastaTextField = new JTextField("Selected FASTA file");
    chosenFastaTextField.setHorizontalAlignment(JTextField.CENTER);
    fastaBox.add(chosenFastaTextField);
    fastaBox.add(Box.createRigidArea(new Dimension(5,0)));
    
    JButton fastaBtnFile = new JButton("Select FASTA file");
    fastaBtnFile.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select file");
        //Handle open button action.
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser(); 
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fastaFile = fc.getSelectedFile();
                //This is where a real application would open the file.
                // System.out.println("File: " + file.getName() + ".");
                chosenFastaTextField.setText(fastaFile.getName());
                // open file 
                
                
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    });
    fastaBox.add(fastaBtnFile);

    Box gffBox = Box.createHorizontalBox();

    gffBox.add(new JLabel("GFF file: "));

    final JTextField chosenGffTextField = new JTextField("Selected GFF file");
    chosenGffTextField.setHorizontalAlignment(JTextField.CENTER);
    gffBox.add(chosenGffTextField);
    gffBox.add(Box.createRigidArea(new Dimension(5,0)));


    JButton gffBtnFile = new JButton("Select GFF file");
    gffBtnFile.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select file");
        //Handle open button action
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                gffFile = fc.getSelectedFile();
                chosenGffTextField.setText(gffFile.getName());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    });

    gffBox.add(gffBtnFile);

    Box outputFileBox = Box.createHorizontalBox();
    outputFileBox.add(new JLabel("Output file: "));


    final JTextField outputFileTextField = new JTextField("Enter desired output file name.");
    outputFileTextField.setHorizontalAlignment(JTextField.CENTER);
    outputFileTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            outputFileTextField.setText("");
        }
    });

    outputFileBox.add(outputFileTextField);


    Box calculate = Box.createHorizontalBox();
    
    JButton ok = new JButton("Calculate!");
    ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String outputText = outputFileTextField.getText();
            if(validateOutputFileName(outputText)) {
                DNADecoder decoder = new DNADecoder();

                // if a file opens incorrectly, don't do more
                if(decoder.readFiles(fastaFile, gffFile)) {
                    // do code stuff
                }
            }
            else {
                System.out.println("The output file " + outputText + " is invalid. It cannot contain spaces or \\ / : * ? \" < > |");
            }
        }
    });

    calculate.add(ok);

    Box allPieces = Box.createVerticalBox();
    allPieces.add(headerBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(fastaBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(gffBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(outputFileBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(calculate);
    panel.add(allPieces);
    
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

  private boolean validateOutputFileName(String outputFile) {
    boolean valid = true;

    if(outputFile.trim().length() > 0) {
        if(outputFile.indexOf(" ") != -1 || outputFile.indexOf("\\") != -1 || outputFile.indexOf("/") != -1 || outputFile.indexOf(":") != -1 ||
            outputFile.indexOf("*") != -1 || outputFile.indexOf("?") != -1 || outputFile.indexOf("\"") != -1 || outputFile.indexOf("<") != -1 ||
            outputFile.indexOf(">") != -1 || outputFile.indexOf("|") != -1) {
            valid = false;
        }
    }
    else {
        valid = false;
    }

    return valid;
  }
}
 

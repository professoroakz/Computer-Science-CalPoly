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

    // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    Box header = Box.createHorizontalBox();

    // panel.add(new JLabel("Calculate GC content!"));
    header.add(new JLabel("DNA Decoder"));

    // final JTextField windowSize = new JTextField("Insert window size");
    // windowSize.setHorizontalAlignment(JTextField.CENTER);
    // windowSize.addMouseListener(new MouseAdapter(){
    //     @Override
    //     public void mouseClicked(MouseEvent e){
    //         windowSize.setText("");
    //     }
    // });

    // final JTextField stepSize = new JTextField("Insert step size");
    // stepSize.setHorizontalAlignment(JTextField.CENTER);
    // stepSize.addMouseListener(new MouseAdapter(){
    //     @Override
    //     public void mouseClicked(MouseEvent e){
    //         stepSize.setText("");
    //     }
    // });
    
    // panel.add(windowSize);

    // // panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    // panel.add(stepSize);

    Box fastaBox = Box.createHorizontalBox();
    
    fastaBox.add(new JLabel("FASTA file: "));

    final JTextField chosenFastaTextField = new JTextField("Selected FASTA file");
    // panel.add(chosenFileTextField);
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
            // System.out.println(returnVal);
        }
    });
    // panel.add(btnFile);
    fastaBox.add(fastaBtnFile);

    Box gffBox = Box.createHorizontalBox();

    gffBox.add(new JLabel("GFF file: "));

    final JTextField chosenGffTextField = new JTextField("Selected GFF file");
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

    Box calculate = Box.createHorizontalBox();
    
    JButton ok = new JButton("Calculate!");
    ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // int window = -1;
            // int step = -1;
            // try {
            //     window = Integer.parseInt(windowSize.getText());
            //     step = Integer.parseInt(stepSize.getText());
            // }
            // catch(NumberFormatException ex) {
            //     System.out.println("Either WindowSize or StepSize (or both) is not a number.");
            // }

            // if(window != -1 && step != -1) {            
                // System.out.println("window: " + window + " : step: " + step);

                GCCounter counter = new GCCounter();
                counter.readFiles(fastaFile, gffFile);
                // if the readBasicCount function succeeds, then actually do more
                if(counter.readBasicGCCount()) {
                    counter.readRollingGCCount();
                }
            // }
        }
    });
    // ok.addActionListener(new ShowDialog(aFrame));
    // panel.add(ok);
    calculate.add(ok);

    Box allPieces = Box.createVerticalBox();
    allPieces.add(header);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(fastaBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(gffBox);
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
}
 

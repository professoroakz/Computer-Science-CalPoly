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
  private int windowSize;
  private int scrollSize;
	
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
    panel.add(new JLabel("Calculate GC content!"));
    
    final JTextField windowSize = new JTextField("Insert window size");
    windowSize.setHorizontalAlignment(JTextField.CENTER);
    windowSize.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            windowSize.setText("");
        }
    });

    final JTextField stepSize = new JTextField("Insert step size");
    stepSize.setHorizontalAlignment(JTextField.CENTER);
    stepSize.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            stepSize.setText("");
        }
    });
    
    panel.add(windowSize);
    panel.add(stepSize);
    
    final JTextField chosenFileTextField = new JTextField("Selected file");
    panel.add(chosenFileTextField);
   
    
    JButton btnFile = new JButton("Select FASTA file");
    btnFile.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select file");
        //Handle open button action.
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser(); 
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("File: " + file.getName() + ".");
                chosenFileTextField.setText(file.getName());
            } else {
                System.out.println("Open command cancelled by user.");
            }
            System.out.println(returnVal);
        }
    });
    panel.add(btnFile);
    
    JButton ok = new JButton("Calculate!");
    ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int window = -1;
            int step = -1;
            try {
                window = Integer.parseInt(windowSize.getText());
                step = Integer.parseInt(stepSize.getText());
            }
            catch(NumberFormatException ex) {
                System.out.println("Either WindowSize or StepSize (or both) is not a number.");
            }

            if(window != -1 && step != -1) {            
                System.out.println("window: " + window + " : step: " + step);

                GCCounter counter = new GCCounter(window, step);
                counter.readFile(file.getName());
                counter.readGCCount();
            }
        }
    });
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
 

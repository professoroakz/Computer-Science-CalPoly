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
import javax.swing.Box;
import java.awt.Dimension;

public final class CPGIslandsView {
  private File file;
  private int windowSize;
  private int scrollSize;

  public static void main(String... aArgs){
    CPGIslandsView app = new CPGIslandsView();
    app.buildAndDisplayGui();
  }


  private void buildAndDisplayGui(){
    JFrame frame = new JFrame("Calculate CPG Islands");
    buildContent(frame);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  private void buildContent(JFrame aFrame){
    final JPanel panel = new JPanel();

    Box headerBox = Box.createHorizontalBox();

    headerBox.add(new JLabel("Calculate CPG Islands!"));

    Box fileBox = Box.createHorizontalBox();
    fileBox.add(new JLabel("File: "));

    final JTextField chosenFileTextField = new JTextField("Selected file");
    chosenFileTextField.setHorizontalAlignment(JTextField.CENTER);
    fileBox.add(chosenFileTextField);
    fileBox.add(Box.createRigidArea(new Dimension(5,0)));

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
                // System.out.println("File: " + file.getName() + ".");
                chosenFileTextField.setText(file.getName());
                // open file


            } else {
                System.out.println("Open command cancelled by user.");
            }
            // System.out.println(returnVal);
        }
    });

    fileBox.add(btnFile);

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
            int window = 200;
            int step = 1;

            if(validateOutputFileName(outputText)) {
                CPGIslands counter = new CPGIslands(window, step, outputText + ".txt");
                if(counter.readFile(file)) {
                  counter.readRollingGCCount();
                }
            }
        }
    });
    // ok.addActionListener(new ShowDialog(aFrame));
    calculate.add(ok);

    Box allPieces = Box.createVerticalBox();
    allPieces.add(headerBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,3)));
    allPieces.add(fileBox);
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


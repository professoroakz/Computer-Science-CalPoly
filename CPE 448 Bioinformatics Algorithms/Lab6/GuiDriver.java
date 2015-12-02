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

  private File queryFile;
  private File databaseFile;

  public static void main(String... aArgs) {
    GuiDriver app = new GuiDriver();
    app.buildAndDisplayGui();
  }


  private void buildAndDisplayGui(){
    JFrame frame = new JFrame("DNA Alignment");
    buildContent(frame);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  private void buildContent(JFrame aFrame){
    final JPanel panel = new JPanel();

    Box headerBox = Box.createHorizontalBox();

    headerBox.add(new JLabel("DNA Alignment"));

    Box queryBox = Box.createHorizontalBox();

    queryBox.add(new JLabel("Query file: "));

    final JTextField chosenFastaTextField = new JTextField("Selected query FASTA file");
    chosenFastaTextField.setHorizontalAlignment(JTextField.CENTER);
    queryBox.add(chosenFastaTextField);
    queryBox.add(Box.createRigidArea(new Dimension(5,0)));

    JButton fastaBtnFile = new JButton("Select Query FASTA file");
    fastaBtnFile.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select file");
        final JFileChooser fc = new JFileChooser();
        //Handle open button action.
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                queryFile = fc.getSelectedFile();
                //This is where a real application would open the file.
                // System.out.println("File: " + file.getName() + ".");
                chosenFastaTextField.setText(queryFile.getName());
                // open file


            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    });
    queryBox.add(fastaBtnFile);

    Box subjectBox = Box.createHorizontalBox();

    subjectBox.add(new JLabel("BATCH file: "));

    final JTextField chosenGffTextField = new JTextField("Selected BATCH FASTA file");
    chosenGffTextField.setHorizontalAlignment(JTextField.CENTER);
    subjectBox.add(chosenGffTextField);
    subjectBox.add(Box.createRigidArea(new Dimension(5,0)));


    JButton batchBtnFile = new JButton("Select BATCH file");
    batchBtnFile.addActionListener(new ActionListener() {
        final JFrame frame = new JFrame("Select file");
        final JFileChooser fc = new JFileChooser();
        //Handle open button action
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                databaseFile = fc.getSelectedFile();
                chosenGffTextField.setText(databaseFile.getName());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    });

    subjectBox.add(batchBtnFile);

    Box gapPenaltyInputBox = Box.createHorizontalBox();
    gapPenaltyInputBox.add(new JLabel("Gap Penalty: "));

    final JTextField gapPenaltyTextField = new JTextField("Enter desired gap penalty.");
    gapPenaltyTextField.setHorizontalAlignment(JTextField.CENTER);
    gapPenaltyTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(((JTextField)e.getSource()).getText().equals("Enter desired gap penalty.")) {
                gapPenaltyTextField.setText("");
            }
        }
    });

    gapPenaltyInputBox.add(gapPenaltyTextField);

    Box outputFileBox = Box.createHorizontalBox();
    outputFileBox.add(new JLabel("Output file: "));


    final JTextField outputFileTextField = new JTextField("Enter desired output file name.");
    outputFileTextField.setHorizontalAlignment(JTextField.CENTER);
    outputFileTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(((JTextField)e.getSource()).getText().equals("Enter desired output file name.")) {
                outputFileTextField.setText("");
            }
        }
    });

    outputFileBox.add(outputFileTextField);


    Box calculate = Box.createHorizontalBox();

    JButton okPam = new JButton("Calculate PAM!");
    okPam.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String gapText = gapPenaltyTextField.getText();
            if(tryParseDouble(gapText)) {
                String outputText = outputFileTextField.getText();
                if(validateOutputFileName(outputText)) {
                    SequenceAlignment controller = new SequenceAlignment(Double.parseDouble(gapText), outputText + ".txt");
                    if(controller.readFiles(queryFile, databaseFile)) {
                        controller.run();
                    }
                    System.out.println("Gap penalty and output file were valid.");
                }
                else {
                    System.out.println("The output file " + outputText + " is invalid. It cannot contain spaces or \\ / : * ? \" < > | ");
                }
            }
            
        }
    });

    calculate.add(okPam);

    calculate.add(Box.createRigidArea(new Dimension(10,0)));

    JButton okBlosum = new JButton("Calculate BLOSUM!");
    okBlosum.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String gapText = gapPenaltyTextField.getText();
            if(tryParseDouble(gapText)) {
                String outputText = outputFileTextField.getText();
                if(validateOutputFileName(outputText)) {
                    // Controller controller = new Controller(Double.parseDouble(gapText), outputText + ".txt");
                    // if(controller.readFiles(queryFile, databaseFile)) {
                    //     controller.run();
                    // }
                    System.out.println("Gap penalty and output file were valid.");
                }
                else {
                    System.out.println("The output file " + outputText + " is invalid. It cannot contain spaces or \\ / : * ? \" < > | ");
                }
            }
        }
    });

    calculate.add(okBlosum);

    Box allPieces = Box.createVerticalBox();
    allPieces.add(headerBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,5)));
    allPieces.add(queryBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,5)));
    allPieces.add(subjectBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,5)));
    allPieces.add(gapPenaltyInputBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,5)));
    allPieces.add(outputFileBox);
    allPieces.add(Box.createRigidArea(new Dimension(0,5)));
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

  private boolean tryParseDouble(String input) {
    try {
        Double.parseDouble(input);
    } catch(Exception e) {
        System.out.println("The entered gap penalty is not a valid number.");
        return false;
    }
    return true;
  }
}

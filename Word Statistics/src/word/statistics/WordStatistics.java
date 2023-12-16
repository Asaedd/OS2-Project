package word.statistics;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class WordStatistics extends JFrame {

    private JTextField directoryTextField;
    private JCheckBox includeSubdirectoriesCheckBox;
    private JButton browseButton;
    private JButton startButton;
    private JTable resultTable;

    public WordStatistics() {
        initUI();
    }
    private void initUI() {
        try {
            UIManager.setLookAndFeel(
                    "javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        setTitle("Word Statistics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        directoryTextField = new JTextField();
        includeSubdirectoriesCheckBox = 
                new JCheckBox("Include Subdirectories");
        browseButton = new JButton("Browse");
        startButton = new JButton("Start");
        resultTable = new JTable();

        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        add(directoryTextField, BorderLayout.NORTH);
        add(includeSubdirectoriesCheckBox, BorderLayout.CENTER);
        add(browseButton, BorderLayout.WEST);
        add(startButton, BorderLayout.SOUTH);
        add(resultScrollPane, BorderLayout.EAST);

        browseButton.addActionListener((ActionEvent e) -> {
            browseForFile();
        });
        startButton.addActionListener((ActionEvent e) -> {
            startProcessing();
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode
        (JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            directoryTextField.setText(selectedFile.getAbsolutePath());
        }
    }
    private void startProcessing() {
        String directoryPath = directoryTextField.getText();
        boolean includeSubdirectories = includeSubdirectoriesCheckBox.isSelected();

        DefaultTableModel resultModel = new DefaultTableModel();

        resultTable.setModel(resultModel);
        
        resultModel.addColumn("File Name");
        resultModel.addColumn("#words");
        resultModel.addColumn("#is");
        resultModel.addColumn("#are");
        resultModel.addColumn("#you");
        resultModel.addColumn("Longest word");
        resultModel.addColumn("Shortest word");

        FileProcessor fileProcessor = new FileProcessor(directoryPath, includeSubdirectories, resultModel);
        new Thread(fileProcessor).start();        
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordStatistics::new);
    }
}
        





package word.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

class FileProcessor implements Runnable {
    // Fields...
    private final String directoryPath;
    private final boolean includeSubdirectories;
    private final DefaultTableModel resultTableModel;

    public FileProcessor(String directoryPath, boolean includeSubdirectories, DefaultTableModel resultTableModel) {
        this.directoryPath = directoryPath;
        this.includeSubdirectories = includeSubdirectories;
        this.resultTableModel = resultTableModel;
    }

    @Override
    public void run() {
        File directory = new File(directoryPath);
        processFiles(directory);
    }

    private void processFiles(File directory) {  
        // Check if the file is a directory
        if (directory.isDirectory()) {
            // Get list of files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Process each file using a separate thread
                        new Thread(() -> processFile(file)).start();
                    } else if (includeSubdirectories && file.isDirectory()) {
                        // If includeSubdirectories is true, recursively process subdirectories
                        processFiles(file);
                    }
                }
            }
        }
    }
    private void processFile(File file) {
        long threadId = Thread.currentThread().getId();
        System.out.println("Processing file " + file.getName() + " on thread " + threadId);

        int wordCount = 0;
        int isCount = 0;
        int areCount = 0;
        int youCount = 0;

        try (LineNumberReader lineReader = new LineNumberReader(new FileReader(file))){
            String line;
            String longestWord = "";
            String shortestWord = "sssssssssssssssssssssssssssssssssssssssssssssssssss";

            while ((line = lineReader.readLine()) != null){
                String[] words = line.split("\\s+");
                for (String word : words) {
                    // Word statistics logic...
                    String lowerCaseWord = word.toLowerCase();
                    int size = lowerCaseWord.length();
                    int size1 = longestWord.length();
                    int size2 = shortestWord.length();
                    int size3 = all.getLongest().length();
                    int size4 = all.getShortest().length();
                    if (lowerCaseWord.equals("is")) {
                        isCount++;
                    }
                    if (lowerCaseWord.equals("are")) {
                        areCount++;
                    }
                    if (lowerCaseWord.equals("you")) {
                        youCount++;
                    }
                    if (size > size1) {
                        longestWord = lowerCaseWord;
                    }
                    if (size <= size2 && size >= 4) {
                        shortestWord = lowerCaseWord;
                    }
                    if (size > size3) {
                        all.setLongest(lowerCaseWord);
                    }
                    if (size <= size4 && size >= 4) {
                        all.setShortest(lowerCaseWord);
                    }
                    // Increment word count...
                    wordCount++;
                }
                    updateGui(file.getName(), wordCount, isCount, areCount,
                                      youCount, longestWord, shortestWord);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("ERROR");
                    }
            }
            // Update the GUI with the final result after processing the entire file
            if(all.isAhmed())
            {
            Object[] globalRowData = {"Longest", all.getLongest()};
            SwingUtilities.invokeLater(() -> resultTableModel.addRow(globalRowData));

            Object[] globalRowData2 = {"Shortest", all.getShortest()};
            SwingUtilities.invokeLater(() -> resultTableModel.addRow(globalRowData2));
            all.setAhmed(false);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    private void updateGui(String fileName, int wordCount, int isCount, int areCount, int youCount,
                           String longestWord, String shortestWord) {
        boolean found = false;
        // Iterate through existing rows to find and update the row with the same file name
        for (int i = 0; i < resultTableModel.getRowCount(); i++) {
            if (resultTableModel.getValueAt(i, 0).equals(fileName)) {
                // Update the existing row
                resultTableModel.setValueAt(wordCount, i, 1);
                resultTableModel.setValueAt(isCount, i, 2);
                resultTableModel.setValueAt(areCount, i, 3);
                resultTableModel.setValueAt(youCount, i, 4);
                resultTableModel.setValueAt(longestWord, i, 5);
                resultTableModel.setValueAt(shortestWord, i, 6);
                found = true;
                break;
            }
        }
        // If the row with the given file name is not found, add a new row
        if (!found) {
            Object[] rowData = {fileName, wordCount, isCount, areCount, youCount, longestWord, shortestWord};
            SwingUtilities.invokeLater(() -> resultTableModel.addRow(rowData));
        }
        
        // Iterate through existing rows to find and update the global statistics rows
        for (int i = 0; i < resultTableModel.getRowCount(); i++) {
            if (resultTableModel.getValueAt(i, 0).equals("Longest")) {
                // Update the existing row for Longest
                resultTableModel.setValueAt(all.getLongest(), i, 1);
            } else if (resultTableModel.getValueAt(i, 0).equals("Shortest")) {
                // Update the existing row for Shortest
                resultTableModel.setValueAt(all.getShortest(), i, 1);
            }
        }    
    }
}

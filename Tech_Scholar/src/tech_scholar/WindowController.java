/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech_scholar;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.*;
import java.util.*;
/**
 * FXML Controller class
 *
 * @author tenzintashi
 */
public class WindowController implements Initializable {
    @FXML
    private List <String> extList;
    @FXML
    private Set <String> filterData;
    @FXML
    private Button btnSelectFile;
    @FXML
    private Label lblFilePath;
    @FXML
    private Button btnConvert;
    @FXML
    private Button btnErase;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblCompleted;
    @FXML
    private String filePath;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        extList = new ArrayList<>();
        extList.add("*.txt");
        extList.add("*.csv");
        filterData = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        filterData.add("java");
        filterData.add("python");
        filterData.add("javascript");
        filterData.add("C");
        filterData.add("C#");
        filterData.add("C++");
        filterData.add("CPP");
        filterData.add("Ruby");
        filterData.add("Swift");
        filterData.add("HTML");
        filterData.add("CSS");
        filterData.add("PHP");
        
    }  
    public void btnClear(){
        lblFilePath.setText("");
        lblCompleted.setText("");
    }
    public void btnExit(){
        System.exit(1);
    }
    
    public void btnSelectFile(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("CSV File",extList));
        File file = fc.showOpenDialog(null);
        filePath = file.getAbsolutePath();
        if (file != null){
            lblFilePath.setText("File : " + file.getName());
        }
    }
    
    
    
    public void btnConvert() throws FileNotFoundException, IOException{
//        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> mapTree = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        
        
        String row, space=" ";
        
        //List<String> word = new ArrayList<String>();
        
        ArrayList<String> word = new ArrayList<String>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        
        
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            
            for (int i = 0; i < data.length; i++) {
                
                // remove any punction from the word
                data[i] = data[i].replaceAll("\\p{Punct}",""); 
                
                //split the data by space " "
                String[] temp = data[i].split(" ");
                word.addAll(Arrays.asList(temp));
                word.removeAll(Arrays.asList(space));
            }

        }
        //close the file
        csvReader.close();
        
        for (int i = 0; i < word.size(); i++) {
            // checking each word of data array 
            
            if (mapTree.containsKey(word.get(i))) {

                // If word is present in map, 
                // incrementing it's count by 1 
                mapTree.put(word.get(i), mapTree.get(word.get(i)) + 1);
            } else {

                // If word is not present in map, 
                // putting this word to map with 1 as it's value 
                mapTree.put(word.get(i), 1);
            }
        }
        
        
        
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(mapTree.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> sorted = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            sorted.put(aa.getKey(), aa.getValue()); 
        } 
//        // Printing the map 
//        for (Map.Entry entry : sorted.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }



        // Write the result to CSV file
        
        String userHomeFolder = System.getProperty("user.home");
        String filename = (userHomeFolder + "/Desktop/"+"Result.csv");

        PrintWriter csvWriter = new PrintWriter(filename);
        csvWriter.append("Key Word");
        csvWriter.append(",");
        csvWriter.append("occurrence");
        csvWriter.append("\n");
        
        
        for (Map.Entry entry : sorted.entrySet()) {
            //filter the data
            if (filterData.contains(entry.getKey())) {
                csvWriter.print(entry.getKey());
                csvWriter.print(",");
                csvWriter.print(entry.getValue());
                csvWriter.print("\n");
            }
            
                              
        }
        csvWriter.flush();
        csvWriter.close();
        lblCompleted.setText("Completed !!! saved to desktop");
    }
    
}

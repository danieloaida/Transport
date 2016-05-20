package ro.ratt.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by baby on 4/25/2016.
 */
public class CSVReader {
    InputStream inputStream;

    public CSVReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<Junction> read(){
        List<Junction> resultList = new ArrayList<Junction>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        try{
            String csvLine;
            String[] row = new String[20];
            csvLine = reader.readLine();
            for (int i = 1;i<=1324;i++){                    )
                row = csvLine.split(",");
                    Junction newItem = new Junction(row);
                    resultList.add(newItem);
                csvLine = reader.readLine();
            }
        } catch(IOException ex){
            throw new RuntimeException("Error in reading CSV file" + ex);
        } finally {
            try {
                inputStream.close();
            }catch (IOException e){
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
}

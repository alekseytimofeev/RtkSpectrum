package detectionModules;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Bdmg implements Bd {

    private byte logicNumber;

    private int serialNubmer;

    public Bdmg(byte logicNumber, int serialNubmer) {
        this.logicNumber = logicNumber;
        this.serialNubmer = serialNubmer;
    }

    public byte getLogicNumber() {
        return logicNumber;
    }

    List<List<BlockDetectionController.MeasureData> > measures = new ArrayList<>();

   public void newMeasure() {
       measures.add( new ArrayList<>());
   }

   public void add(BlockDetectionController.MeasureData measureData) {
       measures.get(measures.size()-1).add(measureData);
   }

   public void importMeasure() throws IOException {
       System.out.println("importMeasure");

       Gson gson = new Gson();
       String json = gson.toJson(measures);
       Writer writer = new FileWriter("E://1.txt");
       writer.write(json);
       writer.close();
   }

}

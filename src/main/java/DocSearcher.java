import jdk.nashorn.internal.runtime.JSONListAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class DocSearcher {

    private final File invertedArrayFile;
    private final File lemmasFile;
    private final List<JSONObject> resultList;

    public DocSearcher() {
        invertedArrayFile = new File("src/main/resources/tf_idf.txt");
        lemmasFile = new File("src/main/resources/lemmas.txt");
        resultList = new ArrayList<>();
    }

    void findLemmaInFile(String[] word) {
        for (String s : word) {
            try (Scanner sc = new Scanner(lemmasFile)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.contains(s)) {
                        findWordInInvertedArrayFile(line.split(" ")[0]);
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        sortAndPrintResult();
    }

    private void sortAndPrintResult() {

//        List<JSONObject> result =  new ArrayList<>();
        resultList.stream().forEach(jsonObject -> {
            JSONArray jsonArray = jsonObject.getJSONArray("tf_idf");
            List<JSONObject> result = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                result.add(jsonObject1);
            }
            result.sort((json, another) -> Double.compare(another.getDouble("value"), json.getDouble("value")));
            jsonObject.remove("tf_idf");
            jsonObject.put("tf_idf", result);
        });
        resultList.stream()
                .sorted((json, other) -> Double.compare(other.getDouble("idf"), json.getDouble("idf")))
                .forEach(jsonObject ->
                        System.out.println(jsonObject.getString("word") + " " + jsonObject.getJSONArray("tf_idf")));
    }

    void findWordInInvertedArrayFile(String word) {
        try (Scanner sc = new Scanner(invertedArrayFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(word)) {
                    resultList.add(new JSONObject(line));
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

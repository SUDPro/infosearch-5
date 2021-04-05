import org.json.JSONObject;

class Parser {

    void parse(String nextLine) {
        String[] words = nextLine.split(" ");
        DocSearcher docSearcher = new DocSearcher();
        docSearcher.findLemmaInFile(words);

    }

    static String parseJSON(JSONObject jsonObject){

        return null;
    }

}

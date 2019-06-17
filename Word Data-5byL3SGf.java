import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
 
/**
 * Created by Ricardo on 12/17/2016.
 */
public class WordData {
 
    private TreeMap<String, Set<Integer>> treeMap;
    private Set<String> commonWords;
    private Set<Integer> lineNumberSet;
 
    public WordData() {
        this.treeMap = new TreeMap<>();
    }
 
    public void loadListFromURL() throws Exception {
 
        commonWords = new HashSet<>();
 
        URL url = new URL("http://cs.stcc.edu/~phillips/CommonWords.txt");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
 
        String inputLine;
 
        while ((inputLine = in.readLine()) != null) {
            String[] words = inputLine.split(" ");
 
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                commonWords.add(word);
            }
        }
        // add noise words
        commonWords.add("the");
        in.close();
    }
    
 
    public void loadMapFromURL() throws Exception {
 
        URL url = new URL("http://cs.stcc.edu/~phillips/warandpeace.txt");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
 
        String inputLine;
 
        int lineNumber = 0;
 
        while ((inputLine = in.readLine()) != null) {
 
            String[] words = removeJunk(inputLine.split(" "));
            lineNumber++;
 
            for (int i = 0; i < words.length; i++) {
 
                String word = words[i];
 
                if (commonWords.contains(word))
                    continue;
                
                if (treeMap.get(word) == null) {
                    lineNumberSet = new HashSet<>();
                    lineNumberSet.add(lineNumber);
                    treeMap.put(word, lineNumberSet);
                } else {
                    lineNumberSet = treeMap.get(word);
                    lineNumberSet.add(lineNumber);
                }
            }
            System.out.print("\n");
        }
        in.close();
    }
 
    public static void getWordData() throws Exception {
        WordData wordData = new WordData();
        wordData.loadListFromURL();
        wordData.loadMapFromURL();
        wordData.displayResults();
    }
    
    private String[] removeJunk(String[] line) {
		
		for (int i = 0; i < line.length; i++) {
			line[i] = line[i].replaceAll("[^a-zA-Z]", "");
			line[i] = line[i].toLowerCase();
		}
		return line;
}
 
    public void displayResults() {
        Set set = treeMap.entrySet();
        // Get an iterator
        Iterator it = set.iterator();
 
        // Display elements
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            System.out.println("\nKey is: " + me.getKey() + " & Value is: " + me.getValue());
        }
    }
 
    public static void main(String[] args) throws Exception {
        getWordData();
    }
}
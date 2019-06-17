import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ricardo on 12/1/2016.
 */
public class Dictionary {

        String content = new String();

        File file = new File("src/words2.txt");
       // LinkedList<String> list = new LinkedList<>();
        MyLinkedList list = new MyLinkedList();


    public void loadDictionary(){

        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            while (sc.hasNextLine()) {
                content = sc.nextLine();
                list.add(content);
            }
            sc.close();
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean lookupWord(String word) {
        if (list.contains(word))
            return true;
        return false;
    }
}
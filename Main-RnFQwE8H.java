/**
 * Created by Ricardo on 12/2/2016.
 */
import java.io.File;
import java.util.Scanner;

public class Main {


    //**********************************************

    private static void process(Scanner sc, String args[]) {
        // Code here is merely a sample
        int x;
        System.out.print("Enter value: ");
        x = sc.nextInt();
        sc.nextLine();  // IMPORTANT!! Reset Scanner
        System.out.println("Processing " + x + " ...");
    }

    //**********************************************

    private static boolean doThisAgain(Scanner sc, String prompt) {
        System.out.print(prompt);
        String doOver = sc.nextLine();
        return doOver.equalsIgnoreCase("Y");
    }

    //**********************************************

    final String TITLE = "CSC111 Project Template";
    final String CONTINUE_PROMPT = "Do this again? [y/N] ";

    static public void main(String args[]) throws Exception {
        ScannerWithLineno swl = new ScannerWithLineno(new File("alice30.txt"));
        Dictionary dictionary = new Dictionary();
        dictionary.loadDictionary();
        while (swl.hasNext()) {
            String word = swl.next();
            if (dictionary.lookupWord(word) == true)
                break;
            System.out.println(swl.getLineno() + ": " + swl.getCurrentLine() + " " + word);
            if (swl.getLineno() >= 20)
                break;
        }
        swl.close();
    }
}
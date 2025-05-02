package main.java.user;

import main.java.util.Ansi;
import main.java.util.ConsoleMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputGUI
{
    public static int getLaunchCode()
    {
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        
        while(true) {
            ConsoleMessage.dv();
            ConsoleMessage.info("Actions available:");

            ConsoleMessage.choice("1", "Currency Mapper");

            ConsoleMessage.input("Which script would you like to run?");

            String selectionInput = "0";

            try{
                selectionInput = userInputReader.readLine();
            }
            catch(IOException iox) {
                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }


            switch(selectionInput) {
                case "1":
                    return 1;
                default:
                    ConsoleMessage.warning("That is an invalid choice!");
            }
        }
    }
    
    public static String getFileName(String instruction)
    {
        // Detect files in the sources folder.
        File[] files = new File("sources\\").listFiles(file -> file.isFile());
        ConsoleMessage.info("Source files detected:");
        for(int i = 0; i < files.length; i++) {
            ConsoleMessage.choice(String.valueOf((i + 1)), files[i].getName());
        }

        // Prompt user selection.
        ConsoleMessage.input("Specify " + Ansi.BOLD + Ansi.DARK_GREEN + instruction + Ansi.RESET + " source file!");

        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        String userSource              = "";
        try { userSource = "sources\\" + files[Integer.parseInt(userInputReader.readLine())-1].getName(); }
        catch(IOException iox) { ConsoleMessage.error(iox, "FATAL - Unable to read user input."); }
        
        ConsoleMessage.br();
        
        return userSource;
    }
}

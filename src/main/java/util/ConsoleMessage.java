package main.java.util;

public class ConsoleMessage
{
    public static void error(Exception x, String message) 
    {
        System.out.println(Ansi.DARK_YELLOW + Ansi.BACKGROUND_RED + "ERROR: " + message + Ansi.RESET);
        throw new RuntimeException(x);
    }
    
    public static void warning(String message) 
    {
        System.out.println(Ansi.DARK_RED + Ansi.BACKGROUND_YELLOW + "WARNING: " + message + Ansi.RESET);
    }
    
    public static void info(String message) 
    {
        System.out.println(Ansi.CYAN + "INFO: " + message + Ansi.RESET);
    }
    
    public static void input(String message) 
    {
        System.out.println(Ansi.YELLOW + "INPUT REQUIRED: " + message + Ansi.RESET);
        System.out.print(Ansi.YELLOW + "INPUT > " + Ansi.RESET);
    }
    
    public static void choice(String choice, String value)
    {
        System.out.println(Ansi.PURPLE + "[" + Ansi.BOLD + Ansi.GREEN +  choice + Ansi.RESET + Ansi.PURPLE + "]" + Ansi.RESET + Ansi.BLUE + " " + value + Ansi.RESET);
    }
    
    public static void dv()
    {
        System.out.println(Ansi.BOLD + Ansi.DARK_RED + Ansi.BACKGROUND_GREEN + "---------- ---------- ---------- ---------- ---------- ----------" + Ansi.RESET);
    }
    
    public static void br() 
    {
        System.out.println();
    }
}

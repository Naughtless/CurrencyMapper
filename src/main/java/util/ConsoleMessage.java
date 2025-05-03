package main.java.util;

import main.java.runners.ConsoleApp;

public class ConsoleMessage
{
    public static void error(Exception x, String message) 
    {
        System.out.println(Ansi.DARK_RED + "ERROR: " + message + Ansi.RESET);
        throw new RuntimeException(x);
    }

    public static void error(String message)
    {
        System.out.println(Ansi.DARK_RED + "ERROR: " + message + Ansi.RESET);
    }
    
    public static void warning(String message) 
    {
        System.out.println(Ansi.YELLOW + "WARNING: " + message + Ansi.RESET);
    }
    
    public static void info(String message) 
    {
        System.out.println(Ansi.BLUE + "INFO: " + message + Ansi.RESET);
    }
    
    public static void input(String message) 
    {
        System.out.println(Ansi.BOLD + "INPUT REQUIRED: " + message + Ansi.RESET);
        System.out.print(Ansi.BOLD + "INPUT > " + Ansi.RESET);
    }
    
    public static void choice(String choice, String value)
    {
        System.out.println(Ansi.BLACK + Ansi.BOLD + "[" + Ansi.BOLD + Ansi.DARK_YELLOW +  choice + Ansi.RESET + Ansi.BLACK + Ansi.BOLD + "]" + Ansi.RESET + Ansi.DARK_GREEN + " " + value + Ansi.RESET);
    }
    
    public static void debug(String message)
    {
        if(ConsoleApp.DEBUG_MODE)
        {
            System.out.println(Ansi.SMOKE + "DEBUG: " + message + Ansi.RESET);
        }
    }
    
    public static void dv()
    {
        br();
        System.out.println(Ansi.BOLD + Ansi.BLACK + Ansi.BACKGROUND_GREEN + "========== ---------- ========== ---------- ========== ---------- ==========" + Ansi.RESET);
        br();
    }
    
    public static void br() 
    {
        System.out.println();
    }
}

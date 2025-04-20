package main.console;

public class M {
    public static void error(Exception x, String message) {
        System.out.println(Ansi.DARK_YELLOW + Ansi.BACKGROUND_RED + "ERROR: " + message + Ansi.RESET);
        throw new RuntimeException(x);
    }
    public static void warning(String message) {
        System.out.println(Ansi.DARK_RED + Ansi.BACKGROUND_YELLOW + "WARNING: " + message + Ansi.RESET);
    }
    public static void info(String message) {
        System.out.println(Ansi.CYAN + "INFO: " + message + Ansi.RESET);
    }
    public static void input(String message) {
        System.out.println(Ansi.YELLOW + "INPUT REQUIRED: " + message + Ansi.RESET);
        System.out.print(Ansi.YELLOW + "INPUT > " + Ansi.RESET);
    }
    public static void br() {
        System.out.println();
    }
}

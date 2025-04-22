package sandbox;

import main.java.util.Ansi;

public class Experiments
{
    public static void main(String[] args)
    {
        System.out.println(Ansi.PURPLE + "[" + Ansi.BOLD + Ansi.GREEN +  "1" + Ansi.RESET + Ansi.PURPLE + "]" + Ansi.RESET + Ansi.BLUE + " Start");
        Ansi.sample();
    }
}

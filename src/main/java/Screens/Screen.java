package Screens;

import java.util.List;
import java.util.Scanner;


abstract public class Screen <T> {

    private final String title;
    public Scanner scanner;

    abstract public T openScreen();

    Screen(Scanner scanner, String title){
        this.title = title;
        this.scanner = scanner;
    }

    public String getTitle(){
        return title;
    }
}

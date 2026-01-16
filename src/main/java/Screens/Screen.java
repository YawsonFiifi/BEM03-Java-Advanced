package Screens;

import java.util.Scanner;


abstract public class Screen {

    private String title;
    public Scanner scanner;

    Screen(Scanner scanner, String title){
        this.title = title;
        this.scanner = scanner;
    }

    public String getTitle(){
        return title;
    }
}

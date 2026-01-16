package Screens;

import java.util.Scanner;

public class Prompt extends Screen {

    private String prompt;

    public Prompt(Scanner scanner, String title, String prompt){
        super(scanner, title);

        this.prompt = prompt;
    }

    public String openScreen(){
        if(getTitle() != null)
            System.out.println(getTitle()+"\n");

        System.out.print(prompt + ": ");

        String promptResponse = scanner.nextLine();

        System.out.println();

        return promptResponse;
    }
}

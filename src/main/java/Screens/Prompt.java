package Screens;

import java.util.Scanner;

public class Prompt extends Screen<String> {

    private final String prompt;
    private String regex;

    public Prompt(Scanner scanner, String title, String prompt, String regex){
        super(scanner, title);

        this.prompt = prompt;
        this.regex = regex;
    }

    public Prompt(Scanner scanner, String title, String prompt){
        super(scanner, title);

        this.prompt = prompt;
    }

    public String openScreen(){
        while(true){
            if(getTitle() != null)
                System.out.println(getTitle()+"\n");

            System.out.print(prompt + ": ");

            String promptResponse = scanner.nextLine();

            System.out.println();

            if(promptResponse.matches(regex)) return promptResponse;

            System.out.println("Invalid Input, try again\n");
        }
    }
}

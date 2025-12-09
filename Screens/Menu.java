package Screens;

import java.util.Scanner;


public class Menu extends Screen{
    private String[] options;

    public Menu(Scanner scanner, String title, String[] options){
        super(scanner, title);
        this.options = options;
    }

    public int openScreen(){
        StringBuilder output = new StringBuilder(String.format("\n%s\n\n", getTitle()));

        for(int i=1; i<=options.length; i++){
            output.append(String.format("%d. %s\n", i, options[i - 1]));
        }

        System.out.print(output + "\n" + "Enter Choice: ");

        int menuResponse = scanner.nextInt();
        scanner.nextLine();

        return menuResponse;
    }
}

package Screens;

import java.util.InputMismatchException;
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

        while(true){
            System.out.print(output + "\n" + "Enter Choice: ");

            int menuResponse;

            try{
                menuResponse = scanner.nextInt();
                scanner.nextLine();
            }catch(InputMismatchException ie){
                System.out.println("Input a number in the options disaplayed, try again");
                continue;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Invalid input, try again");
                continue;
            }

            if(menuResponse > options.length || menuResponse < 0){
                System.out.println("Invalid number option, Try again" + menuResponse);

                continue;
            }

            return menuResponse;
        }
    }
}

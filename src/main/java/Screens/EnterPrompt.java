package Screens;

import java.util.Scanner;

public class EnterPrompt extends Screen<Void> {

    public EnterPrompt(Scanner scanner) {
        super(scanner, "Press Enter to continue...");
    }

    @Override
    public Void openScreen() {
        return null;
    }
}

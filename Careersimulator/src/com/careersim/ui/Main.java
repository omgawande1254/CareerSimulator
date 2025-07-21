// ---------------------
// File: Main.java
// Package: com.careersim.ui
// ---------------------
package com.careersim.ui;

import com.careersim.engine.GameEngine;
import com.careersim.model.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameEngine engine = new GameEngine();

        System.out.print("Do you want to (L)ogin or (R)egister? ");
        String action = scanner.nextLine().trim().toUpperCase();

        if (action.equals("R")) {
            System.out.print("Enter new username: ");
            String newUser = scanner.nextLine().trim();
            System.out.print("Enter new password: ");
            String newPass = scanner.nextLine().trim();

            boolean success = engine.register(newUser, newPass);
            if (success) {
                System.out.println("✅ Registration successful. Please log in.");
            } else {
                System.out.println("❌ Username already taken.");
                scanner.close();
                return;
            }
        }

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = engine.login(username, password);
        if (user == null) {
            System.out.println("❌ Login failed. Exiting game.");
            scanner.close();
            return;
        }

        PlayerStats stats = engine.loadPlayerStats(user.getUserId());
        if (stats == null) {
            stats = new PlayerStats(user.getUserId(), 100, 0, 100, 0, 0, "Student");
            engine.savePlayerStats(stats);
        }

        while (true) {
            GameChoice choice = engine.getChoice(user.getCurrentStage());
            if (choice == null) {
                System.out.println("\n🎉 Game Over or No More Stages!");
                break;
            }

            System.out.println("\n🗺️ Stage " + choice.stage);
            System.out.println(choice.description);
            System.out.println("A. " + choice.optionA);
            System.out.println("B. " + choice.optionB);
            System.out.println("C. " + choice.optionC);

            System.out.print("Choose (A/B/C): ");
            String input = scanner.nextLine().trim().toUpperCase();
            engine.makeChoice(user, stats, choice.choiceId, input);

            user.setCurrentStage(user.getCurrentStage() + 1);
            stats.displayStats();
        }

        scanner.close();
    }
}

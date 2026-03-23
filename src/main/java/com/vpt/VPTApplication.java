package com.vpt;
import java.util.Scanner;

/**
 * VPTApplication controls the command-line interface of the program.
 *
 * This class:
 * - shows the menu
 * - gets user input
 * - calls MatchManager methods
 * - validates user input before creating objects
 * - uses input loops so the user can retry invalid entries
 */
public class VPTApplication {
    private MatchManager manager;
    private Scanner scanner;
    private boolean running;

    /**
     * Constructor creates the manager, scanner, and program state.
     */
    public VPTApplication() {
        manager = new MatchManager();
        scanner = new Scanner(System.in);
        running = true;
    }

    /**
     * Runs the main loop until the user chooses to exit.
     *
     * @return final exit message
     */
    public String run() {
        while (running) {
            System.out.println(showMenu());
            String choice = scanner.nextLine().trim();
            System.out.println(handleMenuChoice(choice));
        }
        return "Program ended.";
    }

    /**
     * Returns the CLI menu as a formatted string.
     *
     * @return menu text
     */
    public String showMenu() {
        return """

                ===== Valorant Match Performance Tracker =====
                1. Load match data from file
                2. Display all match data
                3. Add new match
                4. Remove match by ID
                5. Update match by ID
                6. Generate improvement summary
                7. Exit
                Enter your choice:
                """;
    }

    /**
     * Handles user menu selection.
     *
     * @param choice menu option
     * @return result message
     */
    public String handleMenuChoice(String choice) {
        switch (choice) {
            case "1":
                return loadDataFlow();
            case "2":
                return manager.displayAllMatches();
            case "3":
                return addMatchFlow();
            case "4":
                return removeMatchFlow();
            case "5":
                return updateMatchFlow();
            case "6":
                return manager.generateImprovementSummary();
            case "7":
                running = false;
                return "Exiting program...";
            default:
                return "Invalid menu option. Please enter 1 through 7.";
        }
    }

    /**
     * Handles the flow for loading text file data.
     *
     * @return result message
     */
    public String loadDataFlow() {
        System.out.print("Enter the full path to the text file: ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            return "File path cannot be empty.";
        }

        return manager.loadFromFile(filePath);
    }

    /**
     * Improved add flow:
     * 1. Checks unique Match ID first
     * 2. Uses loops for each field so invalid input does not kick user back to menu
     *
     * @return result message
     */
    public String addMatchFlow() {
        int matchId = promptForUniqueMatchId();
        String agentName = promptForNonEmptyString("Enter Agent Name: ", "Agent name cannot be empty.");
        String mapName = promptForNonEmptyString("Enter Map Name: ", "Map name cannot be empty.");
        int kills = promptForNonNegativeInt("Enter Kills: ", "Kills must be a whole number greater than or equal to 0.");
        int deaths = promptForNonNegativeInt("Enter Deaths: ", "Deaths must be a whole number greater than or equal to 0.");
        int assists = promptForNonNegativeInt("Enter Assists: ", "Assists must be a whole number greater than or equal to 0.");
        double hs = promptForPercentage("Enter Headshot Percentage (0-100): ");
        boolean win = promptForBoolean("Enter Win (true/false): ");
        String matchDate = promptForNonEmptyString("Enter Match Date (YYYY-MM-DD): ", "Match date cannot be empty.");
        String notes = promptForAnyString("Enter Notes: ");

        MatchRecord match = new MatchRecord(
                matchId, agentName, mapName, kills, deaths, assists, hs, win, matchDate, notes
        );

        return manager.addMatch(match);
    }

    /**
     * Handles match removal flow.
     *
     * @return result message
     */
    public String removeMatchFlow() {
        int matchId = promptForInt("Enter Match ID to remove: ", "Match ID must be an integer.");
        return manager.removeMatch(matchId);
    }

    /**
     * Handles record update flow.
     * Keeps your existing approach of typing the field name.
     *
     * @return result message
     */
    public String updateMatchFlow() {
        int matchId = promptForInt("Enter Match ID to update: ", "Match ID must be an integer.");

        System.out.print("Enter field to update (agentName, mapName, kills, deaths, assists, headshotPercentage, win, matchDate, notes): ");
        String fieldName = scanner.nextLine().trim();

        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine().trim();

        return manager.updateMatch(matchId, fieldName, newValue);
    }

    /**
     * Prompts for a match ID and checks uniqueness immediately.
     *
     * @return unique match ID
     */
    public int promptForUniqueMatchId() {
        while (true) {
            int matchId = promptForInt("Enter Match ID: ", "Match ID must be an integer.");

            if (manager.findMatchById(matchId) != null) {
                System.out.println("A match with that ID already exists. Please enter a different Match ID.");
            } else {
                return matchId;
            }
        }
    }

    /**
     * Prompts until the user enters a valid integer.
     *
     * @param prompt prompt text
     * @param errorMessage error message for invalid input
     * @return valid integer
     */
    public int promptForInt(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    /**
     * Prompts until the user enters a non-negative integer.
     *
     * @param prompt prompt text
     * @param errorMessage error message for invalid input
     * @return valid non-negative integer
     */
    public int promptForNonNegativeInt(String prompt, String errorMessage) {
        while (true) {
            int value = promptForInt(prompt, errorMessage);
            if (value >= 0) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    /**
     * Prompts until the user enters a valid decimal percentage between 0 and 100.
     *
     * @param prompt prompt text
     * @return valid percentage
     */
    public double promptForPercentage(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (value >= 0 && value <= 100) {
                    return value;
                }
                System.out.println("Headshot percentage must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Headshot percentage must be a valid number between 0 and 100.");
            }
        }
    }

    /**
     * Prompts until the user enters true or false.
     *
     * @param prompt prompt text
     * @return valid boolean
     */
    public boolean promptForBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("true")) {
                return true;
            }
            if (input.equalsIgnoreCase("false")) {
                return false;
            }

            System.out.println("Please enter true or false.");
        }
    }

    /**
     * Prompts until the user enters a non-empty string.
     *
     * @param prompt prompt text
     * @param errorMessage error message if blank
     * @return valid non-empty string
     */
    public String promptForNonEmptyString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(errorMessage);
        }
    }

    /**
     * Prompts once and accepts any string, including blank if desired.
     *
     * @param prompt prompt text
     * @return entered string
     */
    public String promptForAnyString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
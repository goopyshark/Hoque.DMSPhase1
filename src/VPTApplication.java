import java.util.Scanner;

/**
 * VPTApplication controls the command-line interface of the program.
 *
 * This class:
 * - shows the menu
 * - gets user input
 * - calls MatchManager methods
 * - validates user input before creating objects
 *
 * This is the main application logic class for Phase 1.
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
     * Handles manual creation of a new MatchRecord.
     *
     * @return result message
     */
    public String addMatchFlow() {
        try {
            System.out.print("Enter Match ID: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Agent Name: ");
            String agentName = scanner.nextLine().trim();

            System.out.print("Enter Map Name: ");
            String mapName = scanner.nextLine().trim();

            System.out.print("Enter Kills: ");
            int kills = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Deaths: ");
            int deaths = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Assists: ");
            int assists = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Headshot Percentage: ");
            double hs = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Win (true/false): ");
            String winInput = scanner.nextLine().trim();

            System.out.print("Enter Match Date (YYYY-MM-DD): ");
            String matchDate = scanner.nextLine().trim();

            System.out.print("Enter Notes: ");
            String notes = scanner.nextLine().trim();

            String validationMessage = validateMatchInput(
                    agentName, mapName, kills, deaths, assists, hs, winInput, matchDate
            );

            if (!validationMessage.equals("VALID")) {
                return validationMessage;
            }

            boolean win = Boolean.parseBoolean(winInput);

            MatchRecord match = new MatchRecord(
                    matchId, agentName, mapName, kills, deaths, assists,
                    hs, win, matchDate, notes
            );

            return manager.addMatch(match);

        } catch (NumberFormatException e) {
            return "Invalid numeric input entered.";
        }
    }

    /**
     * Handles match removal flow.
     *
     * @return result message
     */
    public String removeMatchFlow() {
        try {
            System.out.print("Enter Match ID to remove: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());
            return manager.removeMatch(matchId);
        } catch (NumberFormatException e) {
            return "Match ID must be an integer.";
        }
    }

    /**
     * Handles record update flow.
     *
     * @return result message
     */
    public String updateMatchFlow() {
        try {
            System.out.print("Enter Match ID to update: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter field to update (agentName, mapName, kills, deaths, assists, headshotPercentage, win, matchDate, notes): ");
            String fieldName = scanner.nextLine().trim();

            System.out.print("Enter new value: ");
            String newValue = scanner.nextLine().trim();

            return manager.updateMatch(matchId, fieldName, newValue);
        } catch (NumberFormatException e) {
            return "Invalid Match ID entered.";
        }
    }

    /**
     * Validates all user input before creating a MatchRecord.
     *
     * @return "VALID" if all checks pass, otherwise a specific error message
     */
    public String validateMatchInput(String agentName, String mapName, int kills, int deaths,
                                     int assists, double hs, String winInput, String matchDate) {
        if (agentName.isEmpty()) {
            return "Agent name cannot be empty.";
        }
        if (mapName.isEmpty()) {
            return "Map name cannot be empty.";
        }
        if (kills < 0 || deaths < 0 || assists < 0) {
            return "Kills, deaths, and assists cannot be negative.";
        }
        if (hs < 0 || hs > 100) {
            return "Headshot percentage must be between 0 and 100.";
        }
        if (!winInput.equalsIgnoreCase("true") && !winInput.equalsIgnoreCase("false")) {
            return "Win must be entered as true or false.";
        }
        if (matchDate.isEmpty()) {
            return "Match date cannot be empty.";
        }
        return "VALID";
    }
}
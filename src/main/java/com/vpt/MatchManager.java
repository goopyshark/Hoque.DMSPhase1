package com.vpt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * MatchManager stores and manages all MatchRecord objects in memory.
 *
 * This class handles:
 * - loading data from a text file
 * - displaying all records
 * - adding records
 * - updating records
 * - deleting records
 * - generating the custom mathematical summary
 * - saving all current data back to the text file
 *
 * This class supports the CRUD requirements and custom feature for Phase 1.
 */
public class MatchManager {
    private ArrayList<MatchRecord> matches;
    private String currentFilePath;

    /**
     * Constructor initializes the list and starts with no file loaded.
     */
    public MatchManager() {
        matches = new ArrayList<>();
        currentFilePath = "";
    }

    /**
     * Loads records from a text file.
     *
     * This also stores the file path so later CRUD operations can automatically
     * save the updated records back to the same file.
     *
     * @param filePath path entered by the user
     * @return message describing load result
     */
    public String loadFromFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return "File does not exist.";
        }

        matches.clear();
        currentFilePath = filePath;
        int loadedCount = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length != 10) {
                    continue;
                }

                try {
                    int matchId = Integer.parseInt(parts[0].trim());
                    String agentName = parts[1].trim();
                    String mapName = parts[2].trim();
                    int kills = Integer.parseInt(parts[3].trim());
                    int deaths = Integer.parseInt(parts[4].trim());
                    int assists = Integer.parseInt(parts[5].trim());
                    double hs = Double.parseDouble(parts[6].trim());
                    boolean win = Boolean.parseBoolean(parts[7].trim());
                    String matchDate = parts[8].trim();
                    String notes = parts[9].trim();

                    if (kills < 0 || deaths < 0 || assists < 0 || hs < 0 || hs > 100) {
                        continue;
                    }

                    MatchRecord match = new MatchRecord(
                            matchId, agentName, mapName, kills, deaths,
                            assists, hs, win, matchDate, notes
                    );

                    matches.add(match);
                    loadedCount++;
                } catch (NumberFormatException e) {
                    // Skip badly formatted records without crashing the program
                }
            }
        } catch (FileNotFoundException e) {
            return "Could not open file.";
        }

        return loadedCount + " records loaded successfully.";
    }

    /**
     * Displays all currently loaded match records.
     *
     * @return formatted list of all records or a no-data message
     */
    public String displayAllMatches() {
        if (matches.isEmpty()) {
            return "No match records available.";
        }

        StringBuilder builder = new StringBuilder();
        for (MatchRecord match : matches) {
            builder.append(match).append("\n");
        }
        return builder.toString();
    }

    /**
     * Adds a new MatchRecord if the ID does not already exist.
     * If successful, the updated list is automatically saved back to the file.
     *
     * @param match new MatchRecord object
     * @return message describing result
     */
    public String addMatch(MatchRecord match) {
        if (findMatchById(match.getMatchId()) != null) {
            return "A match with that ID already exists.";
        }

        matches.add(match);
        return saveAfterChange("Match added successfully.");
    }

    /**
     * Finds a MatchRecord by its unique ID.
     *
     * @param matchId ID to search
     * @return matching MatchRecord or null if not found
     */
    public MatchRecord findMatchById(int matchId) {
        for (MatchRecord match : matches) {
            if (match.getMatchId() == matchId) {
                return match;
            }
        }
        return null;
    }

    /**
     * Removes a MatchRecord by ID.
     * If successful, the updated list is automatically saved back to the file.
     *
     * @param matchId ID to remove
     * @return result message
     */
    public String removeMatch(int matchId) {
        MatchRecord match = findMatchById(matchId);

        if (match == null) {
            return "Match ID not found.";
        }

        matches.remove(match);
        return saveAfterChange("Match removed successfully.");
    }

    /**
     * Updates a specific field in a specific MatchRecord.
     * If the update succeeds, the updated list is automatically saved back to the file.
     *
     * @param matchId record ID
     * @param fieldName field to update
     * @param newValue replacement value
     * @return result message
     */
    public String updateMatch(int matchId, String fieldName, String newValue) {
        MatchRecord match = findMatchById(matchId);

        if (match == null) {
            return "Match ID not found.";
        }

        String result = match.updateField(fieldName, newValue);

        if (result.toLowerCase().contains("successfully")) {
            return saveAfterChange(result);
        }

        return result;
    }

    /**
     * Generates the custom mathematical summary for all loaded matches.
     *
     * This feature calculates:
     * - average KDA
     * - average headshot percentage
     * - win rate
     * - best match based on highest KDA
     *
     * @return formatted summary string
     */
    public String generateImprovementSummary() {
        if (matches.isEmpty()) {
            return "No match data available for summary.";
        }

        double totalKDA = 0;
        double totalHS = 0;
        int wins = 0;

        MatchRecord bestMatch = matches.get(0);

        for (MatchRecord match : matches) {
            totalKDA += match.calculateKDA();
            totalHS += match.getHeadshotPercentage();

            if (match.isWin()) {
                wins++;
            }

            if (match.calculateKDA() > bestMatch.calculateKDA()) {
                bestMatch = match;
            }
        }

        double averageKDA = totalKDA / matches.size();
        double averageHS = totalHS / matches.size();
        double winRate = ((double) wins / matches.size()) * 100.0;

        return """
                ===== V-PT Improvement Summary =====
                Total Matches: %d
                Average KDA: %.2f
                Average Headshot Percentage: %.2f
                Win Rate: %.2f%%
                Best Match Agent: %s
                Best Match Map: %s
                Best Match KDA: %.2f
                ===================================
                """.formatted(
                matches.size(),
                averageKDA,
                averageHS,
                winRate,
                bestMatch.getAgentName(),
                bestMatch.getMapName(),
                bestMatch.calculateKDA()
        );
    }

    /**
     * Saves all currently loaded matches back to the same text file.
     *
     * This method is called automatically after successful add, update, or delete actions.
     *
     * @return save status message
     */
    public String saveToFile() {
        if (currentFilePath.isEmpty()) {
            return "No file is currently loaded. Changes are only saved in memory.";
        }

        try (PrintWriter writer = new PrintWriter(currentFilePath)) {
            for (MatchRecord match : matches) {
                writer.println(match.toFileString());
            }
            return "File saved successfully.";
        } catch (FileNotFoundException e) {
            return "Error saving file.";
        }
    }

    /**
     * Helper method used after successful changes.
     * Saves the file and appends save result to the success message.
     *
     * @param actionMessage base success message
     * @return combined action + save message
     */
    public String saveAfterChange(String actionMessage) {
        String saveMessage = saveToFile();
        return actionMessage + " " + saveMessage;
    }
}
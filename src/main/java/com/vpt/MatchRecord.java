package com.vpt;
/**
 * MatchRecord represents one Valorant match stored in the V-PT system.
 *
 * This class acts as the main object class for the project.
 * Each object contains multiple pieces of data using different data types.
 */
public class MatchRecord {
    private int matchId;
    private String agentName;
    private String mapName;
    private int kills;
    private int deaths;
    private int assists;
    private double headshotPercentage;
    private boolean win;
    private String matchDate;
    private String notes;

    /**
     * Full constructor used to create a complete MatchRecord object.
     */
    public MatchRecord(int matchId, String agentName, String mapName, int kills, int deaths,
                       int assists, double headshotPercentage, boolean win,
                       String matchDate, String notes) {
        this.matchId = matchId;
        this.agentName = agentName;
        this.mapName = mapName;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.headshotPercentage = headshotPercentage;
        this.win = win;
        this.matchDate = matchDate;
        this.notes = notes;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getMapName() {
        return mapName;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public double getHeadshotPercentage() {
        return headshotPercentage;
    }

    public boolean isWin() {
        return win;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getNotes() {
        return notes;
    }

    /**
     * Updates one field of the object based on the field name entered by the user.
     *
     * This method supports the Update portion of CRUD.
     * It also validates numeric ranges before applying the change.
     *
     * @param fieldName the name of the field the user wants to update
     * @param newValue the new value entered by the user
     * @return a message describing whether the update succeeded or failed
     */
    public String updateField(String fieldName, String newValue) {
        String field = fieldName.trim().toLowerCase();

        try {
            switch (field) {
                case "agentname":
                case "agent":
                    if (newValue.trim().isEmpty()) {
                        return "Agent name cannot be empty.";
                    }
                    agentName = newValue.trim();
                    return "Agent name updated successfully.";

                case "mapname":
                case "map":
                    if (newValue.trim().isEmpty()) {
                        return "Map name cannot be empty.";
                    }
                    mapName = newValue.trim();
                    return "Map name updated successfully.";

                case "kills":
                    int newKills = Integer.parseInt(newValue);
                    if (newKills < 0) {
                        return "Kills cannot be negative.";
                    }
                    kills = newKills;
                    return "Kills updated successfully.";

                case "deaths":
                    int newDeaths = Integer.parseInt(newValue);
                    if (newDeaths < 0) {
                        return "Deaths cannot be negative.";
                    }
                    deaths = newDeaths;
                    return "Deaths updated successfully.";

                case "assists":
                    int newAssists = Integer.parseInt(newValue);
                    if (newAssists < 0) {
                        return "Assists cannot be negative.";
                    }
                    assists = newAssists;
                    return "Assists updated successfully.";

                case "headshotpercentage":
                case "headshot":
                case "hs":
                    double newHs = Double.parseDouble(newValue);
                    if (newHs < 0 || newHs > 100) {
                        return "Headshot percentage must be between 0 and 100.";
                    }
                    headshotPercentage = newHs;
                    return "Headshot percentage updated successfully.";

                case "win":
                    if (!newValue.equalsIgnoreCase("true") && !newValue.equalsIgnoreCase("false")) {
                        return "Win must be true or false.";
                    }
                    win = Boolean.parseBoolean(newValue);
                    return "Win status updated successfully.";

                case "matchdate":
                case "date":
                    if (newValue.trim().isEmpty()) {
                        return "Match date cannot be empty.";
                    }
                    matchDate = newValue.trim();
                    return "Match date updated successfully.";

                case "notes":
                    notes = newValue.trim();
                    return "Notes updated successfully.";

                default:
                    return "Invalid field name.";
            }
        } catch (NumberFormatException e) {
            return "Invalid numeric value entered.";
        }
    }

    /**
     * Calculates KDA as:
     * (kills + assists) / deaths
     *
     * If deaths are zero, the method avoids division by zero and instead
     * returns kills + assists as a simplified value.
     *
     * This method supports the custom mathematical feature of the project.
     *
     * @return calculated KDA value
     */
    public double calculateKDA() {
        if (deaths == 0) {
            return kills + assists;
        }
        return (double) (kills + assists) / deaths;
    }

    /**
     * Converts the object into the exact text-file format used for saving.
     *
     * File format:
     * ID|Agent|Map|Kills|Deaths|Assists|HS%|Win|Date|Notes
     *
     * @return pipe-delimited string for file output
     */
    public String toFileString() {
        return matchId + "|" + agentName + "|" + mapName + "|" + kills + "|" + deaths + "|" +
                assists + "|" + headshotPercentage + "|" + win + "|" + matchDate + "|" + notes;
    }

    /**
     * Returns a user-friendly string for CLI display.
     */
    @Override
    public String toString() {
        return "ID: " + matchId +
                " | Agent: " + agentName +
                " | Map: " + mapName +
                " | Kills: " + kills +
                " | Deaths: " + deaths +
                " | Assists: " + assists +
                " | HS%: " + headshotPercentage +
                " | Win: " + win +
                " | Date: " + matchDate +
                " | Notes: " + notes +
                " | KDA: " + String.format("%.2f", calculateKDA());
    }
}
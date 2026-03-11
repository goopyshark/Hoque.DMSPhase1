Valorant Match Performance Tracker (V-PT)
Phase 1 CLI Application
Author: Ishrak Hoque

Project Overview
This project is a Phase 1 implementation of a Data Management System (DMS) for tracking Valorant match performance. The program is written in Java and runs through the command line interface (CLI). It supports CRUD operations, input validation, file loading, file saving, and a custom mathematical feature.

Project Features
1. Load match data from a text file
2. Display all match records
3. Add a new match record
4. Remove a match record by ID
5. Update any field of a match record
6. Generate an improvement summary using a mathematical calculation
7. Validate all user input so the program does not crash
8. Automatically save changes back to the text file after add, update, or delete

Object Stored
The main object in the system is MatchRecord.

Each MatchRecord stores:
- Match ID
- Agent Name
- Map Name
- Kills
- Deaths
- Assists
- Headshot Percentage
- Win Status
- Match Date
- Notes

Classes
1. Main.java
   - Contains the entry point for the program

2. VPTApplication.java
   - Handles the CLI menu
   - Accepts user input
   - Calls MatchManager methods
   - Performs input validation

3. MatchRecord.java
   - Represents one match record object
   - Stores all match fields
   - Supports field updates
   - Calculates KDA

4. MatchManager.java
   - Manages the list of MatchRecord objects
   - Performs CRUD operations
   - Loads from file
   - Saves back to file
   - Generates improvement summary

Text File Format
The file uses pipe-delimited records in the following format:

ID|Agent|Map|Kills|Deaths|Assists|HeadshotPercentage|Win|Date|Notes

Example:
1|Jett|Ascent|22|14|5|31.5|true|2026-03-01|Strong entry fragging

Validation
The program validates:
- Empty fields
- Negative kills, deaths, or assists
- Headshot percentage outside 0 to 100
- Invalid boolean input for win
- Invalid menu choices
- Invalid numeric input
- Nonexistent file path

Important Notes
- Phase 1 uses only CLI interaction
- Phase 1 does not use a GUI
- Phase 1 does not use a relational database yet
- The main method must be static because Java requires it
- All other logic follows object-oriented structure
Phase 2 Improvements & Fixes

1. Duplicate Match ID Validation (Improved Logic)
Previously, users could enter all match data before being notified that a duplicate ID existed.
  Fix:
The system now checks for an existing Match ID before collecting other inputs
If the ID already exists, the user is prompted to enter a new one immediately

2. Input Validation with Loops
Previously, invalid input (e.g., letters for numeric fields) would return the user to the main menu.
  Fix:
Each input field now uses a loop-based validation system
The user is repeatedly prompted until valid input is entered
  Validation includes:
Non-negative integers (kills, deaths, assists)
Headshot percentage between 0–100
Boolean values for win (true/false)
Non-empty required fields

3. Refactoring for Testability (SDLC Alignment)
The system was refactored to separate business logic from user interaction.
Changes:
  MatchManager methods:
    Accept parameters
    Return values instead of printing directly
    Scanner is only used in VPTApplication

4. JUnit Testing Implementation
JUnit 5 was integrated using Maven to test core functionality.
Tests Created:
  Add Match
  Remove Match
  Update Match
  Custom Feature (Improvement Summary)
  File Loading (invalid file case)

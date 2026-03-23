package com.vpt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchManagerTest {

    @Test
    void testAddMatch() {
        MatchManager manager = new MatchManager();

        MatchRecord match = new MatchRecord(
                1, "Jett", "Ascent", 20, 10, 5,
                30.0, true, "2026-03-01", "Test match"
        );

        String result = manager.addMatch(match);

        assertTrue(result.contains("Match added successfully"));
        assertNotNull(manager.findMatchById(1));
    }

    @Test
    void testRemoveMatch() {
        MatchManager manager = new MatchManager();

        MatchRecord match = new MatchRecord(
                2, "Sova", "Bind", 15, 10, 8,
                25.0, true, "2026-03-02", "Test match"
        );

        manager.addMatch(match);

        String result = manager.removeMatch(2);

        assertTrue(result.contains("removed"));
        assertNull(manager.findMatchById(2));
    }

    @Test
    void testUpdateMatch() {
        MatchManager manager = new MatchManager();

        MatchRecord match = new MatchRecord(
                3, "Omen", "Haven", 10, 10, 5,
                20.0, false, "2026-03-03", "Test match"
        );

        manager.addMatch(match);

        String result = manager.updateMatch(3, "kills", "25");

        assertTrue(result.contains("updated"));
        Assertions.assertEquals(25, manager.findMatchById(3).getKills());
    }

    @Test
    void testCustomFeature() {
        MatchManager manager = new MatchManager();

        manager.addMatch(new MatchRecord(4, "Jett", "Ascent", 20, 10, 5, 30, true, "2026", ""));
        manager.addMatch(new MatchRecord(5, "Sova", "Bind", 10, 10, 5, 20, false, "2026", ""));

        String summary = manager.generateImprovementSummary();

        assertTrue(summary.contains("Average KDA"));
        assertTrue(summary.contains("Win Rate"));
    }

    @Test
    void testLoadFileInvalid() {
        MatchManager manager = new MatchManager();

        String result = manager.loadFromFile("fakefile.txt");

        assertTrue(result.contains("does not exist"));
    }
}
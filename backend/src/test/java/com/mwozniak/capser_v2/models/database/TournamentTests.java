package com.mwozniak.capser_v2.models.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tournament Test")
class TournamentTests {

    @Test
    void singleEliminationTournamentCreation_shouldCreateRightNumberOfEntries() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        assertEquals(15, easyCapsTournament.getBracketEntries().size());
    }


    @Test
    void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries14() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        assertEquals(14, easyCapsTournament.getBracketEntries().size());
    }


    @Test
    void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries30() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);

        assertEquals(30, easyCapsTournament.getBracketEntries().size());
    }


    @Test
    void doubleElimination_seedsCorrectly() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();

        assertNull(getBracketEntry(easyCapsTournament, 6).getPlayer1());

        assertNull(getBracketEntry(easyCapsTournament, 6).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 4).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 7).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 7).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 5).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 5).getPlayer2());
    }

    @Test
    void singleElimination_seedsCorrectly() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);
        easyCapsTournament.seedPlayers();

        assertNull(getBracketEntry(easyCapsTournament, 10).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 10).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 7).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 7).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 14).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 14).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 8).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 8).getPlayer2());

    }

    @Test
    void singleElimination_seedsCorrectly8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();

        assertNull(getBracketEntry(easyCapsTournament, 5).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 5).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 3).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 3).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 6).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 6).getPlayer2());
        assertNotNull(getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 4).getPlayer2());
    }

    @Test
    void singleElimination_resolveByesCorrectly8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer2());
        assertEquals(players.get(4).getUser(), getBracketEntry(easyCapsTournament, 1).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament, 4).isBye());

    }

    @Test
    void doubleElimination_resolveByesCorrectly8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNotNull(getBracketEntry(easyCapsTournament, 2).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament, 5).isBye());

    }

    @Test
    void doubleElimination_resolveByesCorrectlyFinal8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNotNull(getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertTrue(getBracketEntry(easyCapsTournament, 5).isBye());
        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer2());
    }

    @Test
    void singleElimination_resolveByesCorrectly16() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNotNull(getBracketEntry(easyCapsTournament, 3).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament, 8).isBye());

    }


    @Test
    void singleElimination_progressesCorrectly8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNull(getBracketEntry(easyCapsTournament, 1).getPlayer1());

        addGameToEntry(easyCapsTournament, 3, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 0).getPlayer2());

        addGameToEntry(easyCapsTournament, 6, true);
        addGameToEntry(easyCapsTournament, 1, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 0).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament, 0, true);

        assertTrue(easyCapsTournament.isFinished());
    }

    @Test
    void doubleElimination_progressesCorrectly16() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        addGameToEntry(easyCapsTournament, 8, true);
        addGameToEntry(easyCapsTournament, 15, true);
        addGameToEntry(easyCapsTournament, 4, true);
        addGameToEntry(easyCapsTournament, 1006, true);
        addGameToEntry(easyCapsTournament, 1, false);
        addGameToEntry(easyCapsTournament, 1001, false);
        addGameToEntry(easyCapsTournament, 1000, false);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(players.get(0).getUser(),getBracketEntry(easyCapsTournament, 8).getPlayer1());
        assertEquals(players.get(2).getUser(),getBracketEntry(easyCapsTournament, 8).getPlayer2());

        assertEquals(players.get(4).getUser(),getBracketEntry(easyCapsTournament, 9).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 9).getPlayer2());

        assertEquals(players.get(3).getUser(),getBracketEntry(easyCapsTournament, 15).getPlayer1());
        assertEquals(players.get(1).getUser(),getBracketEntry(easyCapsTournament, 15).getPlayer2());

        assertEquals(players.get(0).getUser(),getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertEquals(players.get(4).getUser(),getBracketEntry(easyCapsTournament, 4).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 7).getPlayer1());
        assertEquals(players.get(3).getUser(),getBracketEntry(easyCapsTournament, 7).getPlayer2());

        assertEquals(players.get(0).getUser(),getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 2).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 3).getPlayer1());
        assertEquals(players.get(3).getUser(),getBracketEntry(easyCapsTournament, 3).getPlayer2());

        assertEquals(players.get(0).getUser(),getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertEquals(players.get(3).getUser(),getBracketEntry(easyCapsTournament, 1).getPlayer2());

        assertEquals(players.get(3).getUser(),getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertEquals(players.get(2).getUser(),getBracketEntry(easyCapsTournament, 0).getPlayer2());

        assertEquals(players.get(1).getUser(),getBracketEntry(easyCapsTournament, 1010).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1010).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1013).getPlayer1());
        assertEquals(players.get(2).getUser(),getBracketEntry(easyCapsTournament, 1013).getPlayer2());

        assertEquals(players.get(4).getUser(),getBracketEntry(easyCapsTournament, 1006).getPlayer1());
        assertEquals(players.get(1).getUser(),getBracketEntry(easyCapsTournament, 1006).getPlayer2());

        assertEquals(players.get(4).getUser(),getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertEquals(players.get(2).getUser(),getBracketEntry(easyCapsTournament, 1001).getPlayer2());

        assertEquals(players.get(0).getUser(),getBracketEntry(easyCapsTournament, 1000).getPlayer1());
        assertEquals(players.get(2).getUser(),getBracketEntry(easyCapsTournament, 1000).getPlayer2());

        assertTrue(easyCapsTournament.isFinished());
        assertEquals(players.get(3).getUser().getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
    }

    @Test
    void doubleElimination_progressesCorrectly8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();


        assertNull(getBracketEntry(easyCapsTournament, 2).getPlayer1());

        addGameToEntry(easyCapsTournament, 4, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1).getPlayer2());

        addGameToEntry(easyCapsTournament, 7, true);
        addGameToEntry(easyCapsTournament, 2, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 1).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament, 1, true);

        assertNull(getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 1001).getPlayer2());

        addGameToEntry(easyCapsTournament, 1002, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 1001).getPlayer2());

        addGameToEntry(easyCapsTournament, 1001, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 1000).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 1000).getPlayer2());

        addGameToEntry(easyCapsTournament, 1000, true);

        assertNotNull(getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertNotNull(getBracketEntry(easyCapsTournament, 0).getPlayer2());

        addGameToEntry(easyCapsTournament, 0, true);

        assertTrue(easyCapsTournament.isFinished());
    }

    @Test
    void doubleElimination_progressesCorrectlyWithMemory8() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        User user0 = players.get(0).getUser();
        User user1 = players.get(1).getUser();
        User user2 = players.get(2).getUser();
        User user3 = players.get(3).getUser();
        User user4 = players.get(4).getUser();

        addGameToEntry(easyCapsTournament, 4, true);
        skipGame(easyCapsTournament, user3, 7);
        addGameToEntry(easyCapsTournament, 2, true);

        addGameToEntry(easyCapsTournament, 1001, false);
        addGameToEntry(easyCapsTournament, 1, true);
        skipGame(easyCapsTournament, user1, 1000);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(user0,getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 4).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament, 5).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 5).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 6).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 6).getPlayer2());

        assertEquals(user3,getBracketEntry(easyCapsTournament, 7).getPlayer1());
        assertEquals(user1,getBracketEntry(easyCapsTournament, 7).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertEquals(user4,getBracketEntry(easyCapsTournament, 2).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 3).getPlayer1());
        assertEquals(user1,getBracketEntry(easyCapsTournament, 3).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertEquals(user1,getBracketEntry(easyCapsTournament, 1).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1004).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1004).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1005).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1005).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament, 1002).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1002).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1003).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1003).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1001).getPlayer2());

        assertEquals(user1,getBracketEntry(easyCapsTournament, 1000).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1000).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 0 ).getPlayer2());

        assertTrue(easyCapsTournament.isFinished());
        assertEquals(user0.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
    }

    @Test
    void doubleElimination_progressesCorrectlyWithMemory16() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        User user0 = players.get(0).getUser();
        User user1 = players.get(1).getUser();
        User user2 = players.get(2).getUser();
        User user3 = players.get(3).getUser();
        User user4 = players.get(4).getUser();
        User user5 = players.get(5).getUser();
        User user6 = players.get(6).getUser();
        User user7 = players.get(7).getUser();
        User user8 = players.get(8).getUser();
        User user9 = players.get(9).getUser();
        User user10 = players.get(10).getUser();
        User user11 = players.get(11).getUser();
        User user12 = players.get(12).getUser();
        User user13 = players.get(13).getUser();
        User user14 = players.get(14).getUser();

        addGameToEntry(easyCapsTournament, 8, true);
        addGameToEntry(easyCapsTournament, 9, false);
        addGameToEntry(easyCapsTournament, 10, true);
        skipGame(easyCapsTournament, user12, 11);
        addGameToEntry(easyCapsTournament, 13, true);
        addGameToEntry(easyCapsTournament, 14, false);
        addGameToEntry(easyCapsTournament, 15, true);
        addGameToEntry(easyCapsTournament, 4, true);
        addGameToEntry(easyCapsTournament, 5, true);
        addGameToEntry(easyCapsTournament, 6, true);
        addGameToEntry(easyCapsTournament, 7, false);
        addGameToEntry(easyCapsTournament, 3, true);
        skipGame(easyCapsTournament, user8, 2);
        skipGame(easyCapsTournament, user0, 1);
        addGameToEntry(easyCapsTournament, 1010, true);
        addGameToEntry(easyCapsTournament, 1013, false);
        addGameToEntry(easyCapsTournament, 1006, false);
        addGameToEntry(easyCapsTournament, 1007, true);
        addGameToEntry(easyCapsTournament, 1008, false);
        addGameToEntry(easyCapsTournament, 1009, true);
        addGameToEntry(easyCapsTournament, 1004, false);
        skipGame(easyCapsTournament, user5, 1005);
        addGameToEntry(easyCapsTournament, 1002, true);
        addGameToEntry(easyCapsTournament, 1001, false);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(user0,getBracketEntry(easyCapsTournament, 8).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 8).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament, 9).getPlayer1());
        assertEquals(user6,getBracketEntry(easyCapsTournament, 9).getPlayer2());

        assertEquals(user8,getBracketEntry(easyCapsTournament, 10).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 10).getPlayer2());

        assertEquals(user12,getBracketEntry(easyCapsTournament, 11).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament, 11).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 12).getPlayer1());
        assertEquals(user13,getBracketEntry(easyCapsTournament, 12).getPlayer2());

        assertEquals(user7,getBracketEntry(easyCapsTournament, 14).getPlayer1());
        assertEquals(user5,getBracketEntry(easyCapsTournament, 14).getPlayer2());

        assertEquals(user3,getBracketEntry(easyCapsTournament, 15).getPlayer1());
        assertEquals(user1,getBracketEntry(easyCapsTournament, 15).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertEquals(user6,getBracketEntry(easyCapsTournament, 4).getPlayer2());

        assertEquals(user8,getBracketEntry(easyCapsTournament, 5).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament, 5).getPlayer2());

        assertEquals(user13,getBracketEntry(easyCapsTournament, 6).getPlayer1());
        assertEquals(user11,getBracketEntry(easyCapsTournament, 6).getPlayer2());

        assertEquals(user5,getBracketEntry(easyCapsTournament, 7).getPlayer1());
        assertEquals(user3,getBracketEntry(easyCapsTournament, 7).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertEquals(user8,getBracketEntry(easyCapsTournament, 2).getPlayer2());

        assertEquals(user13,getBracketEntry(easyCapsTournament, 3).getPlayer1());
        assertEquals(user3,getBracketEntry(easyCapsTournament, 3).getPlayer2());

        assertEquals(user0,getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertEquals(user13,getBracketEntry(easyCapsTournament, 1).getPlayer2());

        assertEquals(user13,getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 0).getPlayer2());

        assertEquals(user1,getBracketEntry(easyCapsTournament, 1010).getPlayer1());
        assertEquals(user7,getBracketEntry(easyCapsTournament, 1010).getPlayer2());

        assertEquals(user9,getBracketEntry(easyCapsTournament, 1011).getPlayer1());
        assertNull(getBracketEntry(easyCapsTournament, 1011).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1012).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 1012).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament, 1013).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1013).getPlayer2());

        assertEquals(user6,getBracketEntry(easyCapsTournament, 1006).getPlayer1());
        assertEquals(user1,getBracketEntry(easyCapsTournament, 1006).getPlayer2());

        assertEquals(user14,getBracketEntry(easyCapsTournament, 1007).getPlayer1());
        assertEquals(user9,getBracketEntry(easyCapsTournament, 1007).getPlayer2());

        assertEquals(user11,getBracketEntry(easyCapsTournament, 1008).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 1008).getPlayer2());

        assertEquals(user5,getBracketEntry(easyCapsTournament, 1009).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament, 1009).getPlayer2());

        assertEquals(user1,getBracketEntry(easyCapsTournament, 1004).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament, 1004).getPlayer2());

        assertEquals(user10,getBracketEntry(easyCapsTournament, 1005).getPlayer1());
        assertEquals(user5,getBracketEntry(easyCapsTournament, 1005).getPlayer2());

        assertEquals(user3,getBracketEntry(easyCapsTournament, 1002).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament, 1002).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1003).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 1003).getPlayer2());

        assertEquals(user3,getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 1001).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament, 1000).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament, 1000).getPlayer2());

        assertTrue(easyCapsTournament.isFinished());
        assertEquals(user13.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
    }

    @Test
    void singleElimination_progressesCorrectlyWithMemory16() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        User user1 = getBracketEntry(easyCapsTournament, 7).getPlayer1();
        User user4 = getBracketEntry(easyCapsTournament, 8).getPlayer2();
        User user5 = getBracketEntry(easyCapsTournament, 9).getPlayer1();
        User user7 = getBracketEntry(easyCapsTournament, 10).getPlayer1();
        User user8 = getBracketEntry(easyCapsTournament, 10).getPlayer2();
        User user9 = getBracketEntry(easyCapsTournament, 11).getPlayer2();
        User user10 = getBracketEntry(easyCapsTournament, 12).getPlayer1();
        User user13 = getBracketEntry(easyCapsTournament, 13).getPlayer2();
        User user14 = getBracketEntry(easyCapsTournament, 14).getPlayer1();

        addGameToEntry(easyCapsTournament, 7, true);
        addGameToEntry(easyCapsTournament, 8, false);
        addGameToEntry(easyCapsTournament, 9, true);
        skipGame(easyCapsTournament, user7, 10);
        addGameToEntry(easyCapsTournament, 12, true);
        addGameToEntry(easyCapsTournament, 13, false);
        addGameToEntry(easyCapsTournament, 14, true);
        addGameToEntry(easyCapsTournament, 3, true);
        addGameToEntry(easyCapsTournament, 4, true);
        addGameToEntry(easyCapsTournament, 5, true);
        addGameToEntry(easyCapsTournament, 6, false);
        addGameToEntry(easyCapsTournament, 2, true);
        skipGame(easyCapsTournament, user1, 1);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(user1, getBracketEntry(easyCapsTournament, 3).getPlayer1());
        assertEquals(user4, getBracketEntry(easyCapsTournament, 3).getPlayer2());
        assertEquals(user5, getBracketEntry(easyCapsTournament, 4).getPlayer1());
        assertEquals(user8, getBracketEntry(easyCapsTournament, 4).getPlayer2());
        assertEquals(user9, getBracketEntry(easyCapsTournament, 5).getPlayer1());
        assertEquals(user10, getBracketEntry(easyCapsTournament, 5).getPlayer2());
        assertEquals(user13, getBracketEntry(easyCapsTournament, 6).getPlayer1());
        assertEquals(user14, getBracketEntry(easyCapsTournament, 6).getPlayer2());
        assertEquals(user1, getBracketEntry(easyCapsTournament, 1).getPlayer1());
        assertEquals(user5, getBracketEntry(easyCapsTournament, 1).getPlayer2());
        assertEquals(user9, getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertEquals(user14, getBracketEntry(easyCapsTournament, 2).getPlayer2());
        assertEquals(user5, getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertEquals(user9, getBracketEntry(easyCapsTournament, 0).getPlayer2());

        assertEquals(user5.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
        assertTrue(easyCapsTournament.isFinished());
    }

    @Test
    void singleElimination_resolvesByesCorrectlyInSpecialCasePickedSeed() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.PICKED);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setUsername("a" + i);
            user.setId(UUID.randomUUID());
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        List<BracketEntry> seedsEntries = new ArrayList<>();

        seedsEntries.add(createBracketEntryForCordAndPlayers(10, players.get(0).getUser(), null));
        seedsEntries.add(createBracketEntryForCordAndPlayers(12, players.get(1).getUser(), null));
        seedsEntries.add(createBracketEntryForCordAndPlayers(8, players.get(2).getUser(), players.get(3).getUser()));
        seedsEntries.add(createBracketEntryForCordAndPlayers(9, players.get(4).getUser(), null));
        seedsEntries.add(createBracketEntryForCordAndPlayers(7, players.get(5).getUser(), players.get(6).getUser()));
        seedsEntries.add(createBracketEntryForCordAndPlayers(14, null, players.get(7).getUser()));

        easyCapsTournament.setSeeds(seedsEntries);
        easyCapsTournament.seedPlayers();

        assertNull(getBracketEntry(easyCapsTournament, 0).getCompetitor1());
        assertNull(getBracketEntry(easyCapsTournament, 0).getCompetitor2());

        assertNotNull(getBracketEntry(easyCapsTournament, 2).getCompetitor1());
        assertNotNull(getBracketEntry(easyCapsTournament, 2).getCompetitor2());

    }

    @Test
    void singleElimination_resolvesByesCorrectlyInSpecialCaseRandomSeed() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setUsername("a" + i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 10), players.get(0).getUser(), null);
        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 12), players.get(1).getUser(), null);
        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 8), players.get(2).getUser(), players.get(3).getUser());
        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 9), players.get(4).getUser(), null);
        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 7), players.get(5).getUser(), players.get(6).getUser());
        setPlayersInBracketEntry(getBracketEntry(easyCapsTournament, 14), null, players.get(7).getUser());

        easyCapsTournament.setSeeded(true);

        easyCapsTournament.resolveByes();

        assertNull(getBracketEntry(easyCapsTournament, 0).getCompetitor1());
        assertNull(getBracketEntry(easyCapsTournament, 0).getCompetitor2());

    }


    @Test
    void doubleElimination_resolvesByesCorrectlyInSpecialCasePickedSeed() {
        Tournament easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.PICKED);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);

        List<UserBridge> players = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setUsername("a" + i);
            user.setId(UUID.randomUUID());
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        List<BracketEntry> seedsEntries = new ArrayList<>();

        seedsEntries.add(createBracketEntryForCordAndPlayers(8, players.get(0).getUser(), players.get(1).getUser()));
        seedsEntries.add(createBracketEntryForCordAndPlayers(9, players.get(2).getUser(), players.get(3).getUser()));
        seedsEntries.add(createBracketEntryForCordAndPlayers(10, players.get(4).getUser(), null));
        seedsEntries.add(createBracketEntryForCordAndPlayers(11, players.get(5).getUser(), null));
        seedsEntries.add(createBracketEntryForCordAndPlayers(12, players.get(6).getUser(), players.get(7).getUser()));

        easyCapsTournament.setSeeds(seedsEntries);
        easyCapsTournament.seedPlayers();

        addGameToEntry(easyCapsTournament, 8, true);
        addGameToEntry(easyCapsTournament, 9, true);
        addGameToEntry(easyCapsTournament, 12, true);
        addGameToEntry(easyCapsTournament, 5, true);
        skipGame(easyCapsTournament, players.get(0).getUser(), 4);

        assertFalse(getBracketEntry(easyCapsTournament, 1013).isFinal());
        assertEquals(players.get(2).getUser(), getBracketEntry(easyCapsTournament, 2).getPlayer1());
        assertEquals(players.get(4).getUser(), getBracketEntry(easyCapsTournament, 2).getPlayer2());
    }


    @Test
    void singleEliminationGetAbove_returnsCorrectNumber() {
        int result = BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.RO_16);
        assertEquals(7, result);
    }


    @Test
    void doubleEliminationGetAbove_returnsCorrectNumber() {
        assertEquals(1, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_2, true));
        assertEquals(2, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_4, true));
        assertEquals(4, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_8, true));
        assertEquals(8, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_12, true));
        assertEquals(0, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_2, false));
        assertEquals(1, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_3, false));
        assertEquals(2, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_4, false));
        assertEquals(4, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_6, false));
        assertEquals(6, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_8, false));
        assertEquals(10, BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_12, false));
    }

    @Test
    void doubleEliminationGetAboveAndEqual_returnsCorrectNumber() {
        assertEquals(2, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_2, true));
        assertEquals(4, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_4, true));
        assertEquals(8, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_8, true));
        assertEquals(16, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_16, true));
        assertEquals(1, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_2, false));
        assertEquals(2, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_3, false));
        assertEquals(4, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_4, false));
        assertEquals(6, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_6, false));
        assertEquals(10, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_8, false));
        assertEquals(14, BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_12, false));
    }


    @Test
    void singleEliminationGetAboveAndEquals_returnsCorrectNumber() {
        int result = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.RO_16);
        assertEquals(15, result);
    }

    @Test
    void testIsPowerOf2() {
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_8));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_8));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_12));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_3));
    }

    @Test
    void testGetHigherPowerOf2() {
        assertEquals(BracketEntryType.D_RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_16));
        assertEquals(BracketEntryType.D_RO_4, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_8));
        assertEquals(BracketEntryType.D_RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_12));
        assertEquals(BracketEntryType.RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.RO_16));
    }


    private void addGameToEntry(Tournament easyCapsTournament, int coord, boolean player1Wins) {
        EasyCapsBracketEntry entry2 = getBracketEntry(easyCapsTournament, coord);

        EasyCapsGame game2 = new EasyCapsGame();
        game2.setPlayer1(entry2.getPlayer1().getId());
        game2.setPlayer2(entry2.getPlayer2().getId());
        if (player1Wins) {
            game2.setWinner(entry2.getPlayer1().getId());
        } else {
            game2.setWinner(entry2.getPlayer2().getId());
        }

        entry2.setGame(game2);
        entry2.setFinal(true);

        easyCapsTournament.resolveAfterGame();
    }

    private void skipGame(Tournament tournament, User skippingUser, int coord) {
        EasyCapsBracketEntry entry2 = getBracketEntry(tournament, coord);
        entry2.forfeitGame(skippingUser);
        tournament.resolveAfterGame();
    }

    private EasyCapsBracketEntry getBracketEntry(Tournament tournament, int coord) {
        return (EasyCapsBracketEntry) tournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == coord).findAny().get();
    }

    private BracketEntry createBracketEntryForCordAndPlayers(int cord, User player1, User player2) {
        BracketEntry bracketEntry = new EasyCapsBracketEntry();
        bracketEntry.setCoordinate(cord);
        bracketEntry.setCompetitor1(player1);
        bracketEntry.setCompetitor2(player2);

        return bracketEntry;
    }

    private void setPlayersInBracketEntry(BracketEntry bracketEntry, User player1, User player2) {
        bracketEntry.setCompetitor1(player1);
        bracketEntry.setCompetitor2(player2);
        bracketEntry.setFinal(true);
        if (player1 == null || player2 == null) {
            bracketEntry.setBye(true);
        }
    }

    private void printTournamentJson(Tournament tournament) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(tournament));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

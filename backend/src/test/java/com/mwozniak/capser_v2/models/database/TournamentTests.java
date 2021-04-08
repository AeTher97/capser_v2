package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.tournament.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.EasyCapsTournament;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.UserBridge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tournament Test")
public class TournamentTests {

    @Test
    public void singleEliminationTournamentCreation_shouldCreateRightNumberOfEntries() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);


        assertEquals(15, easyCapsTournament.getBracketEntries().size());
    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries14() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);


        assertEquals(14, easyCapsTournament.getBracketEntries().size());


    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries30() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);


        assertEquals(30, easyCapsTournament.getBracketEntries().size());


    }


    @Test
    public void doubleElimination_seedsCorrectly() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getPlayer2());

    }

    @Test
    public void singleElimination_seedsCorrectly() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 10).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 10).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 14).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 14).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get()).getPlayer2());

    }

    @Test
    public void singleElimination_seedsCorrectly8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getPlayer2());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getPlayer2());

    }

    @Test
    public void singleElimination_resolveByesCorrectly8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get().isBye());

    }

    @Test
    public void doubleElimination_resolveByesCorrectly8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer2());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get().isBye());

    }

    @Test
    public void doubleElimination_resolveByesCorrectlyFinal8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get().isBye());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

    }

    @Test
    public void singleElimination_resolveByesCorrectly16() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getPlayer2());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get().isBye());

    }

    @Test
    public void singleElimination_progressesCorrectly8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());

        addGameToEntry(easyCapsTournament, 3, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 6, true);
        addGameToEntry(easyCapsTournament, 1, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament, 0, true);

        assertTrue(easyCapsTournament.isFinished());


    }

    @Test
    public void doubleElimination_progressesCorrectly16() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1009).findAny().get()).getPlayer1());

        addGameToEntry(easyCapsTournament, 1001, false);
        addGameToEntry(easyCapsTournament, 1000, false);
        addGameToEntry(easyCapsTournament, 0, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());


        assertTrue(easyCapsTournament.isFinished());

    }

    @Test
    public void doubleElimination_progressesCorrectly8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());

        addGameToEntry(easyCapsTournament, 4, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 7, true);
        addGameToEntry(easyCapsTournament, 2, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament, 1, true);

        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 1002, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 1001, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 1000, true);

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament, 0, true);

        assertTrue(easyCapsTournament.isFinished());


    }

    @Test
    public void doubleElimination_progressesCorrectlyWithMemory8() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        User user1 = getBracketEntry(easyCapsTournament, 4).getPlayer1();
        User user2 = getBracketEntry(easyCapsTournament, 4).getPlayer2();
        User user3 = getBracketEntry(easyCapsTournament, 5).getPlayer1();
        User user4 = getBracketEntry(easyCapsTournament, 7).getPlayer1();
        User user5 = getBracketEntry(easyCapsTournament, 7).getPlayer2();

        addGameToEntry(easyCapsTournament, 4, true);
        skipGame(easyCapsTournament, user5, 7);
        addGameToEntry(easyCapsTournament, 2, true);
        addGameToEntry(easyCapsTournament, 1002, false);
        addGameToEntry(easyCapsTournament, 1, true);
        skipGame(easyCapsTournament, user2, 1000);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(user2, getBracketEntry(easyCapsTournament, 1004).getPlayer1());
        assertEquals(user3, getBracketEntry(easyCapsTournament, 1002).getPlayer1());
        assertEquals(user2, getBracketEntry(easyCapsTournament, 1002).getPlayer2());
        assertEquals(user2, getBracketEntry(easyCapsTournament, 1001).getPlayer1());
        assertTrue(getBracketEntry(easyCapsTournament,1001).isBye());
        assertEquals(user4, getBracketEntry(easyCapsTournament, 1000).getPlayer1());
        assertEquals(user2, getBracketEntry(easyCapsTournament, 1000).getPlayer2());
        assertEquals(user1, getBracketEntry(easyCapsTournament, 0).getPlayer1());
        assertEquals(user4, getBracketEntry(easyCapsTournament, 0).getPlayer2());
        assertEquals(user1.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
        assertTrue(easyCapsTournament.isFinished());

    }

    @Test
    public void doubleElimination_progressesCorrectlyWithMemory16() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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

        User user1 = getBracketEntry(easyCapsTournament, 8).getPlayer1();
        User user2 = getBracketEntry(easyCapsTournament, 8).getPlayer2();
        User user3 = getBracketEntry(easyCapsTournament, 9).getPlayer1();
        User user4 = getBracketEntry(easyCapsTournament, 9).getPlayer2();
        User user5 = getBracketEntry(easyCapsTournament, 10).getPlayer1();
        User user6 = getBracketEntry(easyCapsTournament, 10).getPlayer2();
        User user7 = getBracketEntry(easyCapsTournament, 11).getPlayer1();
        User user8 = getBracketEntry(easyCapsTournament, 11).getPlayer2();
        User user9 = getBracketEntry(easyCapsTournament, 12).getPlayer2();
        User user10 = getBracketEntry(easyCapsTournament, 13).getPlayer1();
        User user11 = getBracketEntry(easyCapsTournament, 13).getPlayer2();
        User user12 = getBracketEntry(easyCapsTournament, 14).getPlayer1();
        User user13 = getBracketEntry(easyCapsTournament, 14).getPlayer2();
        User user14 = getBracketEntry(easyCapsTournament, 15).getPlayer1();
        User user15 = getBracketEntry(easyCapsTournament, 15).getPlayer2();

        addGameToEntry(easyCapsTournament, 8, true);
        addGameToEntry(easyCapsTournament, 9, false);
        addGameToEntry(easyCapsTournament, 10, true);
        skipGame(easyCapsTournament, user7, 11);
        addGameToEntry(easyCapsTournament, 13, true);
        addGameToEntry(easyCapsTournament, 14, false);
        addGameToEntry(easyCapsTournament, 15, true);
        addGameToEntry(easyCapsTournament, 4, true);
        addGameToEntry(easyCapsTournament, 5, true);
        addGameToEntry(easyCapsTournament, 6, true);
        addGameToEntry(easyCapsTournament, 7, false);
        addGameToEntry(easyCapsTournament, 3, true);
        skipGame(easyCapsTournament, user1,2);
        skipGame(easyCapsTournament, user5,1);
        addGameToEntry(easyCapsTournament, 1010, true);
        addGameToEntry(easyCapsTournament, 1013, false);
        addGameToEntry(easyCapsTournament, 1006, false);
        addGameToEntry(easyCapsTournament, 1007, true);
        addGameToEntry(easyCapsTournament, 1008, false);
        addGameToEntry(easyCapsTournament, 1009, true);
        addGameToEntry(easyCapsTournament, 1004, false);
        skipGame(easyCapsTournament, user11,1005);
        addGameToEntry(easyCapsTournament, 1003, true);
        addGameToEntry(easyCapsTournament, 1001, false);
        addGameToEntry(easyCapsTournament, 0, true);

        assertEquals(user1,getBracketEntry(easyCapsTournament,4).getPlayer1());
        assertEquals(user4,getBracketEntry(easyCapsTournament,4).getPlayer2());
        assertEquals(user5,getBracketEntry(easyCapsTournament,5).getPlayer1());
        assertEquals(user8,getBracketEntry(easyCapsTournament,5).getPlayer2());
        assertEquals(user9,getBracketEntry(easyCapsTournament,6).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament,6).getPlayer2());
        assertEquals(user13,getBracketEntry(easyCapsTournament,7).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,7).getPlayer2());
        assertEquals(user1,getBracketEntry(easyCapsTournament,2).getPlayer1());
        assertEquals(user5,getBracketEntry(easyCapsTournament,2).getPlayer2());
        assertEquals(user9,getBracketEntry(easyCapsTournament,3).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,3).getPlayer2());
        assertEquals(user5,getBracketEntry(easyCapsTournament,1).getPlayer1());
        assertEquals(user9,getBracketEntry(easyCapsTournament,1).getPlayer2());
        assertEquals(user9,getBracketEntry(easyCapsTournament,0).getPlayer1());

        assertEquals(user2,getBracketEntry(easyCapsTournament,1010).getPlayer1());
        assertEquals(user3,getBracketEntry(easyCapsTournament,1010).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament,1011).getPlayer2());
        assertEquals(user6,getBracketEntry(easyCapsTournament,1011).getPlayer1());
        assertTrue(getBracketEntry(easyCapsTournament,1011).isBye());

        assertNull(getBracketEntry(easyCapsTournament,1012).getPlayer1());
        assertEquals(user11,getBracketEntry(easyCapsTournament,1012).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament,1012).isBye());

        assertEquals(user12,getBracketEntry(easyCapsTournament,1013).getPlayer1());
        assertEquals(user15,getBracketEntry(easyCapsTournament,1013).getPlayer2());

        assertEquals(user4,getBracketEntry(easyCapsTournament,1006).getPlayer1());
        assertEquals(user2,getBracketEntry(easyCapsTournament,1006).getPlayer2());

        assertEquals(user8,getBracketEntry(easyCapsTournament,1007).getPlayer1());
        assertEquals(user6,getBracketEntry(easyCapsTournament,1007).getPlayer2());

        assertEquals(user10,getBracketEntry(easyCapsTournament,1008).getPlayer1());
        assertEquals(user11,getBracketEntry(easyCapsTournament,1008).getPlayer2());

        assertEquals(user13,getBracketEntry(easyCapsTournament,1009).getPlayer1());
        assertEquals(user15,getBracketEntry(easyCapsTournament,1009).getPlayer2());

        assertEquals(user2,getBracketEntry(easyCapsTournament,1004).getPlayer1());
        assertEquals(user8,getBracketEntry(easyCapsTournament,1004).getPlayer2());

        assertEquals(user11,getBracketEntry(easyCapsTournament,1005).getPlayer1());
        assertEquals(user13,getBracketEntry(easyCapsTournament,1005).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament,1002).getPlayer1());
        assertEquals(user8,getBracketEntry(easyCapsTournament,1002).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament,1002).isBye());

        assertEquals(user14,getBracketEntry(easyCapsTournament,1003).getPlayer1());
        assertEquals(user13,getBracketEntry(easyCapsTournament,1003).getPlayer2());

        assertEquals(user8,getBracketEntry(easyCapsTournament,1001).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,1001).getPlayer2());

        assertNull(getBracketEntry(easyCapsTournament,1000).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,1000).getPlayer2());
        assertTrue(getBracketEntry(easyCapsTournament,1000).isBye());

        assertEquals(user9,getBracketEntry(easyCapsTournament,0).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,0).getPlayer2());

        assertEquals(user9.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
        assertTrue(easyCapsTournament.isFinished());

    }

    @Test
    public void singleElimination_progressesCorrectlyWithMemory16() {
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
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
        User user2 = getBracketEntry(easyCapsTournament, 7).getPlayer2();
        User user3 = getBracketEntry(easyCapsTournament, 8).getPlayer1();
        User user4 = getBracketEntry(easyCapsTournament, 8).getPlayer2();
        User user5 = getBracketEntry(easyCapsTournament, 9).getPlayer1();
        User user6 = getBracketEntry(easyCapsTournament, 9).getPlayer2();
        User user7 = getBracketEntry(easyCapsTournament, 10).getPlayer1();
        User user8 = getBracketEntry(easyCapsTournament, 10).getPlayer2();
        User user9 = getBracketEntry(easyCapsTournament, 11).getPlayer2();
        User user10 = getBracketEntry(easyCapsTournament, 12).getPlayer1();
        User user11 = getBracketEntry(easyCapsTournament, 12).getPlayer2();
        User user12 = getBracketEntry(easyCapsTournament, 13).getPlayer1();
        User user13 = getBracketEntry(easyCapsTournament, 13).getPlayer2();
        User user14 = getBracketEntry(easyCapsTournament, 14).getPlayer1();
        User user15 = getBracketEntry(easyCapsTournament, 14).getPlayer2();

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
        skipGame(easyCapsTournament, user1,1);
        addGameToEntry(easyCapsTournament,0,true);


        assertEquals(user1,getBracketEntry(easyCapsTournament,3).getPlayer1());
        assertEquals(user4,getBracketEntry(easyCapsTournament,3).getPlayer2());
        assertEquals(user5,getBracketEntry(easyCapsTournament,4).getPlayer1());
        assertEquals(user8,getBracketEntry(easyCapsTournament,4).getPlayer2());
        assertEquals(user9,getBracketEntry(easyCapsTournament,5).getPlayer1());
        assertEquals(user10,getBracketEntry(easyCapsTournament,5).getPlayer2());
        assertEquals(user13,getBracketEntry(easyCapsTournament,6).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,6).getPlayer2());
        assertEquals(user1,getBracketEntry(easyCapsTournament,1).getPlayer1());
        assertEquals(user5,getBracketEntry(easyCapsTournament,1).getPlayer2());
        assertEquals(user9,getBracketEntry(easyCapsTournament,2).getPlayer1());
        assertEquals(user14,getBracketEntry(easyCapsTournament,2).getPlayer2());
        assertEquals(user5,getBracketEntry(easyCapsTournament,0).getPlayer1());
        assertEquals(user9,getBracketEntry(easyCapsTournament,0).getPlayer2());


        assertEquals(user5.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
        assertTrue(easyCapsTournament.isFinished());

    }


    @Test
    public void singleEliminationGetAbove_returnsCorrectNumber() {
        int result = BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.RO_16);
        assertEquals(7, result);
    }


    @Test
    public void doubleEliminationGetAbove_returnsCorrectNumber() {
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
    public void doubleEliminationGetAboveAndEqual_returnsCorrectNumber() {
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
    public void singleEliminationGetAboveAndEquals_returnsCorrectNumber() {
        int result = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.RO_16);
        assertEquals(15, result);
    }

    @Test
    public void testIsPowerOf2() {
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_8));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_8));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_12));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_3));
    }

    @Test
    public void testGetHigherPowerOf2() {
        assertEquals(BracketEntryType.D_RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_16));
        assertEquals(BracketEntryType.D_RO_4, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_8));
        assertEquals(BracketEntryType.D_RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_12));
        assertEquals(BracketEntryType.RO_8, BracketEntryType.getHigherPowerOf2(BracketEntryType.RO_16));
    }


    private void addGameToEntry(Tournament<?> easyCapsTournament, int coord, boolean player1Wins) {
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

    private void skipGame(Tournament<?> tournament, User skippingUser, int coord) {
        EasyCapsBracketEntry entry2 = getBracketEntry(tournament, coord);
        entry2.forfeitGame(skippingUser);
        tournament.resolveAfterGame();
    }

    private EasyCapsBracketEntry getBracketEntry(Tournament<?> tournament, int coord) {
        return (EasyCapsBracketEntry) tournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == coord).findAny().get();
    }

}

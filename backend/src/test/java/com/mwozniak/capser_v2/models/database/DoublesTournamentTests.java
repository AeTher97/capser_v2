package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesTournament;
import com.mwozniak.capser_v2.models.database.tournament.doubles.TeamBridge;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Doubles Tournament Test")
public class DoublesTournamentTests {

    @Test
    public void singleEliminationTournamentCreation_shouldCreateRightNumberOfEntries() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.RO_16);


        assertEquals(15, doublesTournament.getBracketEntries().size());
    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries14() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_8);


        assertEquals(14, doublesTournament.getBracketEntries().size());


    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries30() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_16);


        assertEquals(30, doublesTournament.getBracketEntries().size());


    }


    @Test
    public void doubleElimination_seedsCorrectly() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_8);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats team = new TeamWithStats();
            team.setId(UUID.randomUUID());
            team.setName("a" + i);
            TeamBridge teamBridge = new TeamBridge(team);
            players.add(teamBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();

        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getTeam2());

    }

    @Test
    public void singleElimination_seedsCorrectly() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.RO_16);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();

        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 10).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 10).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 7).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 14).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 14).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get()).getTeam2());

    }

    @Test
    public void singleElimination_seedsCorrectly8() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.RO_8);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();

        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 6).findAny().get()).getTeam2());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getTeam1());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get()).getTeam2());

    }

    @Test
    public void singleElimination_resolveByesCorrectly8() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.RO_8);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();
        doublesTournament.resolveByes();

        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getTeam2());
        assertTrue(doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 4).findAny().get().isBye());

    }

    @Test
    public void doubleElimination_resolveByesCorrectly8() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_8);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();
        doublesTournament.resolveByes();

        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getTeam2());
        assertTrue(doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get().isBye());

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

//    @Test
//    public void singleElimination_progressesCorrectly8() {
//        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
//        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
//        easyCapsTournament.setTournamentName("Test");
//        easyCapsTournament.setSeedType(SeedType.RANDOM);
//        easyCapsTournament.setSize(BracketEntryType.RO_8);
//
//        List<UserBridge> players = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID());
//            user.setUsername("a" + i);
//            UserBridge userBridge = new UserBridge(user);
//            players.add(userBridge);
//        }
//
//        easyCapsTournament.getPlayers().addAll(players);
//
//        easyCapsTournament.seedPlayers();
//        easyCapsTournament.resolveByes();
//
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
//
//        addGameToEntry(easyCapsTournament, 3, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 6, true);
//        addGameToEntry(easyCapsTournament, 1, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());
//
//        assertFalse(easyCapsTournament.isFinished());
//
//        addGameToEntry(easyCapsTournament, 0, true);
//
//        assertTrue(easyCapsTournament.isFinished());
//
//
//    }

    @Test
    public void doubleElimination_progressesCorrectly16() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_16);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setId(UUID.randomUUID());
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();
        doublesTournament.resolveByes();

        addGameToEntry(doublesTournament, 8, true);
        addGameToEntry(doublesTournament, 15, true);
        addGameToEntry(doublesTournament, 4, true);
        addGameToEntry(doublesTournament, 1006, true);
        addGameToEntry(doublesTournament, 1, false);
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1009).findAny().get()).getTeam1());

        addGameToEntry(doublesTournament, 1001, false);
        addGameToEntry(doublesTournament, 1000, false);
        addGameToEntry(doublesTournament, 0, true);

        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getTeam2());


        assertTrue(doublesTournament.isFinished());

    }

//    @Test
//    public void doubleElimination_progressesCorrectly8() {
//        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
//        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
//        easyCapsTournament.setTournamentName("Test");
//        easyCapsTournament.setSeedType(SeedType.RANDOM);
//        easyCapsTournament.setSize(BracketEntryType.D_RO_8);
//
//        List<UserBridge> players = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID());
//            user.setUsername("a" + i);
//            UserBridge userBridge = new UserBridge(user);
//            players.add(userBridge);
//        }
//
//        easyCapsTournament.getPlayers().addAll(players);
//
//        easyCapsTournament.seedPlayers();
//        easyCapsTournament.resolveByes();
//
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
//
//        addGameToEntry(easyCapsTournament, 4, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 7, true);
//        addGameToEntry(easyCapsTournament, 2, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());
//
//        assertFalse(easyCapsTournament.isFinished());
//
//        addGameToEntry(easyCapsTournament, 1, true);
//
//        assertNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 1002, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 1001, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 1000, true);
//
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
//        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());
//
//        addGameToEntry(easyCapsTournament, 0, true);
//
//        assertTrue(easyCapsTournament.isFinished());
//
//
//    }
//
//    @Test
//    public void doubleElimination_progressesCorrectlyWithMemory8() {
//        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
//        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
//        easyCapsTournament.setTournamentName("Test");
//        easyCapsTournament.setSeedType(SeedType.RANDOM);
//        easyCapsTournament.setSize(BracketEntryType.D_RO_8);
//
//        List<UserBridge> players = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID());
//            user.setUsername("a" + i);
//            UserBridge userBridge = new UserBridge(user);
//            players.add(userBridge);
//        }
//
//        easyCapsTournament.getPlayers().addAll(players);
//
//        easyCapsTournament.seedPlayers();
//        easyCapsTournament.resolveByes();
//
//        User user1 = getBracketEntry(easyCapsTournament, 4).getPlayer1();
//        User user2 = getBracketEntry(easyCapsTournament, 4).getPlayer2();
//        User user3 = getBracketEntry(easyCapsTournament, 5).getPlayer1();
//        User user4 = getBracketEntry(easyCapsTournament, 7).getPlayer1();
//        User user5 = getBracketEntry(easyCapsTournament, 7).getPlayer2();
//
//        addGameToEntry(easyCapsTournament, 4, true);
//        skipGame(easyCapsTournament, user5, 7);
//        addGameToEntry(easyCapsTournament, 2, true);
//        addGameToEntry(easyCapsTournament, 1002, false);
//        addGameToEntry(easyCapsTournament, 1, true);
//        skipGame(easyCapsTournament, user2, 1000);
//        addGameToEntry(easyCapsTournament, 0, true);
//
//        assertEquals(user2, getBracketEntry(easyCapsTournament, 1004).getPlayer1());
//        assertEquals(user3, getBracketEntry(easyCapsTournament, 1002).getPlayer1());
//        assertEquals(user2, getBracketEntry(easyCapsTournament, 1002).getPlayer2());
//        assertEquals(user2, getBracketEntry(easyCapsTournament, 1001).getPlayer1());
//        assertTrue(getBracketEntry(easyCapsTournament,1001).isBye());
//        assertEquals(user4, getBracketEntry(easyCapsTournament, 1000).getPlayer1());
//        assertEquals(user2, getBracketEntry(easyCapsTournament, 1000).getPlayer2());
//        assertEquals(user1, getBracketEntry(easyCapsTournament, 0).getPlayer1());
//        assertEquals(user4, getBracketEntry(easyCapsTournament, 0).getPlayer2());
//        assertEquals(user1.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
//        assertTrue(easyCapsTournament.isFinished());
//
//    }
//
    @Test
    public void doubleElimination_progressesCorrectlyWithMemory16() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_16);

        List<TeamBridge> players = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            TeamWithStats user = new TeamWithStats();
            user.setId(UUID.randomUUID());
            user.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(user);
            players.add(userBridge);
        }

        doublesTournament.getTeams().addAll(players);

        doublesTournament.seedPlayers();
        doublesTournament.resolveByes();

        TeamWithStats user1 = getBracketEntry(doublesTournament, 8).getTeam1();
        TeamWithStats user2 = getBracketEntry(doublesTournament, 8).getTeam2();
        TeamWithStats user3 = getBracketEntry(doublesTournament, 9).getTeam1();
        TeamWithStats user4 = getBracketEntry(doublesTournament, 9).getTeam2();
        TeamWithStats user5 = getBracketEntry(doublesTournament, 10).getTeam1();
        TeamWithStats user6 = getBracketEntry(doublesTournament, 10).getTeam2();
        TeamWithStats user7 = getBracketEntry(doublesTournament, 11).getTeam1();
        TeamWithStats user8 = getBracketEntry(doublesTournament, 11).getTeam2();
        TeamWithStats user9 = getBracketEntry(doublesTournament, 12).getTeam2();
        TeamWithStats user10 = getBracketEntry(doublesTournament, 13).getTeam1();
        TeamWithStats user11 = getBracketEntry(doublesTournament, 13).getTeam2();
        TeamWithStats user12 = getBracketEntry(doublesTournament, 14).getTeam1();
        TeamWithStats user13 = getBracketEntry(doublesTournament, 14).getTeam2();
        TeamWithStats user14 = getBracketEntry(doublesTournament, 15).getTeam1();
        TeamWithStats user15 = getBracketEntry(doublesTournament, 15).getTeam2();

        addGameToEntry(doublesTournament, 8, true);
        addGameToEntry(doublesTournament, 9, false);
        addGameToEntry(doublesTournament, 10, true);
        skipGame(doublesTournament, user7, 11);
        addGameToEntry(doublesTournament, 13, true);
        addGameToEntry(doublesTournament, 14, false);
        addGameToEntry(doublesTournament, 15, true);
        addGameToEntry(doublesTournament, 4, true);
        addGameToEntry(doublesTournament, 5, true);
        addGameToEntry(doublesTournament, 6, true);
        addGameToEntry(doublesTournament, 7, false);
        addGameToEntry(doublesTournament, 3, true);
        skipGame(doublesTournament, user1,2);
        skipGame(doublesTournament, user5,1);
        addGameToEntry(doublesTournament, 1010, true);
        addGameToEntry(doublesTournament, 1013, false);
        addGameToEntry(doublesTournament, 1006, false);
        addGameToEntry(doublesTournament, 1007, true);
        addGameToEntry(doublesTournament, 1008, false);
        addGameToEntry(doublesTournament, 1009, true);
        addGameToEntry(doublesTournament, 1004, false);
        skipGame(doublesTournament, user11,1005);
        addGameToEntry(doublesTournament, 1003, true);
        addGameToEntry(doublesTournament, 1001, false);
        addGameToEntry(doublesTournament, 0, true);

        assertEquals(user1,getBracketEntry(doublesTournament,4).getTeam1());
        assertEquals(user4,getBracketEntry(doublesTournament,4).getTeam2());
        assertEquals(user5,getBracketEntry(doublesTournament,5).getTeam1());
        assertEquals(user8,getBracketEntry(doublesTournament,5).getTeam2());
        assertEquals(user9,getBracketEntry(doublesTournament,6).getTeam1());
        assertEquals(user10,getBracketEntry(doublesTournament,6).getTeam2());
        assertEquals(user13,getBracketEntry(doublesTournament,7).getTeam1());
        assertEquals(user14,getBracketEntry(doublesTournament,7).getTeam2());
        assertEquals(user1,getBracketEntry(doublesTournament,2).getTeam1());
        assertEquals(user5,getBracketEntry(doublesTournament,2).getTeam2());
        assertEquals(user9,getBracketEntry(doublesTournament,3).getTeam1());
        assertEquals(user14,getBracketEntry(doublesTournament,3).getTeam2());
        assertEquals(user5,getBracketEntry(doublesTournament,1).getTeam1());
        assertEquals(user9,getBracketEntry(doublesTournament,1).getTeam2());
        assertEquals(user9,getBracketEntry(doublesTournament,0).getTeam1());

        assertEquals(user2,getBracketEntry(doublesTournament,1010).getTeam1());
        assertEquals(user3,getBracketEntry(doublesTournament,1010).getTeam2());

        assertNull(getBracketEntry(doublesTournament,1011).getTeam2());
        assertEquals(user6,getBracketEntry(doublesTournament,1011).getTeam1());
        assertTrue(getBracketEntry(doublesTournament,1011).isBye());

        assertNull(getBracketEntry(doublesTournament,1012).getTeam1());
        assertEquals(user11,getBracketEntry(doublesTournament,1012).getTeam2());
        assertTrue(getBracketEntry(doublesTournament,1012).isBye());

        assertEquals(user12,getBracketEntry(doublesTournament,1013).getTeam1());
        assertEquals(user15,getBracketEntry(doublesTournament,1013).getTeam2());

        assertEquals(user4,getBracketEntry(doublesTournament,1006).getTeam1());
        assertEquals(user2,getBracketEntry(doublesTournament,1006).getTeam2());

        assertEquals(user8,getBracketEntry(doublesTournament,1007).getTeam1());
        assertEquals(user6,getBracketEntry(doublesTournament,1007).getTeam2());

        assertEquals(user10,getBracketEntry(doublesTournament,1008).getTeam1());
        assertEquals(user11,getBracketEntry(doublesTournament,1008).getTeam2());

        assertEquals(user13,getBracketEntry(doublesTournament,1009).getTeam1());
        assertEquals(user15,getBracketEntry(doublesTournament,1009).getTeam2());

        assertEquals(user2,getBracketEntry(doublesTournament,1004).getTeam1());
        assertEquals(user8,getBracketEntry(doublesTournament,1004).getTeam2());

        assertEquals(user11,getBracketEntry(doublesTournament,1005).getTeam1());
        assertEquals(user13,getBracketEntry(doublesTournament,1005).getTeam2());

        assertNull(getBracketEntry(doublesTournament,1002).getTeam1());
        assertEquals(user8,getBracketEntry(doublesTournament,1002).getTeam2());
        assertTrue(getBracketEntry(doublesTournament,1002).isBye());

        assertEquals(user14,getBracketEntry(doublesTournament,1003).getTeam1());
        assertEquals(user13,getBracketEntry(doublesTournament,1003).getTeam2());

        assertEquals(user8,getBracketEntry(doublesTournament,1001).getTeam1());
        assertEquals(user14,getBracketEntry(doublesTournament,1001).getTeam2());

        assertNull(getBracketEntry(doublesTournament,1000).getTeam1());
        assertEquals(user14,getBracketEntry(doublesTournament,1000).getTeam2());
        assertTrue(getBracketEntry(doublesTournament,1000).isBye());

        assertEquals(user9,getBracketEntry(doublesTournament,0).getTeam1());
        assertEquals(user14,getBracketEntry(doublesTournament,0).getTeam2());

        assertEquals(user9.getId(), getBracketEntry(doublesTournament, 0).getGame().getWinnerId());
        assertTrue(doublesTournament.isFinished());

    }
//
//    @Test
//    public void singleElimination_progressesCorrectlyWithMemory16() {
//        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
//        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
//        easyCapsTournament.setTournamentName("Test");
//        easyCapsTournament.setSeedType(SeedType.RANDOM);
//        easyCapsTournament.setSize(BracketEntryType.RO_16);
//
//        List<UserBridge> players = new ArrayList<>();
//
//        for (int i = 0; i < 15; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID());
//            user.setUsername("a" + i);
//            UserBridge userBridge = new UserBridge(user);
//            players.add(userBridge);
//        }
//
//        easyCapsTournament.getPlayers().addAll(players);
//
//        easyCapsTournament.seedPlayers();
//        easyCapsTournament.resolveByes();
//
//        User user1 = getBracketEntry(easyCapsTournament, 7).getPlayer1();
//        User user2 = getBracketEntry(easyCapsTournament, 7).getPlayer2();
//        User user3 = getBracketEntry(easyCapsTournament, 8).getPlayer1();
//        User user4 = getBracketEntry(easyCapsTournament, 8).getPlayer2();
//        User user5 = getBracketEntry(easyCapsTournament, 9).getPlayer1();
//        User user6 = getBracketEntry(easyCapsTournament, 9).getPlayer2();
//        User user7 = getBracketEntry(easyCapsTournament, 10).getPlayer1();
//        User user8 = getBracketEntry(easyCapsTournament, 10).getPlayer2();
//        User user9 = getBracketEntry(easyCapsTournament, 11).getPlayer2();
//        User user10 = getBracketEntry(easyCapsTournament, 12).getPlayer1();
//        User user11 = getBracketEntry(easyCapsTournament, 12).getPlayer2();
//        User user12 = getBracketEntry(easyCapsTournament, 13).getPlayer1();
//        User user13 = getBracketEntry(easyCapsTournament, 13).getPlayer2();
//        User user14 = getBracketEntry(easyCapsTournament, 14).getPlayer1();
//        User user15 = getBracketEntry(easyCapsTournament, 14).getPlayer2();
//
//        addGameToEntry(easyCapsTournament, 7, true);
//        addGameToEntry(easyCapsTournament, 8, false);
//        addGameToEntry(easyCapsTournament, 9, true);
//        skipGame(easyCapsTournament, user7, 10);
//        addGameToEntry(easyCapsTournament, 12, true);
//        addGameToEntry(easyCapsTournament, 13, false);
//        addGameToEntry(easyCapsTournament, 14, true);
//        addGameToEntry(easyCapsTournament, 3, true);
//        addGameToEntry(easyCapsTournament, 4, true);
//        addGameToEntry(easyCapsTournament, 5, true);
//        addGameToEntry(easyCapsTournament, 6, false);
//        addGameToEntry(easyCapsTournament, 2, true);
//        skipGame(easyCapsTournament, user1,1);
//        addGameToEntry(easyCapsTournament,0,true);
//
//
//        assertEquals(user1,getBracketEntry(easyCapsTournament,3).getPlayer1());
//        assertEquals(user4,getBracketEntry(easyCapsTournament,3).getPlayer2());
//        assertEquals(user5,getBracketEntry(easyCapsTournament,4).getPlayer1());
//        assertEquals(user8,getBracketEntry(easyCapsTournament,4).getPlayer2());
//        assertEquals(user9,getBracketEntry(easyCapsTournament,5).getPlayer1());
//        assertEquals(user10,getBracketEntry(easyCapsTournament,5).getPlayer2());
//        assertEquals(user13,getBracketEntry(easyCapsTournament,6).getPlayer1());
//        assertEquals(user14,getBracketEntry(easyCapsTournament,6).getPlayer2());
//        assertEquals(user1,getBracketEntry(easyCapsTournament,1).getPlayer1());
//        assertEquals(user5,getBracketEntry(easyCapsTournament,1).getPlayer2());
//        assertEquals(user9,getBracketEntry(easyCapsTournament,2).getPlayer1());
//        assertEquals(user14,getBracketEntry(easyCapsTournament,2).getPlayer2());
//        assertEquals(user5,getBracketEntry(easyCapsTournament,0).getPlayer1());
//        assertEquals(user9,getBracketEntry(easyCapsTournament,0).getPlayer2());
//
//
//        assertEquals(user5.getId(), getBracketEntry(easyCapsTournament, 0).getGame().getWinner());
//        assertTrue(easyCapsTournament.isFinished());
//
//    }
//

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


    private void addGameToEntry(DoublesTournament doublesTournament, int coord, boolean player1Wins) {
        DoublesBracketEntry entry2 = getBracketEntry(doublesTournament, coord);

        DoublesGame game2 = new DoublesGame();
        game2.setTeam1DatabaseId(entry2.getTeam1().getId());
        game2.setTeam2DatabaseId(entry2.getTeam2().getId());
        if (player1Wins) {
            game2.setWinnerId(entry2.getTeam1().getId());
        } else {
            game2.setWinnerId(entry2.getTeam2().getId());
        }

        entry2.setGame(game2);
        entry2.setFinal(true);

        doublesTournament.resolveAfterGame();
    }

    private void skipGame(DoublesTournament tournament, TeamWithStats skippingUser, int coord) {
        DoublesBracketEntry entry2 = getBracketEntry(tournament, coord);
        entry2.forfeitGame(skippingUser);
        tournament.resolveAfterGame();
    }

    private DoublesBracketEntry getBracketEntry(DoublesTournament tournament, int coord) {
        return (DoublesBracketEntry) tournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == coord).findAny().get();
    }

}

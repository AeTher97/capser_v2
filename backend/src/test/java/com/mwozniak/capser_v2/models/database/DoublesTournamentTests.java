package com.mwozniak.capser_v2.models.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.game.team.DoublesGame;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesTournament;
import com.mwozniak.capser_v2.models.database.tournament.doubles.TeamBridge;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import com.mwozniak.capser_v2.models.dto.TeamGameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Doubles Tournament Test")
class DoublesTournamentTests {

    @Test
    void singleEliminationTournamentCreation_shouldCreateRightNumberOfEntries() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.RO_16);

        assertEquals(15, doublesTournament.getBracketEntries().size());
    }


    @Test
    void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries14() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_8);

        assertEquals(14, doublesTournament.getBracketEntries().size());
    }


    @Test
    void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries30() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_16);


        assertEquals(30, doublesTournament.getBracketEntries().size());


    }


    @Test
    void doubleElimination_seedsCorrectly() {
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
    void singleElimination_seedsCorrectly() {
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
    void singleElimination_seedsCorrectly8() {
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
    void singleElimination_resolveByesCorrectly8() {
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
    void doubleElimination_resolveByesCorrectly8() {
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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 5).findAny().get().isBye());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

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

        assertNotNull(((EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 3).findAny().get()).getPlayer2());
        assertTrue(easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 8).findAny().get().isBye());

    }


    @Test
    void doubleElimination_progressesCorrectly16() {
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

        addGameToEntry(doublesTournament, 1001, false);
        addGameToEntry(doublesTournament, 1000, false);
        addGameToEntry(doublesTournament, 0, true);

        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getTeam1());
        assertNotNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getTeam2());
        assertNull(((DoublesBracketEntry) doublesTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1003).findAny().get()).getTeam1());


        assertTrue(doublesTournament.isFinished());
    }

    @Test
    void doubleElimination_progressesCorrectlyWithMemory16() {
        DoublesTournament doublesTournament = new DoublesTournament();
        doublesTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        doublesTournament.setTournamentName("Test");
        doublesTournament.setSeedType(SeedType.RANDOM);
        doublesTournament.setSize(BracketEntryType.D_RO_16);

        List<TeamBridge> teams = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            TeamWithStats team = new TeamWithStats();
            team.setPlayerList(new ArrayList<>());
            team.setId(UUID.randomUUID());
            team.setName("a" + i);
            TeamBridge userBridge = new TeamBridge(team);
            teams.add(userBridge);
        }

        doublesTournament.getTeams().addAll(teams);

        doublesTournament.seedPlayers();
        doublesTournament.resolveByes();

        TeamWithStats user0 = teams.get(0).getTeam();
        TeamWithStats user1 = teams.get(1).getTeam();
        TeamWithStats user2 = teams.get(2).getTeam();
        TeamWithStats user3 = teams.get(3).getTeam();
        TeamWithStats user4 = teams.get(4).getTeam();
        TeamWithStats user5 = teams.get(5).getTeam();
        TeamWithStats user6 = teams.get(6).getTeam();
        TeamWithStats user7 = teams.get(7).getTeam();
        TeamWithStats user8 = teams.get(8).getTeam();
        TeamWithStats user9 = teams.get(9).getTeam();
        TeamWithStats user10 = teams.get(10).getTeam();
        TeamWithStats user11 = teams.get(11).getTeam();
        TeamWithStats user12 = teams.get(12).getTeam();
        TeamWithStats user13 = teams.get(13).getTeam();
        TeamWithStats user14 = teams.get(14).getTeam();

        addGameToEntry(doublesTournament, 8, true);
        addGameToEntry(doublesTournament, 9, false);
        addGameToEntry(doublesTournament, 10, true);
        skipGame(doublesTournament, user12, 11);
        addGameToEntry(doublesTournament, 13, true);
        addGameToEntry(doublesTournament, 14, false);
        addGameToEntry(doublesTournament, 15, true);
        addGameToEntry(doublesTournament, 4, true);
        addGameToEntry(doublesTournament, 5, true);
        addGameToEntry(doublesTournament, 6, true);
        addGameToEntry(doublesTournament, 7, false);
        addGameToEntry(doublesTournament, 3, true);
        skipGame(doublesTournament, user8, 2);
        skipGame(doublesTournament, user0, 1);
        addGameToEntry(doublesTournament, 1010, true);
        addGameToEntry(doublesTournament, 1013, false);
        addGameToEntry(doublesTournament, 1006, false);
        addGameToEntry(doublesTournament, 1007, true);
        addGameToEntry(doublesTournament, 1008, false);
        addGameToEntry(doublesTournament, 1009, true);
        addGameToEntry(doublesTournament, 1004, false);
        skipGame(doublesTournament, user10,1005);
        addGameToEntry(doublesTournament, 1002, true);
        addGameToEntry(doublesTournament, 1001, false);
        addGameToEntry(doublesTournament, 0, true);

        assertEquals(user0, getBracketEntry(doublesTournament, 4).getTeam1());
        assertEquals(user6, getBracketEntry(doublesTournament, 4).getTeam2());

        assertEquals(user8, getBracketEntry(doublesTournament, 5).getTeam1());
        assertEquals(user14, getBracketEntry(doublesTournament, 5).getTeam2());

        assertEquals(user13, getBracketEntry(doublesTournament, 6).getTeam1());
        assertEquals(user11, getBracketEntry(doublesTournament, 6).getTeam2());

        assertEquals(user5, getBracketEntry(doublesTournament, 7).getTeam1());
        assertEquals(user3, getBracketEntry(doublesTournament, 7).getTeam2());

        assertEquals(user0, getBracketEntry(doublesTournament, 2).getTeam1());
        assertEquals(user8, getBracketEntry(doublesTournament, 2).getTeam2());

        assertEquals(user13, getBracketEntry(doublesTournament, 3).getTeam1());
        assertEquals(user3, getBracketEntry(doublesTournament, 3).getTeam2());

        assertEquals(user0, getBracketEntry(doublesTournament, 1).getTeam1());
        assertEquals(user13, getBracketEntry(doublesTournament, 1).getTeam2());

        assertEquals(user13, getBracketEntry(doublesTournament, 0).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 0).getTeam2());

        assertEquals(user1, getBracketEntry(doublesTournament, 1010).getTeam1());
        assertEquals(user7, getBracketEntry(doublesTournament, 1010).getTeam2());

        assertEquals(user9, getBracketEntry(doublesTournament, 1011).getTeam1());
        assertNull(getBracketEntry(doublesTournament, 1011).getTeam2());

        assertTrue(getBracketEntry(doublesTournament, 1011).isBye());

        assertNull(getBracketEntry(doublesTournament, 1012).getTeam1());
        assertEquals(user10, getBracketEntry(doublesTournament, 1012).getTeam2());
        assertTrue(getBracketEntry(doublesTournament, 1012).isBye());

        assertEquals(user4, getBracketEntry(doublesTournament, 1013).getTeam1());
        assertEquals(user2, getBracketEntry(doublesTournament, 1013).getTeam2());

        assertEquals(user6, getBracketEntry(doublesTournament, 1006).getTeam1());
        assertEquals(user1, getBracketEntry(doublesTournament, 1006).getTeam2());

        assertEquals(user14, getBracketEntry(doublesTournament, 1007).getTeam1());
        assertEquals(user9, getBracketEntry(doublesTournament, 1007).getTeam2());

        assertEquals(user11, getBracketEntry(doublesTournament, 1008).getTeam1());
        assertEquals(user10, getBracketEntry(doublesTournament, 1008).getTeam2());

        assertEquals(user5, getBracketEntry(doublesTournament, 1009).getTeam1());
        assertEquals(user2, getBracketEntry(doublesTournament, 1009).getTeam2());

        assertEquals(user1, getBracketEntry(doublesTournament, 1004).getTeam1());
        assertEquals(user14, getBracketEntry(doublesTournament, 1004).getTeam2());

        assertEquals(user10, getBracketEntry(doublesTournament, 1005).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 1005).getTeam2());

        assertEquals(user3, getBracketEntry(doublesTournament, 1002).getTeam1());
        assertEquals(user14, getBracketEntry(doublesTournament, 1002).getTeam2());

        assertNull(getBracketEntry(doublesTournament, 1003).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 1003).getTeam2());
        assertTrue(getBracketEntry(doublesTournament, 1003).isBye());

        assertEquals(user3, getBracketEntry(doublesTournament, 1001).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 1001).getTeam2());

        assertNull(getBracketEntry(doublesTournament, 1000).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 1000).getTeam2());
        assertTrue(getBracketEntry(doublesTournament, 1000).isBye());

        assertEquals(user13, getBracketEntry(doublesTournament, 0).getTeam1());
        assertEquals(user5, getBracketEntry(doublesTournament, 0).getTeam2());

        assertEquals(user13.getId(), getBracketEntry(doublesTournament, 0).getGame().getWinner());
        assertTrue(doublesTournament.isFinished());

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


    private void addGameToEntry(DoublesTournament doublesTournament, int coord, boolean player1Wins) {
        DoublesBracketEntry entry2 = getBracketEntry(doublesTournament, coord);

        TeamGameDto dto = new TeamGameDto();
        dto.setGameMode(GameMode.SUDDEN_DEATH);
        dto.setPlayerStatsDtos(new ArrayList<>());
        dto.setTeam1(UUID.randomUUID());
        dto.setTeam2(UUID.randomUUID());
        dto.setTeam1Players(Collections.singletonList(UUID.randomUUID()));
        dto.setTeam2Players(Collections.singletonList(UUID.randomUUID()));
        DoublesGame game2 = new DoublesGame();
        game2.fillCommonProperties(dto);
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

    private void printTournamentJson(Tournament tournament) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(tournament));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

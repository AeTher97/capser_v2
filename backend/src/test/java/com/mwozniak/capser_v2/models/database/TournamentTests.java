package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.tournament.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentTests {

    @Test
    public void singleEliminationTournamentCreation_shouldCreateRightNumberOfEntries(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);


        assertEquals(15,easyCapsTournament.getBracketEntries().size());
    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries14(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);


        assertEquals(14,easyCapsTournament.getBracketEntries().size());


    }


    @Test
    public void doubleEliminationTournamentCreation_shouldCreateRightNumberOfEntries30(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_16);


        assertEquals(30,easyCapsTournament.getBracketEntries().size());


    }



    @Test
    public void doubleElimination_seedsCorrectly(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a"+i);
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
    public void singleElimination_seedsCorrectly(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void singleElimination_seedsCorrectly8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void singleElimination_resolveByesCorrectly8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void doubleElimination_resolveByesCorrectly8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void doubleElimination_resolveByesCorrectlyFinal8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<2;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void singleElimination_resolveByesCorrectly16(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_16);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("a"+i);
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
    public void singleElimination_progressesCorrectly8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.SINGLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a"+i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());

        addGameToEntry(easyCapsTournament,3,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,6,true);
        addGameToEntry(easyCapsTournament,1,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament,0,true);

        assertTrue(easyCapsTournament.isFinished());



    }

    @Test
    public void doubleElimination_progressesCorrectly8(){
        Tournament<EasyCapsGame> easyCapsTournament = new EasyCapsTournament();
        easyCapsTournament.setTournamentType(TournamentType.DOUBLE_ELIMINATION);
        easyCapsTournament.setTournamentName("Test");
        easyCapsTournament.setSeedType(SeedType.RANDOM);
        easyCapsTournament.setSize(BracketEntryType.D_RO_8);

        List<UserBridge> players = new ArrayList<>();

        for(int i=0;i<5;i++){
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername("a"+i);
            UserBridge userBridge = new UserBridge(user);
            players.add(userBridge);
        }

        easyCapsTournament.getPlayers().addAll(players);

        easyCapsTournament.seedPlayers();
        easyCapsTournament.resolveByes();

        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());

        addGameToEntry(easyCapsTournament,4,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 2).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,7,true);
        addGameToEntry(easyCapsTournament,2,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1).findAny().get()).getPlayer2());

        assertFalse(easyCapsTournament.isFinished());

        addGameToEntry(easyCapsTournament,1,true);

        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,1002,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
        assertNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,1003,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1001).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,1001,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 1000).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,1000,true);

        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer1());
        assertNotNull(((EasyCapsBracketEntry)easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == 0).findAny().get()).getPlayer2());

        addGameToEntry(easyCapsTournament,0,true);

        assertTrue(easyCapsTournament.isFinished());



    }


    @Test
    public void singleEliminationGetAbove_returnsCorrectNumber(){
        int result = BracketEntryType.getSingleEliminationCountAbove(BracketEntryType.RO_16);
        assertEquals(7,result);
    }


    @Test
    public void doubleEliminationGetAbove_returnsCorrectNumber(){
        assertEquals(1,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_2,true));
        assertEquals(2,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_4,true));
        assertEquals(4,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_8,true));
        assertEquals(8,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_12,true));
        assertEquals(0,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_2,false));
        assertEquals(1,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_3,false));
        assertEquals(2,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_4,false));
        assertEquals(4,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_6,false));
        assertEquals(6,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_8,false));
        assertEquals(10,BracketEntryType.getDoubleEliminationCountAbove(BracketEntryType.D_RO_12,false));
    }

    @Test
    public void doubleEliminationGetAboveAndEqual_returnsCorrectNumber(){
        assertEquals(2,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_2,true));
        assertEquals(4,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_4,true));
        assertEquals(8,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_8,true));
        assertEquals(16,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_16,true));
        assertEquals(1,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_2,false));
        assertEquals(2,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_3,false));
        assertEquals(4,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_4,false));
        assertEquals(6,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_6,false));
        assertEquals(10,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_8,false));
        assertEquals(14,BracketEntryType.getDoubleEliminationCountAboveAndEqual(BracketEntryType.D_RO_12,false));
    }


    @Test
    public void singleEliminationGetAboveAndEquals_returnsCorrectNumber(){
        int result = BracketEntryType.getSingleEliminationCountAboveAndEqual(BracketEntryType.RO_16);
        assertEquals(15,result);
    }

    @Test
    public void testIsPowerOf2(){
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.RO_8));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_4));
        assertTrue(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_8));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_12));
        assertFalse(BracketEntryType.isPowerOf2(BracketEntryType.D_RO_3));
    }

    @Test
    public void testGetHigherPowerOf2(){
        assertEquals(BracketEntryType.D_RO_8,BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_16));
        assertEquals(BracketEntryType.D_RO_4,BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_8));
        assertEquals(BracketEntryType.D_RO_8,BracketEntryType.getHigherPowerOf2(BracketEntryType.D_RO_12));
        assertEquals(BracketEntryType.RO_8,BracketEntryType.getHigherPowerOf2(BracketEntryType.RO_16));
    }


    private void addGameToEntry(Tournament<?> easyCapsTournament, int coord, boolean player1Wins){
        EasyCapsBracketEntry entry2 = (EasyCapsBracketEntry) easyCapsTournament.getBracketEntries().stream().filter(bracketEntry -> bracketEntry.getCoordinate() == coord).findAny().get();

        EasyCapsGame game2 = new EasyCapsGame();
        game2.setPlayer1(entry2.getPlayer1().getId());
        game2.setPlayer2(entry2.getPlayer2().getId());
        if(player1Wins) {
            game2.setWinner(entry2.getPlayer1().getId());
        } else {
            game2.setWinner(entry2.getPlayer2().getId());
        }

        entry2.setGame(game2);
        entry2.setFinal(true);

        easyCapsTournament.resolveAfterGame();
    }

}

package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@MappedSuperclass
public abstract class AbstractSinglesTournament<T extends AbstractSinglesGame> extends Tournament<T> {


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    protected List<UserBridge> players;



    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"userSinglesStats","userEasyStats","userUnrankedStats","userDoublesStats","teams","lastSeen","lastGame","role"})
    private UserBridge winner;

    @Override
    protected void populateEntryList() {
        players = new ArrayList<>();
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            List<AbstractSinglesBracketEntry> abstractBracketEntries = new ArrayList<>();
            int number = getCoordinatesIdsAboveAndEqual(size);
            for (int i = 0; i < number; i++) {
                AbstractSinglesBracketEntry abstractBracketEntry = createBracketEntry();
                abstractBracketEntry.setCoordinate(i);
                abstractBracketEntries.add(abstractBracketEntry);
            }
            abstractBracketEntries.get(0).setBracketEntryType(BracketEntryType.RO_2);
            setBracketEntries(abstractBracketEntries);
        }
    }

    @Override
    public void doSeedPlayers() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            if(seedType.equals(SeedType.RANDOM)) {
                int playersCount = players.size();
                int lastRowCoordinateStart = getCoordinatesIdsAbove(size);
                List<User> randomPlayerList = players.stream().map(UserBridge::getUser).collect(Collectors.toList());
                Collections.shuffle(randomPlayerList);
                for (int i = 0; i < size.getValue() / 2; i += 2) {
                    getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                    AbstractSinglesBracketEntry topEntry = getBracketEntries().get(lastRowCoordinateStart + i / 2);
                    AbstractSinglesBracketEntry bottomEntry = getBracketEntries().get(lastRowCoordinateStart + size.getValue() / 2 - 1 - i / 2);
                    if (i * 2 < playersCount) {
                        topEntry.setPlayer1(randomPlayerList.get(i * 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 1 < playersCount) {
                        bottomEntry.setPlayer2(randomPlayerList.get(i * 2 + 1));
                    } else {
                        bottomEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 2 < playersCount) {
                        topEntry.setPlayer2(randomPlayerList.get(i * 2 + 2));
                    } else {
                        topEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                    if (i * 2 + 3 < playersCount) {
                        bottomEntry.setPlayer1(randomPlayerList.get(i * 2 + 3));
                    } else {
                        bottomEntry.setBye(true);
                        topEntry.setFinal(true);
                    }
                }
            } else if(seedType.equals(SeedType.ELO)){

            }
        }
    }

    @Override
    public void resolveAfterGame(){
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {

            BracketEntryType currentRow = size;
            while (!currentRow.equals(BracketEntryType.RO_2)) {
                getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                int absoluteCoord = getCoordinatesIdsAbove(currentRow);
                int higherAbsoluteCoord = getCoordinatesIdsAboveAndEqual(BracketEntryType.getHigher(currentRow)) - getCoordinatesIdsAbove(BracketEntryType.getHigher(currentRow)) -1;
                for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                    AbstractSinglesBracketEntry topEntry = getBracketEntries().get(absoluteCoord + i);
                    AbstractSinglesBracketEntry bottomEntry = getBracketEntries().get(absoluteCoord + i + 1);
                    AbstractSinglesBracketEntry higherEntry = getBracketEntries().get(higherAbsoluteCoord + i / 2);
                    if(higherEntry.isFinal()){
                        continue;
                    }

                    if (topEntry.getGame() != null) {
                        if (higherEntry.getPlayer1() == null) {
                            higherEntry.setPlayer1(topEntry.getPlayer1().getId().equals(topEntry.getGame().getWinner()) ? topEntry.getPlayer1() : topEntry.getPlayer2());
                        }
                    } else {
                        if (topEntry.isForfeited()) {
                            higherEntry.setPlayer1(topEntry.getPlayer1().getId().equals(topEntry.getForfeitedId()) ? topEntry.getPlayer2() : topEntry.getPlayer1());
                        }
                    }

                    if (bottomEntry.getGame() != null) {
                        if (higherEntry.getPlayer2() == null) {
                            higherEntry.setPlayer2(bottomEntry.getPlayer1().getId().equals(bottomEntry.getGame().getWinner()) ? bottomEntry.getPlayer1() : bottomEntry.getPlayer2());
                        }
                    } else {
                        if (bottomEntry.isForfeited()) {
                            higherEntry.setPlayer2(bottomEntry.getPlayer1().getId().equals(bottomEntry.getForfeitedId()) ? bottomEntry.getPlayer2() : bottomEntry.getPlayer1());
                        }
                    }

                    threeObjectBye(topEntry, bottomEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye());


                }
                currentRow = BracketEntryType.getHigher(currentRow);

            }
            AbstractSinglesBracketEntry entry = getBracketEntries().get(0);
            if(entry.getCoordinate() == 0){
                if (entry.getGame() != null) {
                    setFinished(true);
                    setWinner(entry.getPlayer1().getId().equals(entry.getGame().getWinner()) ? new UserBridge(entry.getPlayer1()) : new UserBridge(entry.getPlayer2()));
                } else if (entry.isForfeited()) {
                    setFinished(true);
                    setWinner(entry.getPlayer1().getId().equals(entry.getForfeitedId()) ? new UserBridge(entry.getPlayer2()) : new UserBridge(entry.getPlayer1()));
                }
            }
        }
    }

    private void threeObjectBye(AbstractSinglesBracketEntry topEntry, AbstractSinglesBracketEntry bottomEntry, AbstractSinglesBracketEntry higherEntry, User player1, User player2, User player12, User player22, boolean bye, boolean bye2) {
        if(bottomEntry.isBye()&&topEntry.isBye()){
            higherEntry.setPlayer1(player1 == null ? player2 : player1);
            higherEntry.setPlayer2(player12 == null ? player22 : player12);
            if(higherEntry.getPlayer1() == null || higherEntry.getPlayer2() == null){
                higherEntry.setBye(true);
                higherEntry.setFinal(true);
            }
        }else if (bye || bye2) {
            if (bye) {
                higherEntry.setPlayer1(player1 == null ? player2 : player1);
                if(higherEntry.getPlayer1()==null && higherEntry.getPlayer2()!=null){
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            } else {
                higherEntry.setPlayer2(player12 == null ? player22 : player12);
                if(higherEntry.getPlayer2()==null && higherEntry.getPlayer1()!=null){
                    higherEntry.setFinal(true);
                    higherEntry.setBye(true);
                }
            }
        }
    }

    @Override
    public void resolveByes() {
        if (tournamentType.equals(TournamentType.SINGLE_ELIMINATION)) {
            BracketEntryType currentRow = size;
            while (!currentRow.equals(BracketEntryType.RO_2)) {
                getBracketEntries().sort(BracketEntry.Comparators.COORDINATE);
                int absoluteCoord = getCoordinatesIdsAbove(currentRow);
                int higherAbsoluteCoord = getCoordinatesIdsAboveAndEqual(BracketEntryType.getHigher(currentRow)) - getCoordinatesIdsAbove(BracketEntryType.getHigher(currentRow)) -1;
                for (int i = 0; i < currentRow.getValue() / 2; i += 2) {
                    AbstractSinglesBracketEntry topEntry = getBracketEntries().get(absoluteCoord + i);
                    AbstractSinglesBracketEntry bottomEntry = getBracketEntries().get(absoluteCoord + i + 1);
                    topEntry.setBracketEntryType(currentRow);
                    bottomEntry.setBracketEntryType(currentRow);
                    AbstractSinglesBracketEntry higherEntry = getBracketEntries().get(higherAbsoluteCoord + i / 2);

                    threeObjectBye(bottomEntry, topEntry, higherEntry, topEntry.getPlayer1(), topEntry.getPlayer2(), bottomEntry.getPlayer1(), bottomEntry.getPlayer2(), topEntry.isBye(), bottomEntry.isBye());
                }
                currentRow = BracketEntryType.getHigher(currentRow);

            }
        }

    }

    protected abstract List<? extends AbstractSinglesBracketEntry> getBracketEntries();
    protected abstract void setBracketEntries(List<AbstractSinglesBracketEntry> entries);
    protected abstract AbstractSinglesBracketEntry createBracketEntry();
}

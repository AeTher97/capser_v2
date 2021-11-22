import React, {useState} from 'react';
import {useLatestPlayerGames, usePlayerGamesWithOpponent} from "../../data/SoloGamesData";
import GameComponent from "../game/details/GameComponent";
import GameHistoryFilters from "./GameHistoryFilters";

const GameHistory = ({userId}) => {

    const {games} = useLatestPlayerGames(userId);
    const [opponentId, setOpponentId] = useState(null);
    const [gameType, setGameType] = useState('ALL');
    const {games: filteredGames} = usePlayerGamesWithOpponent(userId, opponentId, gameType);

    console.log(filteredGames)
    return (
        <div>
            <GameHistoryFilters setOpponent={setOpponentId} selectedGameType={gameType} setGameType={setGameType}/>
            {gameType === 'ALL' && games.map(game =>
                <div key={game.id} style={{marginBottom: 10}}>
                    <GameComponent game={game} vertical={false}/>
                </div>
            )}
            {gameType !== 'ALL' && filteredGames.map(game =>
                <div key={game.id} style={{marginBottom: 10}}>
                    <GameComponent game={game} vertical={false}/>
                </div>
            )}
        </div>
    );
};

export default GameHistory;

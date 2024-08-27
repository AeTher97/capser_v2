import React, {useEffect, useState} from 'react';
import {useLatestPlayerGames, usePlayerGamesWithOpponent} from "../../data/SoloGamesData";
import GameComponent from "../game/details/GameComponent";
import GameHistoryFilters from "./GameHistoryFilters";
import CapserPagination from "../game/list/CapserPagination";
import {Skeleton} from "@material-ui/lab";
import {useSelector} from "react-redux";

const GameHistory = ({userId}) => {

    const [page, setPage] = useState(1);
    const {games, pages, loading: loadingAll} = useLatestPlayerGames(userId, page);
    const [opponentId, setOpponentId] = useState(null);
    const [gameType, setGameType] = useState('ALL');
    const {
        games: filteredGames,
        pages: filteredPages,
        loading: loadingWithOpponent
    } = usePlayerGamesWithOpponent(userId, opponentId, gameType, page);

    useEffect(() => {
        setPage(1);
    }, [opponentId, gameType])

    const loading = loadingAll || loadingWithOpponent;
    return (
        <div>
            <GameHistoryFilters profileOwner={userId} setOpponent={setOpponentId} selectedGameType={gameType} setGameType={setGameType}/>
            {loading && Array.from(Array(10)).map((index) => <Skeleton key={index} variant={"rect"} style={{
                width: '100%', height: 62, borderRadius: 7
                , margin: '0 0px 8px 0'
            }}/>)}
            {gameType === 'ALL' && !loading && games.map(game =>
                <div key={game.id} style={{marginBottom: 10}}>
                    <GameComponent game={game} vertical={false}/>
                </div>
            )}
            {gameType !== 'ALL' && !loading && filteredGames.map(game =>
                <div key={game.id} style={{marginBottom: 10}}>
                    <GameComponent game={game} vertical={false}/>
                </div>
            )}
            {(pages > 1 || filteredPages > 1) && <CapserPagination onChange={(newPage) => setPage(newPage)}
                                                                   currentPage={page}
                                                                   pageCount={gameType === 'ALL' ? pages : filteredPages}/>}
        </div>
    );
};

export default GameHistory;

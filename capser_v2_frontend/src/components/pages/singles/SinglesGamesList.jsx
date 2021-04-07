import React, {useState} from 'react';
import {useSinglesGames} from "../../../data/SoloGamesData";
import LoadingComponent from "../../../utils/LoadingComponent";
import GameComponent from "../../game/GameComponent";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import CapserPagination from "../../misc/CapserPagination";

export  const findPlayerStats = (game, id) => {
    return game.gamePlayerStats.find(o => o.playerId === id)
}
const SinglesGamesList = ({type, hiddenPoints = false, render = true}) => {

    const [currentPage, setPage] = useState(1);
    const small = useXtraSmallSize();

    const {games, loading, pagesNumber} = useSinglesGames(type, currentPage - 1, 10);

    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div  style={{maxWidth:800}}>
                    <div style={{padding: 5}}>
                        {!loading && render ?
                            games.map(game =>
                                <div style={{marginBottom: 10}}>
                                    <GameComponent game={game}
                                                   vertical={small}/>
                                </div>)

                            :
                            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                                <LoadingComponent/>
                            </div>}
                        {!loading && pagesNumber > 1 &&
                        <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                            <CapserPagination onNext={() => setPage(currentPage + 1)}
                                              onPrevious={() => setPage(currentPage - 1)}
                                              currentPage={currentPage}
                                              pageCount={pagesNumber}/>
                        </div>}
                    </div>
                </div>
        </div>
    );
};


export default SinglesGamesList;

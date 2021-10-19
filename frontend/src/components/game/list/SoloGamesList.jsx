import React, {useState} from 'react';
import {useSinglesGames} from "../../../data/SoloGamesData";
import GameComponent from "../details/GameComponent";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import CapserPagination from "./CapserPagination";
import Grid from "@material-ui/core/Grid";
import {Skeleton} from "@material-ui/lab";

export const findPlayerStats = (game, id) => {
    return game.gamePlayerStats.find(o => o.playerId === id)
}


const SoloGamesList = ({type, hiddenPoints = false, render = true}) => {

    const [currentPage, setPage] = useState(1);
    const small = useXtraSmallSize();

    const {games, loading, pagesNumber} = useSinglesGames(type, currentPage - 1, 10);


    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div style={{maxWidth: 800, padding: 15, flex: 1}}>
                {!loading && render &&
                <Grid container spacing={2}>{games.map(game => <Grid xs={12} item key={game.id}
                                                                     style={{padding: 0, marginBottom: 8}}>
                    <GameComponent game={game}
                                   vertical={small}/>
                </Grid>)} </Grid>}
                {loading && Array.from(Array(10)).map(() => <Skeleton variant={"rect"} style={{
                    width: '100%', height: 62, borderRadius: 7
                    , margin: '0 0px 8px 0'
                }}/>)}
                {((!loading && pagesNumber > 1) || games) &&
                <div style={{display: "flex", flexDirection: "row", justifyContent: "center", marginTop: 10}}>
                    <CapserPagination onChange={(page) => setPage(page)}
                                      currentPage={currentPage}
                                      pageCount={pagesNumber}/>
                </div>}
            </div>
        </div>
    );
};


export default SoloGamesList;

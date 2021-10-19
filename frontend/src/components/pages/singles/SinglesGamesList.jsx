import React, {useState} from 'react';
import {useSinglesGames} from "../../../data/SoloGamesData";
import LoadingComponent from "../../../utils/LoadingComponent";
import GameComponent from "../../game/GameComponent";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import CapserPagination from "../../misc/CapserPagination";
import Grid from "@material-ui/core/Grid";
import {Skeleton} from "@material-ui/lab";

export const findPlayerStats = (game, id) => {
    return game.gamePlayerStats.find(o => o.playerId === id)
}


const SinglesGamesList = ({type, hiddenPoints = false, render = true}) => {

    const [currentPage, setPage] = useState(1);
    const small = useXtraSmallSize();

    const {games, loading, pagesNumber} = useSinglesGames(type, currentPage - 1, 10);

    console.log((!loading && pagesNumber > 1) || games)
    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div style={{maxWidth: 800}}>
                <div style={{padding: 15}}>
                    {!loading && render ?
                        <Grid container spacing={2}>{games.map(game => <Grid xs={12} item key={game.id}
                                                                             style={{padding: 0, marginBottom: 8}}>
                            <GameComponent game={game}
                                           vertical={small}/>
                        </Grid>)} </Grid> :
                        <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                            <LoadingComponent/>
                        </div>}
                    {((!loading && pagesNumber > 1) || games) &&
                    <div style={{display: "flex", flexDirection: "row", justifyContent: "center", marginTop: 10}}>
                        <CapserPagination onChange={(page) => setPage(page)}
                                          currentPage={currentPage}
                                          pageCount={pagesNumber}/>
                    </div>}
                    {loading && <Skeleton style={{width: 500, height: 100}}/>}
                </div>
            </div>
        </div>
    );
};


export default SinglesGamesList;

import React, {useState} from 'react';
import {useMultipleGames} from "../../../data/MultipleGamesData";
import LoadingComponent from "../../../utils/LoadingComponent";
import GameComponent from "../../game/GameComponent";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import Grid from "@material-ui/core/Grid";
import CapserPagination from "../../misc/CapserPagination";

const MultipleGameList = ({hiddenPoints = false, type, render = true}) => {

    const small = useXtraSmallSize();

    const [currentPage, setPage] = useState(1);

    const {games, loading, pagesNumber} = useMultipleGames(type, currentPage - 1, 10);


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
                            <LoadingComponent showText/>
                        </div>}
                    {!loading && pagesNumber > 1 &&
                    <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                        <CapserPagination onChange={(page) => setPage(page)}
                                          currentPage={currentPage}
                                          pageCount={pagesNumber}/>
                    </div>}
                </div>
            </div>
        </div>
    );
};


MultipleGameList.propTypes = {};

export default MultipleGameList;

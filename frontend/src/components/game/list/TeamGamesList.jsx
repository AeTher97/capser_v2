import React, {useState} from 'react';
import {useMultipleGames} from "../../../data/MultipleGamesData";
import GameComponent from "../details/GameComponent";
import {useXtraSmallSize} from "../../../utils/SizeQuery";
import Grid from "@material-ui/core/Grid";
import CapserPagination from "./CapserPagination";
import {Skeleton} from "@material-ui/lab";

const TeamGamesList = ({hiddenPoints = false, type, render = true}) => {

    const small = useXtraSmallSize();

    const [currentPage, setPage] = useState(1);

    const {games, loading, pagesNumber} = useMultipleGames(type, currentPage - 1, 10);


    return (
        <div style={{display: "flex", justifyContent: 'center'}}>
            <div style={{maxWidth: 800, flex: 1}}>
                <div style={{padding: 15}}>
                    {!loading && render ?
                        <Grid container spacing={2}>{games.map(game => <Grid xs={12} item key={game.id}
                                                                             style={{padding: 0, marginBottom: 8}}>
                            <GameComponent game={game}
                                           vertical={small}/>
                        </Grid>)} </Grid> :
                        loading && Array.from(Array(10)).map(() => <Skeleton variant={"rect"} style={{
                            width: '100%', height: 62, borderRadius: 7
                            , margin: '0 0px 8px 0'
                        }}/>)}
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


TeamGamesList.propTypes = {};

export default TeamGamesList;

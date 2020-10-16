import React from 'react';
import PropTypes from 'prop-types';
import {useLocation, useParams} from "react-router-dom";
import {useGame} from "../../../data/SoloGamesData";

const MultipleGame = () => {
    const {gameId} = useParams();
    const location = useLocation();
    console.log()
    // const {loading,game} = useGame(location.pathname.split('/')[1], gameId)

    console.log(game)
    return (
        <div>

        </div>
    );
};

MultipleGame.propTypes = {

};

export default MultipleGame;

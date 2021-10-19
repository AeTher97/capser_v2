import React from 'react';
import SoloGameSubpage from "../../../components/game/list/SoloGameSubpage";

const UnrankedScreen = () => {

    return (
        <SoloGameSubpage type={"UNRANKED"} title={"Unranked"} showPlayers={false}/>
    )
};

export default UnrankedScreen;

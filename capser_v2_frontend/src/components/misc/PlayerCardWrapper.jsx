import React from 'react';
import {useUserData} from "../../data/UserData";
import PlayerCard from "./PlayerCard";

const PlayerCardWrapper = ({type,playerId}) => {

    const {data} = useUserData(playerId);

    return (
        <PlayerCard player={data} type={type}/>
    );
};

export default PlayerCardWrapper;
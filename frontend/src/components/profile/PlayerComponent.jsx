import React from 'react';
import {useParams} from "react-router-dom";
import UserComponent from "./UserComponent";

const PlayerComponent = () => {

    const {playerId} = useParams();

    return (
        <>
            <UserComponent id={playerId}/>
        </>
    );
};

export default PlayerComponent;

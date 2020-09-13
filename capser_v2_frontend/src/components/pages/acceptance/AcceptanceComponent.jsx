import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import useAcceptanceFetch from "../../../data/Acceptance";
import PageHeader from "../../misc/PageHeader";
import {Typography} from "@material-ui/core";
import {useGameFetch} from "../../../data/Game";

const AcceptanceComponent = props => {

    const fetchAcceptance = useAcceptanceFetch();
    const [acceptanceRequests,setAcceptanceRequests] = useState([]);



    useEffect(() => {
        fetchAcceptance().then(data => {
            console.log(data.data);
            setAcceptanceRequests(data.data)
        })
    },[])


    return (
        <div>
            <PageHeader title={"Games Accepting"}/>
            {acceptanceRequests.map(request => {
                return (
                    <div key={request.acceptanceRequest.id}>
                        <Typography>{request.acceptanceRequest.acceptanceRequestType}</Typography>
                        <Typography>{request.abstractGame.gameType}</Typography>
                </div>)
            })}
        </div>
    );
};

AcceptanceComponent.propTypes = {

};

export default AcceptanceComponent;

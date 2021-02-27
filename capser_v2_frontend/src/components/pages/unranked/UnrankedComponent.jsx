import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import {useHistory} from "react-router-dom";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "../../misc/TabPanel";
import {SinglesIcon, UnrankedIcon} from "../../../misc/icons/CapsIcons";
import {useSelector} from "react-redux";
import SinglesGamesList from "../singles/SinglesGamesList";
import SinglesPlayersList from "../singles/SinglesPlayersList";
import AddSinglesGameComponent from "../singles/AddSinglesGameComponent";
import {useHasRole} from "../../../utils/SecurityUtils";
import GamesSubpageComponent from "../singles/GamesSubpageComponent";

const UnrankedComponent = () => {

    return (
        <GamesSubpageComponent type={"UNRANKED"} title={"Unranked"}/>
    )
};

export default UnrankedComponent;

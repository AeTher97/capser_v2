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

const UnrankedComponent = () => {

    const [currentTab, setCurrentTab] = useState(0);

    const {isAuthenticated} = useSelector(state => state.auth)

    const history = useHistory();


    const handleTabChange = (e, value) => {
        if (value === 2 && !isAuthenticated) {
            history.push('/login')
        }
        setCurrentTab(value);
    }

    return (<div>
        <PageHeader title={"Unranked"} icon={<UnrankedIcon fontSize={"large"}/>} noSpace/>

        <Tabs value={currentTab} onChange={handleTabChange}>
            <Tab value={0} label={'Games'}/>
            <Tab value={1} label={'Players'}/>
            <Tab value={2} label={'Add Game'}/>
        </Tabs>

        <TabPanel value={currentTab} showValue={0}>
            <SinglesGamesList type={'UNRANKED'} hiddenPoints/>
        </TabPanel>

        <TabPanel value={currentTab} showValue={1}>
            <SinglesPlayersList type={'UNRANKED'} pointsHidden/>
        </TabPanel>

        <TabPanel value={currentTab} showValue={2}>
            <AddSinglesGameComponent type={'UNRANKED'}/>
        </TabPanel>
    </div>)
};

export default UnrankedComponent;

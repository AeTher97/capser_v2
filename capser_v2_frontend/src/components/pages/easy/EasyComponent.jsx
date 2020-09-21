import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import {useHistory} from "react-router-dom";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "../../misc/TabPanel";
import {EasyIcon, SinglesIcon} from "../../../misc/icons/CapsIcons";
import {useSelector} from "react-redux";
import SinglesGamesList from "../singles/SinglesGamesList";
import SinglesPlayersList from "../singles/SinglesPlayersList";
import AddSinglesGameComponent from "../singles/AddSinglesGameComponent";

const EasyComponent = () => {

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
        <PageHeader title={"Easy Caps"} icon={<EasyIcon fontSize={"large"}/>} noSpace/>

        <Tabs value={currentTab} onChange={handleTabChange}>
            <Tab value={0} label={'Games'}/>
            <Tab value={1} label={'Players'}/>
            <Tab value={2} label={'Add Game'}/>
        </Tabs>

        <TabPanel value={currentTab} showValue={0}>
            <SinglesGamesList type={'EASY_CAPS'}/>
        </TabPanel>

        <TabPanel value={currentTab} showValue={1}>
            <SinglesPlayersList type={'EASY_CAPS'}/>
        </TabPanel>

        <TabPanel value={currentTab} showValue={2}>
            <AddSinglesGameComponent type={'EASY_CAPS'}/>
        </TabPanel>
    </div>)
};

export default EasyComponent;

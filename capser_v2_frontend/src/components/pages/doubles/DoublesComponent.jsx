import React, {useState} from 'react';
import {Typography} from "@material-ui/core";
import PageHeader from "../../misc/PageHeader";
import {DoublesIcon} from "../../../misc/icons/CapsIcons";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "../../misc/TabPanel";
import {useSelector} from "react-redux";
import {useHistory} from "react-router-dom";
import AddDoublesGameComponent from "./AddDoublesGameComponent";
import MultipleGameList from "./MultipleGameList";
import TeamsList from "./TeamsList";


const DoublesComponent = () => {
    const [currentTab, setCurrentTab] = useState(0);
    const {isAuthenticated} = useSelector(state => state.auth);
    const history = useHistory();

    const handleTabChange = (e, value) => {
        if (value === 2 && !isAuthenticated) {
            history.push('/login')
        }
        setCurrentTab(value);
    }
    return (
        <div>
            <PageHeader title={"Doubles"} icon={<DoublesIcon/>}/>
            <Tabs value={currentTab} onChange={handleTabChange}>
                <Tab value={0} label={'Games'}/>
                <Tab value={1} label={'Teams'}/>
                <Tab value={2} label={'Add Game'}/>
            </Tabs>
            <TabPanel value={currentTab} showValue={0}>
                <MultipleGameList  type={'DOUBLES'}/>
            </TabPanel>
            <TabPanel value={currentTab} showValue={1}>
                <TeamsList type={'DOUBLES'}/>
            </TabPanel>
            <TabPanel value={currentTab} showValue={2}>
                <AddDoublesGameComponent/>
            </TabPanel>
        </div>
    );
};

export default DoublesComponent;

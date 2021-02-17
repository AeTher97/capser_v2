import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import {useHistory} from "react-router-dom";
import SinglesGamesList from "./SinglesGamesList";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "../../misc/TabPanel";
import AddSinglesGameComponent from "./AddSinglesGameComponent";
import SinglesPlayersList from "./SinglesPlayersList";
import {SinglesIcon} from "../../../misc/icons/CapsIcons";
import {useSelector} from "react-redux";
import {useHasRole} from "../../../utils/SecurityUtils";

const SinglesComponent = () => {

    const [currentTab, setCurrentTab] = useState(0);

    const {isAuthenticated} = useSelector(state => state.auth)

    const history = useHistory();
    const hasRole = useHasRole();


    const handleTabChange = (e, value) => {
        if (value === 2 && !isAuthenticated) {
            history.push('/login')
        }
        setCurrentTab(value);
    }

    return (<div>
        <PageHeader title={"Singles"} icon={<SinglesIcon fontSize={"large"}/>} noSpace/>

        <Tabs value={currentTab} onChange={handleTabChange} style={{marginTop:5}} centered>
            <Tab value={0} label={'Games'}/>
            <Tab value={1} label={'Players'}/>
            {!hasRole('ADMIN') &&   <Tab value={2} label={'Post Game'}/>}
        </Tabs>

        <TabPanel value={currentTab} showValue={0}>
            <SinglesGamesList type={'SINGLES'}/>
        </TabPanel>

        <TabPanel value={currentTab} showValue={1}>
            <SinglesPlayersList type={'SINGLES'}/>
        </TabPanel>

        {!hasRole('ADMIN') && <TabPanel value={currentTab} showValue={2}>
            <AddSinglesGameComponent type={'SINGLES'}/>
        </TabPanel>}
    </div>)
};

export default SinglesComponent;

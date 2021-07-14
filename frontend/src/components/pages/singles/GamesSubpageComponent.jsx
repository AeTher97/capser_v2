import React, {useState} from 'react';
import {useSelector} from "react-redux";
import useQuery from "../../../utils/UserQuery";
import {Redirect, useHistory, useLocation} from "react-router-dom";
import {useHasRole} from "../../../utils/SecurityUtils";
import PageHeader from "../../misc/PageHeader";
import {EasyIcon, SinglesIcon, UnrankedIcon} from "../../../misc/icons/CapsIcons";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "../../misc/TabPanel";
import SinglesGamesList from "./SinglesGamesList";
import SinglesPlayersList from "./SinglesPlayersList";
import AddSinglesGameComponent from "./AddSinglesGameComponent";
import {getRequestGameTypeString} from "../../../utils/Utils";

const GamesSubpageComponent = ({title, type}) => {
    const {isAuthenticated} = useSelector(state => state.auth)
    const [redirect, setRedirect] = useState(false);
    const [redirectTarget, setRedirectTarget] = useState('')
    const query = useQuery();
    const tab = query.get('tab') || 'games';
    const history = useHistory();
    const hasRole = useHasRole();
    const location = useLocation();


    if (tab === 'addGame' && !isAuthenticated) {
        return (<Redirect to={{pathname: "/login", state: {prevPath: location.pathname + '?tab=' + tab}}}/>)
    }


    const handleTabChange = (e, value) => {
        if (value === 'addGame' && !isAuthenticated) {
            setRedirectTarget(value);
            setRedirect(true);
            return;
        }
        history.push(`/${getRequestGameTypeString(type)}/?tab=${value}`)
    }

    const getIcon = () => {
        if (type === 'SINGLES') {
            return <SinglesIcon fontSize={'large'}/>
        } else if (type === 'EASY_CAPS') {
            return <EasyIcon fontSize={'large'}/>
        } else {
            return <UnrankedIcon fontSize={'large'}/>
        }
    }

    return (<div>
        <PageHeader title={title} icon={getIcon()}/>

        <Tabs value={tab} onChange={handleTabChange} style={{marginTop: 5}} centered>
            <Tab value={'games'} label={'Games'}/>
            <Tab value={'players'} label={'Players'}/>
            {!hasRole('ADMIN') && <Tab value={'addGame'} label={'Post Game'}/>}
        </Tabs>

        <TabPanel value={tab} showValue={'games'}>
            <SinglesGamesList type={type} render={tab === 'games'}/>
        </TabPanel>

        <TabPanel value={tab} showValue={'players'}>
            <SinglesPlayersList type={type}/>
        </TabPanel>

        {!hasRole('ADMIN') && <TabPanel value={tab} showValue={'addGame'}>
            <AddSinglesGameComponent type={type}/>
        </TabPanel>}

        {redirect &&
        <Redirect to={{pathname: "/login", state: {prevPath: location.pathname + '?tab=' + redirectTarget}}}/>}
    </div>)
};

GamesSubpageComponent.propTypes = {};

export default GamesSubpageComponent;

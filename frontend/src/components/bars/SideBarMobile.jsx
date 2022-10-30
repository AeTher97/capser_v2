import React, {useEffect, useState} from 'react';
import {SwipeableDrawer} from "@material-ui/core";
import {useHistory} from "react-router-dom";
import SideBarContent from "./SideBarContent";


const SideBarMobile = ({open, setOpen}) => {

    const history = useHistory();

    const openState = {
        visible: true,
        width: 300
    }

    const closedState ={
        visible: false,
        width: 300
    }


    const [state, setState] = useState(open ? openState : closedState);


    const go = (address) => {
        history.push(address);
            setOpen(false);

    }


    useEffect(() => {
        if (open) {
            setState(openState);
        } else {
            setState(closedState);
        }
    }, [open])


    return (

        <SwipeableDrawer variant={"temporary"} open={state.visible}
                         onOpen={() => {
                             setOpen(true);
                         }}
                         onClose={() => {
                             setOpen(false)
                         }}
     >
            <div style={{height: 48}}/>
            <SideBarContent state={state} small={true} go={go}/>
        </SwipeableDrawer>
    );
};


export default SideBarMobile;

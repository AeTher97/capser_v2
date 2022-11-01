import React, {useEffect, useState} from 'react';
import {Drawer} from "@material-ui/core";
import {useHistory} from "react-router-dom";
import SideBarContent from "./SideBarContent";


const SideBar = ({open, setOpen}) => {

    const history = useHistory();

    const openState =  {
        visible: true,
        width: 300
    } ;

    const closedState =  {
        visible: true,
        width: 50
    } ;


    const [state, setState] = useState(open ? openState : closedState);


    const go = (address) => {
        history.push(address);

    }


    useEffect(() => {
        if (open) {
            setState(openState);
        } else {
            setState(closedState);
        }
    }, [open])


    return (

        <Drawer variant={"persistent"} open={state.visible}

                onMouseEnter={() => {
                                 setOpen(true);

                         }}
                         onMouseLeave={() => {
                                 setOpen(false);

                         }}>
            <SideBarContent state={state} small={false} go={go}/>
        </Drawer>
    );
};


export default SideBar;

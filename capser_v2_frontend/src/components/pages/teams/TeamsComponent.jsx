import React, {useState} from 'react';
import PageHeader from "../../misc/PageHeader";
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';
import mainStyles from "../../../misc/styles/MainStyles";
import Grid from "@material-ui/core/Grid";
import {Typography} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";

const TeamsComponent = () => {

    const classes = mainStyles();

    const [teams,setTeams] = useState([]);

    return (
        <div>
            <PageHeader title={"Teams"} icon={<PeopleOutlineIcon fontSize={"large"}/>}/>

            {/*<Tabs value={currentTab} onChange={handleTabChange}>*/}
            <Tabs>
                <Tab value={0} label={'Teams'}/>
                <Tab value={1} label={'Stats'}/>
            </Tabs>

            <div className={classes.root}>
                <Grid container spacing={2}>
                    <Grid item sm={4}>
                        <div style={{padding: 10, minHeight: 500,}}
                             className={[classes.squareShine, classes.neon].join(' ')}>
                            <div className={classes.header}>
                                <Typography variant={"h5"} style={{flex: 1}}>Your Teams</Typography>
                                <Button>Create new</Button>
                            </div>
                        </div>
                    </Grid>
                    <Grid item sm={8}>
                        <div style={{padding: 10}} className={[classes.squareShine, classes.neon].join(' ')}>
                            <div style={{
                                minHeight: 500,
                                display: 'flex',
                                justifyContent: 'center',
                                flexDirection: "column",
                                alignItems: 'center'
                            }}>
                                <img src={"/dd.svg"} style={{maxWidth: 250}}/>
                                {teams.length > 0 ? <Typography variant={"h5"}>Select a team to start</Typography> : <Typography  variant={"h5"}>No teams, create one</Typography>}
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
};

export default TeamsComponent;

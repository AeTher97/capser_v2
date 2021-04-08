import React, {useState} from 'react';
import {useUserPlots} from "../../data/UserData";
import Plot from "./Plot";
import {Typography, useTheme} from "@material-ui/core";
import mainStyles from "../../misc/styles/MainStyles";

const ProfilePlots = ({userId, width}) => {
    const [gameType, setGameType] = useState('SINGLES');
    const {data, loading, loaded} = useUserPlots(userId, gameType);

    return (
        <div>
            <ProfilePlotsSelector selected={gameType} onClick={(value) => setGameType(value)}/>
            <Typography variant={"h6"}>Results over time</Typography>
            {gameType === 'DOUBLES' &&
            <Typography>Plots in double stats are aggregated results from all teams</Typography>}
            {loaded && <>
                <Plot width={width} timeSeries={data.pointSeries} title={"Points"}/>
                <Plot width={width} timeSeries={data.rebuttalsSeries} title={"Average rebuttals"}/>
            </>}

        </div>
    );
};

const ProfilePlotsSelector = ({selected, onClick}) => {

    const classes = mainStyles();


    return <div style={{display: 'flex', justifyContent: 'center'}}>
        <div className={classes.standardBorder}
             style={{margin: 0, maxWidth: 500, flex: 1, display: 'flex', justifyContent: 'center', padding: 0}}>

            <SelectorElement selected={selected === "SINGLES"} value={"SINGLES"} onClick={onClick}>
                Singles
            </SelectorElement>
            <SelectorElement selected={selected === 'EASY_CAPS'} value={"EASY_CAPS"} onClick={onClick}>
                Easy Caps
            </SelectorElement>
            <SelectorElement last selected={selected === "DOUBLES"} value={"DOUBLES"} onClick={onClick}>
                Doubles
            </SelectorElement>
        </div>
    </div>
}

const SelectorElement = ({
                             children, last, selected, onClick = () => {
    }, value
                         }) => {
    const theme = useTheme();

    return <div onClick={() => onClick(value)} style={{
        borderRadius: 5,
        borderRight: last ? null : "1px solid" + theme.palette.divider,
        flex: 0.33,
        padding: 5,
        textAlign: 'center',
        cursor: 'pointer',
        backgroundColor: selected ? "rgb(75,0,0)" : "transparent"
    }}>
        {children}
    </div>
}
export default ProfilePlots;
import React from 'react';
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import mainStyles from "../../misc/styles/MainStyles";
import Achievement from "../achievements/Achievement";
import gameIconWithName from "../../misc/GameIconWithName";

const AchievementsTab = ({achievementsList}) => {

    const achievementStyles = useAchievementStyles();
    const classes = mainStyles();


    const renderAchievements = () => {
        if (!achievementsList) {
            return;
        }
        return achievementsList.map(achievement =>
            <div className={classes.standardBorder}>
                <div className={[achievementStyles.container]}>
                    <Achievement name={achievement.achievement}/>
                    {new Date(achievement.dateAchieved).toDateString()}
                    {gameIconWithName(achievement)}
                </div>
            </div>
        )
    }

    return (
        <div>
            <Typography variant={"h6"}>Achievements</Typography>
            <div style={{display: "flex", flexWrap: "wrap", justifyContent: "center"}}>
                {renderAchievements()}
            </div>
        </div>
    );
};

const useAchievementStyles = makeStyles(() => ({
    container: {
        padding: 5,
        margin: 20,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        width: 120
    }
}));

export default AchievementsTab;
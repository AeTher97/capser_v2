import React from 'react';

const Achievement = ({name}) => {

    const icon = 1;

    const getSource = () => {
        switch (name) {
            case "PLACE_IN_EASY":
            case "PLACE_IN_SINGLES":
                return "/achievements/Placement.png"
            case "PLAY_FIRST_GAME":
                return "/achievements/First_game.png"
            case "FIRST_WIN":
                return "/achievements/Win_first_game.png"
            case "FIRST_NAKED_LAP":
                return "/achievements/Naked_lap.png"
            case "REBUTTALS_IN_A_ROW_5":
                return "/achievements/Rebuttals_5.png"
            case "REBUTTALS_IN_A_ROW_7":
                return "/achievements/Rebuttals_7.png"
            case "REBUTTALS_IN_A_ROW_10":
                return "/achievements/Rebuttals_10.png"
            case "REBUTTALS_IN_A_ROW_12":
                return "/achievements/Rebuttals_12.png"
            case "REBUTTALS_IN_A_ROW_15":
                return "/achievements/Rebuttals_15.png"
            case "REBUT_ON_LAST_CHANCE":
                return "/achievements/Rebuttal_on_11.png"
            default:
                return ""
        }
    }

    const getDescription = () => {
        switch (name) {
            case "PLACE_IN_EASY":
                return "Get placement in easy caps!"
            case "PLACE_IN_SINGLES":
                return "Get placement in singles!"
            case "PLAY_FIRST_GAME":
                return "Play first game!"
            case "FIRST_WIN":
                return "Win first game!"
            case "FIRST_NAKED_LAP":
                return "First naked lap!"
            case "REBUTTALS_IN_A_ROW_5":
                return "5 rebuttals in a row!"
            case "REBUTTALS_IN_A_ROW_7":
                return "7 rebuttals in a row!"
            case "REBUTTALS_IN_A_ROW_10":
                return "10 rebuttals in a row!"
            case "REBUTTALS_IN_A_ROW_12":
                return "12 rebuttals in a row!"
            case "REBUTTALS_IN_A_ROW_15":
                return "15 rebuttals in a row!"
            case "REBUT_ON_LAST_CHANCE":
                return "Rebut on your last chance!"
            default:
                return ""
        }
    }


    return (
        <div style={{display: "flex", flexDirection: "column", alignItems: "center"}}>
            <img src={getSource()}/>
            <div style={{textAlign: "center"}}>{getDescription()}</div>
        </div>
    );
};

export default Achievement;
import React, {useEffect, useState} from 'react';
import LiveGameCards from "./LiveGameCards";
import AddSinglesGameComponent from "../../../components/game/AddSinglesGameComponent";
import {useSelector} from "react-redux";

export const SINK = "SINK"
export const REBUTTAL = "REBUTTAL"
export const POINT = "POINT"
export const END = "END"
export const START = "START"
export const BEER_DOWNED = "BEER_DOWNED"
export const BEER_SPILLED = "BEER_SPILLED"
export const OUT_OF_TURN = "OUT_OF_TURN"
export const ROCK_PAPER_SCISSORS = "ROCK_PAPER_SCISSORS"

const LiveGameScreen = () => {

    const {userId} = useSelector(state => state.auth);

    const [rebuttalsActive, setRebuttalsActive] = useState(false);
    const [player1Sunk, setPlayer1Sunk] = useState(false);
    const [stage, setStage] = useState(1);
    const [gameEvents, setGameEvents] = useState([{
        gameEvent: START,
        time: new Date()

    }]);

    const [gameState, setGameState] = useState({
            player1: {
                sinks: 0,
                points: 0,
                rebuttals: 0
            },
            player2: {
                sinks: 0,
                points: 0,
                rebuttals: 0
            }
        }
    )


    useEffect(() => {
        const disablePullToRefresh = (e) => {
            // Prevent default action if the touch move is vertical
            if (e.touches.length > 1 || e.touches[0].clientY > 0) {
                e.preventDefault();
            }
        };
        // Add event listener to the document
        document.addEventListener("touchmove", disablePullToRefresh, {passive: false});

        // Clean up the event listener on unmount
        return () => {
            document.removeEventListener("touchmove", disablePullToRefresh);
        };
    }, []);

    const addGameEvents = (user, ...types) => {
        const newGameEvents = [...gameEvents];
        types.forEach(type => {
            newGameEvents.push({
                gameEvent: type,
                time: new Date(),
                userId: user ? userId : null
            })
        })
        setGameEvents(newGameEvents)
    }

    const switchToRebuttals = () => {
        setRebuttalsActive(true);
    }

    const switchToDefault = () => {
        setRebuttalsActive(false);
    }

    const sinkForPlayer1 = () => {
        const sinkCount = gameState.player1.sinks + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player1.sinks = sinkCount;
            return newState;
        })
        setPlayer1Sunk(true);
    }

    const sinkForPlayer2 = () => {
        const sinkCount = gameState.player2.sinks + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player2.sinks = sinkCount;
            return newState;
        })
        setPlayer1Sunk(false);
    }

    const pointForPlayer1 = () => {
        const pointsCount = gameState.player1.points + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player1.points = pointsCount;
            return newState;
        })
        if (pointsCount === 11) {
            setStage(3);
        }
    }

    const pointForPlayer2 = () => {
        const pointsCount = gameState.player2.points + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player2.points = pointsCount;
            return newState;
        })
        if (pointsCount === 11) {
            setStage(3);
        }
    }

    const rebuttalForPlayer1 = () => {
        const rebuttalsCount = gameState.player1.rebuttals + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player1.rebuttals = rebuttalsCount;
            return newState;
        })
    }

    const rebuttalForPlayer2 = () => {
        const rebuttalsCount = gameState.player1.rebuttals + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player2.rebuttals = rebuttalsCount;
            return newState;
        })
    }

    useEffect(() => {
        setTimeout(() => {
            // setStage(1);

        }, 500)
    }, []);

    return (
        <div style={{
            height: "100%",
            display: "flex",
            width: "400vw",
        }}>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${stage * -100}%,0)`,
                padding: "20px 20px 20px 20px",
                display: "flex",
                alignItems: "center",
                flexDirection: "column",
                justifyContent: "center",

            }}>
                <img src={"/glhf.png"} style={{width: "60vw"}}/>
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${stage * -100 + (rebuttalsActive ? -100 : 0)}%,0)`,
                padding: "20px 20px 20px 20px",
                display: "flex",
                alignItems: "stretch"

            }}>
                <LiveGameCards gameState={gameState}
                               switchScreen={switchToRebuttals}
                               rebuttals={false}
                               sinkForPlayer1={sinkForPlayer1}
                               sinkForPlayer2={sinkForPlayer2}
                               addGameEvents={addGameEvents}
                />
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                position: "relative",
                padding: "20px 20px 20px 20px",
                transform: `translate(${stage * -100 + (rebuttalsActive ? -100 : 0)}%,0)`,
                display: "flex",
                alignItems: "stretch"
            }}>
                <LiveGameCards
                    gameState={gameState}
                    switchScreen={switchToDefault}
                    rebuttals={true}
                    sinkForPlayer1={sinkForPlayer1}
                    sinkForPlayer2={sinkForPlayer2}
                    pointForPlayer1={pointForPlayer1}
                    pointForPlayer2={pointForPlayer2}
                    rebuttalForPlayer1={rebuttalForPlayer1}
                    rebuttalForPlayer2={rebuttalForPlayer2}
                    player1Sunk={player1Sunk}
                    addGameEvents={addGameEvents}
                />
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${stage * -100}%,0)`,
                display: "flex",
                alignItems: "stretch",
                flexDirection: "column",
                padding: "20px 20px 20px 20px",
                justifyContent: "center",

            }}>
                <AddSinglesGameComponent type={"EASY_CAPS"} disableEditing={true}
                                         player1Points={gameState.player1.points}
                                         player1Sinks={gameState.player1.sinks}
                                         player2Points={gameState.player2.points}
                                         player2Sinks={gameState.player2.sinks} gameEventsList={gameEvents}/>
            </div>
        </div>

    );
};


export default LiveGameScreen;
import React, {useEffect, useState} from 'react';
import LiveGameCards from "./LiveGameCards";
import AddSinglesGameComponent from "../../../components/game/AddSinglesGameComponent";
import {useSelector} from "react-redux";
import FillingDots from "../../../components/misc/FillingDots";
import {Typography} from "@material-ui/core";

export const SINK = "SINK"
export const REBUTTAL = "REBUTTAL"
export const POINT = "POINT"
export const END = "END"
export const START = "START"
export const BEER_DOWNED = "BEER_DOWNED"
export const BEER_SPILLED = "BEER_SPILLED"
export const OUT_OF_TURN = "OUT_OF_TURN"
export const ROCK_PAPER_SCISSORS = "ROCK_PAPER_SCISSORS"

const SUMMARY_PAGE = 6;

const LiveGameScreen = () => {

    const {userId} = useSelector(state => state.auth);

    const [rebuttalsActive, setRebuttalsActive] = useState(false);
    const [player1Sunk, setPlayer1Sunk] = useState(false);
    const [stage, setStage] = useState(2);
    const [disabled, setDisabled] = useState(false);
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
        const tutorialPassed = localStorage.getItem("tutorialPassed");
        if (tutorialPassed) {
            setTimeout(() => {
                setStage(3);
            }, 500)
        } else {
            setStage(0);
        }
    }, []);


    useEffect(() => {
        const disablePullToRefresh = (e) => {
            // Prevent default action if the touch move is vertical
            if (e.touches.length > 1 || e.touches[0].clientY > 0) {
                e.preventDefault();
            }
        };
        if (stage !== SUMMARY_PAGE) {
            // Add event listener to the document
            document.addEventListener("touchmove", disablePullToRefresh, {passive: false});

            // Clean up the event listener on unmount
            return () => {
                document.removeEventListener("touchmove", disablePullToRefresh);
            };
        } else {
            setTimeout(() => {
                document.removeEventListener("touchmove", disablePullToRefresh);
            }, 300);
        }
    }, [stage]);

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
        setDisabled(true)
        setTimeout(() => {
            setDisabled(false);
        }, 250)
    }

    const switchToDefault = () => {
        setRebuttalsActive(false);
        setDisabled(true)
        setTimeout(() => {
            setDisabled(false);
        }, 250)
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
            addGameEvents(null, END)
            setStage(SUMMARY_PAGE);
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
            addGameEvents(null, END)
            setStage(SUMMARY_PAGE);
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
        const rebuttalsCount = gameState.player2.rebuttals + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player2.rebuttals = rebuttalsCount;
            return newState;
        })
    }

    return (
        <div style={{
            height: "100%",
            display: "flex",
            width: "700vw",
            maxHeight: "100%",
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
                <div style={{display: "flex", flexDirection: "column", alignItems: "center", textAlign: "center"}}>
                    <img src={"/main_game.png"} style={{width: "60vw"}}/>
                    <Typography>You are about to enter the game screen. If you manage to sink a cap flip the scoreboard
                        down
                        if your opponent does sink flip the scoreboard up.<br/><br/> After that you will enter rebuttals
                        screen.
                    </Typography>
                </div>
                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignSelf: "stretch",
                    marginTop: "auto"
                }}>
                    <FillingDots count={3} filledCount={1}/>
                    <div onClick={() => setStage(1)}>
                        <Typography color={"primary"}>
                            Next
                        </Typography>
                    </div>
                </div>
            </div>
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
                <div style={{display: "flex", flexDirection: "column", alignItems: "center", textAlign: "center"}}>
                    <img src={"/rebuttal.png"} style={{width: "60vw"}}/>
                    <Typography>Person rebutting is indicated by the green outline.
                        If someone rebutted drag the green card if not drag the red.
                        Then colors switch. Follow the green with rebuttals. <br/><br/> If someone fails you go back to
                        previous screen.
                    </Typography>
                </div>
                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignSelf: "stretch",
                    marginTop: "auto"
                }}>
                    <FillingDots count={3} filledCount={2}/>
                    <div onClick={() => setStage(2)}>
                        <Typography color={"primary"}>
                            Next
                        </Typography>
                    </div>
                </div>
            </div>
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
                <div style={{display: "flex", flexDirection: "column", alignItems: "center", textAlign: "center"}}>
                    <img src={"/glhf.png"} style={{width: "60vw"}}/>
                    <Typography>Good luck and have fun in your games!
                    </Typography>
                </div>
                {!localStorage.getItem("tutorialPassed") && <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignSelf: "stretch",
                    marginTop: "auto"
                }}>
                    <FillingDots count={3} filledCount={3}/>
                    <div onClick={() => {
                        setStage(3);
                        localStorage.setItem("tutorialPassed", "true")
                    }}>
                        <Typography color={"primary"}>
                            Next
                        </Typography>
                    </div>
                </div>}
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${stage * -100}%,0)`,
                padding: "20px 20px 20px 20px",
                display: "flex",
                alignItems: "center",
                flexDirection: "column",
                justifyContent: "space-around",
                textAlign: "center"
            }}>
                <div style={{
                    width: 200,
                    height: 200,
                    border: "5px solid red",
                    borderRadius: 50,
                    display: "flex",
                    justifyContent: "center",
                    flexDirection: "column"
                }} onClick={() => {
                    addGameEvents(userId, ROCK_PAPER_SCISSORS)
                    setStage(4)
                }}>
                    <Typography variant={"h4"}>You</Typography>
                </div>
                <Typography variant={"h4"}>
                    Who won rock paper scissors?
                </Typography>
                <div style={{
                    width: 200,
                    height: 200,
                    border: "5px solid red",
                    borderRadius: 50,
                    display: "flex",
                    justifyContent: "center",
                    flexDirection: "column"
                }} onClick={() => {
                    addGameEvents(null, ROCK_PAPER_SCISSORS)
                    setStage(4)
                }}>
                    <Typography variant={"h4"}>Opponent</Typography>
                </div>
                {!localStorage.getItem("tutorialPassed") && <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignSelf: "stretch",
                    marginTop: "auto"
                }}>
                    <FillingDots count={3} filledCount={3}/>
                    <div onClick={() => {
                        setStage(3);
                        localStorage.setItem("tutorialPassed", "true")
                    }}>
                        <Typography color={"primary"}>
                            Next
                        </Typography>
                    </div>
                </div>}
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
                               disabled={disabled}
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
                    disabled={disabled}
                />
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${stage * -100}%,0)`,
                display: "flex",
                alignItems: "stretch",
                flexDirection: "column",
                padding: "0px 20px 20px 20px",
                overflow: "scroll",
            }}>
                <AddSinglesGameComponent type={"EASY_CAPS"} disableEditing={true}
                                         player1Points={gameState.player1.points}
                                         player1Sinks={gameState.player1.sinks}
                                         player2Points={gameState.player2.points}
                                         player2Sinks={gameState.player2.sinks}
                                         gameEventsList={gameEvents}/>
            </div>
        </div>

    );
};


export default LiveGameScreen;
import React, {useEffect, useState} from 'react';
import LiveGameCards from "./LiveGameCards";


const LiveGameScreen = () => {

    const [rebuttalsActive, setRebuttalsActive] = useState(false);
    const [player1Sunk, setPlayer1Sunk] = useState(false);

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
    }

    const pointForPlayer2 = () => {
        const pointsCount = gameState.player2.points + 1;
        setGameState(state => {
            const newState = {...state}
            newState.player2.points = pointsCount;
            return newState;
        })
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


    return (
        <div style={{
            height: "100%",
            display: "flex",
            width: "200vw",
        }}>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                transform: `translate(${rebuttalsActive ? -100 : 0}%,0)`,
                padding: "20px 20px 20px 20px",
                display: "flex",
                alignItems: "stretch"

            }}>
                <LiveGameCards gameState={gameState}
                               switchScreen={switchToRebuttals}
                               rebuttals={false}
                               sinkForPlayer1={sinkForPlayer1}
                               sinkForPlayer2={sinkForPlayer2}
                />
            </div>
            <div style={{
                width: "100vw",
                transition: "0.3s ease-in-out",
                position: "relative",
                padding: "20px 20px 20px 20px",
                transform: `translate(${rebuttalsActive ? -100 : 0}%,0)`,
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
                />
            </div>
        </div>

    );
};


export default LiveGameScreen;
import React, {useEffect, useRef, useState} from 'react';
import {Typography} from "@material-ui/core";
import {POINT, REBUTTAL, SINK} from "./LiveGameScreen";
import BoldTyphography from "../../../components/misc/BoldTyphography";

const BREAK_ROTATION = 75;
const ZERO_ROTATION = 0;
const UP_SPEED =  3.5;
const DOWN_SPEED = 3.5;
const DRAG_MULTIPLIER = 1.5;

const LiveGameCards = ({
                           gameState,
                           switchScreen,
                           rebuttals,
                           sinkForPlayer1,
                           sinkForPlayer2,
                           pointForPlayer1,
                           pointForPlayer2,
                           rebuttalForPlayer1,
                           rebuttalForPlayer2,
                           player1Sunk,
                           addGameEvents,
                           disabled
                       }) => {
    const [everClicked, setEverClicked] = useState(false);
    const [rotationTopCard, setRotationTopCard] = useState(ZERO_ROTATION);
    const [rotationBottomCard, setRotationBottomCard] = useState(ZERO_ROTATION);

    const [disableInteractionTopCard, setDisableInteractionTopCard] = useState(false);
    const [disableInteractionBottomCard, setDisableInteractionBottomCard] = useState(false);

    const [animatingDownTopCard, setAnimatingDownTopCard] = useState(false);
    const [animatingDownBottomCard, setAnimatingDownBottomCard] = useState(false);

    const [rebuttedTopCard, setRebuttedTopCard] = useState(false);
    const [rebuttedBottomCard, setRebuttedBottomCard] = useState(false);

    const [timeoutObjectTopCard, setTimeoutObjectTopCard] = useState(null);
    const [timeoutObjectBottomCard, setTimeoutObjectBottomCard] = useState(null);


    const ref = useRef();


    useEffect(() => {
        return () => {
            if (timeoutObjectTopCard) {
                clearTimeout(timeoutObjectTopCard);
            }
            if (timeoutObjectBottomCard) {
                clearTimeout(timeoutObjectBottomCard);
            }
        }
    }, []);

    const setRotation = (top, targetRotation) => {
        if (top) {
            setRotationTopCard(targetRotation);
        } else {
            setRotationBottomCard(targetRotation);
        }
    }

    const setDisableInteraction = (top, disable) => {
        if (top) {
            setDisableInteractionTopCard(disable)
        } else {
            setDisableInteractionBottomCard(disable);
        }
    }

    const setTimeoutObject = (top, timeout) => {
        if (top) {
            setTimeoutObjectTopCard(timeout);
        } else {
            setTimeoutObjectBottomCard(timeout)
        }
    }

    const setAnimatingDown = (top, animating) => {
        if (top) {
            setAnimatingDownTopCard(animating);
        } else {
            setAnimatingDownBottomCard(animating);
        }
    }

    const setRebutted = (top, animating) => {
        if (top) {
            setRebuttedTopCard(animating);
        } else {
            setRebuttedBottomCard(animating);
        }
    }

    const addRebuttal = (top) => {
        let pointScored = false;
        if (top) {
            if (player1Sunk) {
                pointForPlayer1();
                pointScored = true;
                addGameEvents(true, POINT);
            } else {
                rebuttalForPlayer1();
                addGameEvents(true, SINK, REBUTTAL);
            }
        } else {
            if (!player1Sunk) {
                pointForPlayer2();
                pointScored = true;
                addGameEvents(false, POINT);
            } else {
                rebuttalForPlayer2();
                addGameEvents(false, SINK, REBUTTAL);
            }
        }
        return pointScored;
    }

    const addSink = (top) => {
        if (rebuttals) {
            if (top) {
                if (!player1Sunk) {
                    sinkForPlayer1()
                }
            } else {
                if (player1Sunk) {
                    sinkForPlayer2();
                }
            }
        } else {
            if (top) {
                sinkForPlayer1();
            } else {
                sinkForPlayer2();
            }
        }
    }

    const frame = (top, targetRotation, currentRotation, callback) => {
        if (targetRotation <= currentRotation) {
            const result = currentRotation - DOWN_SPEED;
            if (result <= targetRotation) {
                setRotation(top, targetRotation);
                setDisableInteraction(top, false);
                setAnimatingDown(top, false);
                callback();
            } else {
                //Card was released
                setRotation(top, result);
                const timeout = setTimeout(() => frame(top, targetRotation, result, callback), 10)
                setTimeoutObject(top, timeout)
            }
        } else if (targetRotation > currentRotation) {
            const result = currentRotation + UP_SPEED;
            if (result >= targetRotation && targetRotation) {
                if (targetRotation !== 90) {
                    setRotation(top, targetRotation)
                } else {
                    //Point or rebuttal was scored
                    let pointScored = false;
                    if (rebuttals) {
                        addSink(top)
                        pointScored = addRebuttal(top);
                        if (!pointScored) {
                            setRebutted(!top, true);
                        }
                    } else {
                        addSink(top);
                        addGameEvents(top, SINK)
                    }
                    setRotation(top, 0)
                    animate(!top, 0, 90, () => {
                        setRebutted(!top, false);
                        if (!rebuttals) {
                            switchScreen();
                        } else if (rebuttals && ((player1Sunk && top) || (!player1Sunk && !top))) {
                            switchScreen()
                        }
                    }, pointScored || !rebuttals)
                }
                setDisableInteraction(top, false);
                setAnimatingDown(top, false);
                callback()
            } else {
                setRotation(top, result)
                const timeout = setTimeout(() => frame(top, targetRotation, result, callback), 10)
                setTimeoutObject(top, timeout)
            }
        }
    }


    const animate = (top, targetRotation, currentRotation, callback = () => {
    }, pointScored) => {
        if (targetRotation === 0 && pointScored) {
            setAnimatingDown(top, true)
        }
        setDisableInteraction(top, true);
        frame(top, targetRotation, currentRotation ? currentRotation : (top ? rotationTopCard : rotationBottomCard), callback)
    }

    console.log(rebuttedTopCard, rebuttedBottomCard)

    return (
        <div style={{
            display: "flex", flexDirection: "column", alignItems: "stretch",
            flex: 1
        }}>
            <div style={{position: "relative", flex: 1, display: "flex", flexDirection: "column"}}>
                {everClicked && ref.current && <div style={{
                    display: "flex",
                    position: "absolute",
                    height: ref.current.clientHeight,
                    width: ref.current.clientWidth
                }}>
                    <LiveCard disabled={true} stats={gameState.player1} green={animatingDownTopCard && rebuttals}
                              rebuttals={rebuttals} player1Sunk={player1Sunk} animating={animatingDownTopCard}
                              rebutted={rebuttedTopCard}/>
                </div>}
                <div ref={ref} style={{flex: 1, display: "flex"}}>
                    <LiveCard rotation={rotationTopCard}
                              setRotation={setRotationTopCard}
                              disableInteraction={disabled || disableInteractionTopCard}
                              animate={animate}
                              stats={gameState.player1}
                              green={rebuttals && !player1Sunk}
                              setEverClicked={setEverClicked}
                              animating={disableInteractionTopCard}/>
                </div>
            </div>
            <div style={{position: "relative", flex: 1, display: "flex", flexDirection: "column"}}>
                {everClicked && ref.current && <div style={{
                    display: "flex",
                    position: "absolute",
                    height: ref.current.clientHeight,
                    width: ref.current.clientWidth
                }}>
                    <LiveCard bottom={true} disabled={true} stats={gameState.player2}
                              green={animatingDownBottomCard && rebuttals} rebuttals={rebuttals}
                              player1Sunk={player1Sunk}
                              animating={animatingDownBottomCard} rebutted={rebuttedBottomCard}/>
                </div>}
                <div style={{flex: 1, display: "flex"}}>
                    <LiveCard bottom={true}
                              rotation={rotationBottomCard}
                              setRotation={setRotationBottomCard}
                              disableInteraction={disabled || disableInteractionBottomCard}
                              animate={animate}
                              stats={gameState.player2}
                              green={rebuttals && player1Sunk}
                              setEverClicked={setEverClicked}/>
                </div>
            </div>
        </div>
    );
};

const LiveCard = ({
                      stats,
                      rotation,
                      setRotation,
                      disableInteraction,
                      animate,
                      disabled = false,
                      bottom = false,
                      green = false,
                      rebuttals,
                      player1Sunk,
                      setEverClicked,
                      animating,
                      rebutted
                  }) => {

    const [clicked, setClicked] = useState(false);
    const [startPosition, setStartPosition] = useState(null);
    const ref = useRef();


    const handleClick = () => {
        setEverClicked(true);
        if (!disableInteraction && !disabled) {
            setClicked(true)
        }
    }

    const handleMove = (e) => {
        if (clicked && !disableInteraction && !disabled) {
            if (startPosition !== null) {
                let yOffset = e.nativeEvent.offsetY - startPosition.y;
                if (isNaN(yOffset)) {
                    yOffset = e.targetTouches[0].clientY - startPosition.y;
                }

                if (bottom) {
                    yOffset = -yOffset;
                }

                let rotation = yOffset / ref.current.getBoundingClientRect().height * 90 * DRAG_MULTIPLIER;
                if (rotation < 0) {
                    rotation = 0;
                } else if (rotation >= BREAK_ROTATION) {
                    animate(!bottom, 90, null, () => {
                    }, true)
                    setClicked(false);
                    return;
                }
                setRotation(rotation);
            } else {
                if (isNaN(e.nativeEvent.offsetY)) {
                    setStartPosition({x: e.targetTouches[0].clientX, y: e.targetTouches[0].clientY});
                } else {
                    setStartPosition({x: e.nativeEvent.offsetX, y: e.nativeEvent.offsetY});
                }
            }
        }
    }

    const handleLeave = () => {
        if (disabled) {
            return;
        }
        setClicked(false);
        setStartPosition(null)
        if (!disableInteraction && rotation < BREAK_ROTATION && rotation !== ZERO_ROTATION) {
            animate(!bottom, 0);
        }
    }


    return (
        <div style={{
            flex: 1,
            position: "relative",
            display: "flex",
            justifyContent: "center",
            alignItems: "stretch",
            perspective: disabled ? 0 : 2000,
            transformStyle: "preserve-3d",
            perspectiveOrigin: bottom ? "top" : "bottom"
        }}>
            <div style={{
                flex: 1,
                border: `5px solid ${green ? "green" : "red"}`,
                borderRadius: bottom ? "0 0 20px 20px" : "20px 20px 0 0",
                transformOrigin: bottom ? "top" : "bottom",
                transform: `rotateX(${bottom ? rotation : -rotation}deg)`,
                backgroundColor: "black",
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center"
            }}>
                <Typography variant={"h4"}>{bottom ? "Opponent" : "You"}</Typography>
                <Typography variant={"body1"}>Rebuttals</Typography>
                <Typography
                    variant={"body1"}>{!rebutted && !animating && disabled && rebuttals && ((bottom && player1Sunk) || (!bottom && !player1Sunk)) ? stats.rebuttals + 1 : stats.rebuttals}</Typography>
                <BoldTyphography variant={"h4"}>Points</BoldTyphography>
                <Typography
                    variant={"h4"}>{!animating && disabled && rebuttals && ((bottom && !player1Sunk) || (!bottom && player1Sunk)) ? stats.points + 1 : stats.points}</Typography>
                <Typography variant={"body2"}>Sinks</Typography>
                <Typography variant={"body2"}>{!rebutted && !animating && disabled &&
                (!rebuttals || (rebuttals && ((bottom && player1Sunk) || (!bottom && !player1Sunk)))) ? stats.sinks + 1 : stats.sinks}</Typography>

            </div>

            <div ref={ref} style={{width: "100%", height: "98%", position: "absolute"}}
                 onMouseDown={handleClick}
                 onTouchStart={handleClick}

                 onTouchEnd={handleLeave}
                 onMouseUp={handleLeave}
                 onMouseLeave={handleLeave}

                 onTouchMove={handleMove}
                 onMouseMove={handleMove}
            />
        </div>
    );
};


export default LiveGameCards;
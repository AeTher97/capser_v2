import React, {useEffect, useRef, useState} from "react";
import {Tooltip, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import {PointIcon, RebuttalIcon, SinkIcon} from "./TimelineIcons";
import {useXtraSmallSize} from "../../../utils/SizeQuery";


const leftColor = 'red';
const rightColor = "orange";


const getIcon = (evenType, color) => {
    switch (evenType) {
        case "SINK" : {
            return <SinkIcon color={color} fontSize={"small"}/>
        }
        case "POINT": {
            return <PointIcon color={color} fontSize={"large"}/>
        }
        case "REBUTTAL": {
            return <RebuttalIcon color={color} fontSize={"large"}/>
        }
    }

}

const Timeline = ({timeline, leftPlayer, leftPlayerName, rightPlayerName}) => {

    const small = useXtraSmallSize();
    const ref = useRef(null);

    const [height, setHeight] = useState(0);
    const verticalClasses = useTimelineStyles(height, small)();
    const horizontalClasses = useTimelineHorizontalStyles();

    const classes = small ? verticalClasses : horizontalClasses;

    useEffect(() => {
        if (ref.current) {
            setHeight(ref.current.clientHeight);
        }
    }, [ref.current])


    return (
        <div style={{
            marginLeft: "auto",
            marginRight: "auto",
            display: "flex",
            alignItems: "center",
            flexDirection: "column",
            paddingLeft: 40,
            paddingRight: 40
        }}>
            <div style={{padding: 10}}>
                <Typography variant={"h5"}>Timeline</Typography>

            </div>
            {small && <div style={{
                display: "flex", justifyContent: "space-around", flexDirection: small ? "row" : "column",
                width: small ? "100%" : null
            }}>
                <Typography variant={"h6"}>{leftPlayerName}</Typography><Typography
                variant={"h6"}>{rightPlayerName}</Typography>
            </div>}
            <div style={{position: "relative", marginTop: 20}}>

                <div className={classes.emptyDotLeft}/>
                <div className={classes.emptyDotRight}/>
                <div className={classes.lineLeft}/>
                <div className={classes.lineRight}/>
                <div className={classes.emptyDotLeft} style={{bottom: small ? 0 : null, right: small ? null : -47}}/>
                <div className={classes.emptyDotRight} style={{bottom: small ? 0 : null, right: small ? null : -47}}/>

                <div ref={ref} style={{
                    display: "flex", flexDirection: small ? "column" : "row", width: small ? 140 : null,
                    height: small ? null : 160, padding: 10
                }}>
                    {!small && <div style={{
                        display: "flex", justifyContent: "space-around", flexDirection: small ? "row" : "column",
                        width: small ? "100%" : null
                    }}>
                        <Typography variant={"h6"}>{leftPlayerName}</Typography><Typography
                        variant={"h6"}>{rightPlayerName}</Typography>
                    </div>}
                    <div style={{height: small ? 30 : null, width: small ? null : 30}}/>
                    {timeline.filter(event => event.gameEvent !== "SINK").map(event => {
                        const left = leftPlayer === event.userId;
                        return <div key={event.time + event.gameEvent} className={classes.row}>
                            {left && <div className={classes.dotLeft}/>}
                            {!left && <div className={classes.dotRight}/>}
                            <div className={left ? classes.leftItem : classes.rightItem}>
                                <Tooltip title={event.gameEvent}>
                                    <div>
                                        {getIcon(event.gameEvent, left ? leftColor : rightColor)}
                                    </div>
                                </Tooltip>
                                <div style={{color: left ? leftColor : rightColor}}>
                                    <Typography color={"inherit"} style={{position: "relative", top: -5}}
                                                variant={"caption"}>
                                        {new Date(event.time).getHours()}:{new Date(event.time).getMinutes()}</Typography>
                                </div>
                            </div>
                        </div>
                    })}
                </div>
            </div>
            <div style={{height: 30}}/>

        </div>
    );
};

const useTimelineHorizontalStyles = makeStyles(theme => ({
    leftItem: {
        padding: 0,
        transform: "translate(-12px,0)"
    },
    rightItem: {
        padding: 0,
        marginLeft: "auto",
        alignSelf: "flex-end"
    },
    dotRight: {
        position: "relative",
        width: 12,
        height: 6,
        borderRadius: "0 0  150px 150px ",
        backgroundColor: rightColor,
        top: "50%",
        left: 22
    },
    dotLeft: {
        position: "relative",
        width: 12,
        height: 6,
        borderRadius: "150px 150px 0 0",
        transform: "translate(-100%,-100%)",
        backgroundColor: leftColor,
        top: "50%",
        left: 22
    },
    emptyDotLeft: {
        position: "absolute",
        top: "50%",
        transform: "translate(0,-100%)",
        width: 20,
        height: 10,
        borderRadius: "150px 150px 0 0",
        border: "3px solid " + leftColor,
        borderBottom: 0

    },
    emptyDotRight: {
        position: "absolute",
        top: "50%",
        width: 20,
        height: 10,
        borderRadius: "0 0  150px 150px ",
        border: "3px solid " + rightColor,
        borderTop: 0

    },
    lineLeft: {
        position: "absolute",
        backgroundColor: leftColor,
        top: "50%",
        transform: "translate(0,-100%)",
        left: 24,
        height: 2,
        width: "100%",
    },
    lineRight: {
        position: "absolute",
        backgroundColor: rightColor,
        left: 24,
        top: "50%",
        width: "100%",
        height: 2
    },
    row: {
        position: "relative",
        display: "flex",

    }
}))

const useTimelineStyles = (height) => makeStyles(theme => ({
    leftItem: {
        padding: 0,
        transform: "translate(-12px,0)"
    },
    rightItem: {
        padding: 0,
        marginLeft: "auto",
        alignSelf: "flex-end"
    },
    dotRight: {
        position: "relative",
        width: 6,
        height: 12,
        borderRadius: "0px 150px 150px 0",
        backgroundColor: rightColor,
        left: "50%",
        top: 10
    },
    dotLeft: {
        position: "relative",
        width: 6,
        height: 12,
        borderRadius: "150px 0 0 150px",
        transform: "translate(-100%,0)",
        backgroundColor: leftColor,
        left: "50%",
        top: 10
    },
    emptyDotLeft: {
        position: "absolute",
        left: "50%",
        transform: "translate(-100%,0)",
        width: 10,
        height: 20,
        borderRadius: "150px 0 0 150px",
        border: "3px solid " + leftColor,
        borderRight: 0

    },
    emptyDotRight: {
        position: "absolute",
        left: "50%",
        width: 10,
        height: 20,
        borderRadius: " 0 150px 150px 0",
        border: "3px solid " + rightColor,
        borderLeft: 0

    },
    lineLeft: {
        position: "absolute",
        backgroundColor: leftColor,
        left: "50%",
        transform: "translate(-100%,0)",
        top: 24,
        width: 2,
        height: height - 48
    },
    lineRight: {
        position: "absolute",
        backgroundColor: rightColor,
        left: "50%",
        top: 24,
        width: 2,
        height: height - 48
    },
    row: {
        position: "relative",
        display: "flex",

    }
}))


export default Timeline;
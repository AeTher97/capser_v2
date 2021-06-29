import React, {useState} from 'react';
import {Button, Dialog, MenuItem, Select, TextField, Typography} from "@material-ui/core";
import PageHeader from "../misc/PageHeader";
import AccountTreeOutlinedIcon from '@material-ui/icons/AccountTreeOutlined';
import mainStyles from "../../misc/styles/MainStyles";
import {makeStyles} from "@material-ui/core/styles";
import {useTournamentsList} from "../../data/TournamentData";
import {useHistory} from "react-router-dom";
import {useHasRole} from "../../utils/SecurityUtils";
import {getRequestGameTypeString} from "../../utils/Utils";
import {useDispatch} from "react-redux";
import {showError} from "../../redux/actions/alertActions";
import LoadingComponent from "../../utils/LoadingComponent";
import GameIconWithName from "../../misc/GameIconWithName";
import BoldTyphography from "../misc/BoldTyphography";

export const getInProgressString = (seeded, finished) => {
    if (seeded && finished) {
        return "Finished";
    } else if (seeded && !finished) {
        return "In progress";
    } else if (!seeded) {
        return "In preparation";
    }
}

export const getTournamentTypeString = (type) => {
    switch (type) {
        case "SINGLE_ELIMINATION":
            return "Single elimination"
        case "DOUBLE_ELIMINATION":
            return "Double elimination"
        case "ROUND_ROBIN":
            return "Round robin"
    }
}


export const getSeedTypeString = (type) => {
    switch (type) {
        case "RANDOM":
            return "Random seed"
        case "ELO":
            return "Elo seeded"
        case "PICKED":
            return "Picked seeding"
    }
}


const TournamentsComponent = () => {

    const {tournaments, createNew, loading} = useTournamentsList("SINGLES", 0, 10);
    const history = useHistory();
    const [creationOpen, setCreationOpen] = useState(false);
    const [size, setSize] = useState('RO_16');
    const [gameType, setGameType] = useState('EASY_CAPS');
    const [tournamentType, setTournamentType] = useState('SINGLE_ELIMINATION');
    const [name, setName] = useState('');
    const hasRole = useHasRole();
    const dispatch = useDispatch();


    const classes = mainStyles();
    const styles = tournamentListStyles();
    return (
        <div>
            <PageHeader title={"Tournaments"} icon={<AccountTreeOutlinedIcon fontSize={"large"}/>} noSpace/>
            <div style={{display: "flex", justifyContent: "center"}}>
                <div className={[classes.paddedContent, styles.column].join(' ')}>
                    <div style={{display: "flex", flexDirection: "row", alignItems: 'center', padding: 10}}>
                        <Typography style={{flex: 1}} variant={"h4"}>Official tournaments list</Typography>
                        <div>
                            {hasRole('ADMIN') && <Button onClick={() => setCreationOpen(true)}>Create new</Button>}
                        </div>
                    </div>
                    {loading && <LoadingComponent/>}
                    {tournaments && <>
                        {tournaments.map(tournament => {
                            const multiplier = tournament.gameType === 'DOUBLES' ? 2 : 1;
                            return <div key={tournament.id} onClick={() => {
                                history.push(`${getRequestGameTypeString(tournament.gameType)}/tournament/${tournament.id}`)
                            }} className={classes.standardBorder}
                                        style={{marginBottom: 10, cursor: "pointer", paddingTop: 7}}>
                                <div className={classes.header} style={{alignItems: "flex-start"}}>
                                    <BoldTyphography variant={"h5"} color={"primary"} style={{flex: 1}}>
                                        {tournament.tournamentName}
                                    </BoldTyphography>
                                    <Typography
                                        variant={"caption"}
                                        style={{marginTop: 5}}>{new Date(tournament.date).toDateString()}
                                    </Typography>
                                </div>

                                <div style={{display: "flex", flexDirection: "column", alignItems: "flex-start"}}>
                                    <GameIconWithName gameType={tournament.gameType}/>
                                    <Typography variant={"caption"}>
                                        {getInProgressString(tournament.seeded, tournament.finished)}
                                    </Typography>
                                    <Typography variant={"caption"}>
                                        {getTournamentTypeString(tournament.tournamentType)}
                                    </Typography>
                                    <Typography variant={"caption"}>
                                        {getSeedTypeString(tournament.seedType)}
                                    </Typography>
                                    <Typography variant={"caption"}>
                                        {tournament.tournamentType === "DOUBLE_ELIMINATION" ? tournament.size.split("_")[2] * multiplier : tournament.size.split("_")[1] * multiplier} Players {multiplier === 2 && <>{tournament.tournamentType === "DOUBLE_ELIMINATION" ? tournament.size.split("_")[2] : tournament.size.split("_")[1]} Teams</>}
                                    </Typography>
                                </div>
                            </div>
                        })}
                    </>}
                    {!loading && tournaments.length && tournaments === 0 &&
                    <div style={{padding: 60, textAlign: 'center'}} className={classes.standardBorder}>
                        <Typography variant={"h5"}>No tournaments yet</Typography>
                    </div>}
                </div>
            </div>
            <Dialog open={creationOpen}>
                <div className={classes.standardBorder} style={{margin: 0}}>
                    <Typography variant={"h5"}>Create new tournament</Typography>
                    <div style={{display: "flex", flexDirection: "column"}}>
                        <TextField label={name === '' ? "Name" : ''} style={{width: 200}} value={name}
                                   onChange={event => setName(event.target.value)}/>
                        <Select style={{width: 200, marginBottom: 10}} value={size}
                                onChange={(e) => setSize(e.target.value)} label={"Player count"}>
                            <MenuItem value={"RO_8"}>8</MenuItem>
                            <MenuItem value={"RO_16"}>16</MenuItem>
                            {tournamentType !== "DOUBLE_ELIMINATION" && <MenuItem value={"RO_32"}>32</MenuItem>}
                            {tournamentType !== "DOUBLE_ELIMINATION" && <MenuItem value={"RO_64"}>64</MenuItem>}
                        </Select>
                        <Select style={{width: 200, marginBottom: 10}} value={gameType}
                                onChange={event => setGameType(event.target.value)}>
                            <MenuItem value={"SINGLES"}>Singles</MenuItem>
                            <MenuItem value={"EASY_CAPS"}>Easy caps</MenuItem>
                            <MenuItem value={"UNRANKED"}>Unranked</MenuItem>
                            <MenuItem value={"DOUBLES"}>Doubles</MenuItem>
                        </Select>
                        <Select style={{width: 200, marginBottom: 10}} value={tournamentType}
                                onChange={event => {
                                    setTournamentType(event.target.value)
                                    if (size === "RO_32" || size === "RO_64") {
                                        setSize("RO_16");
                                    }
                                }}>
                            <MenuItem value={"SINGLE_ELIMINATION"}>Single Elimination</MenuItem>
                            <MenuItem value={"DOUBLE_ELIMINATION"}>Double Elimination</MenuItem>
                        </Select>
                    </div>
                    <div>

                        <Button style={{marginRight: 5}} onClick={() => {
                            if (name === '') {
                                dispatch(showError("Invalid name"));
                                return;
                            }
                            createNew({
                                tournamentName: name,
                                seedType: "RANDOM",
                                tournamentType: tournamentType,
                                size: tournamentType === "SINGLE_ELIMINATION" ? size : "D_" + size
                            }, getRequestGameTypeString(gameType))
                            setCreationOpen(false)
                        }}>Create</Button>
                        <Button onClick={() => setCreationOpen(false)} variant={"outlined"}>Cancel</Button>
                    </div>
                </div>
            </Dialog>
        </div>
    );
};

const tournamentListStyles = makeStyles(theme => ({
    column: {
        maxWidth: 600
    }
}))

export default TournamentsComponent;

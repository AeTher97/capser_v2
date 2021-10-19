import React from 'react';
import PageHeader from "../../components/misc/PageHeader";
import GavelIcon from '@material-ui/icons/Gavel';
import mainStyles from "../../misc/styles/MainStyles";
import {Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

const RulesScreen = () => {

    const classes = mainStyles();
    const styles = useStyles();

    const basicRules = [
        "Game needs to have at least two teams with at least one player.",
        "Team count doesn't have to be equal, but if it's not, adjustments need to be made " +
        "for each team to shoot the same number of times each round. Adjustments can be made in any way that is fair and fulfills previously stated condition.",
        "Caps is a game played with beverages with at least 3% alcohol in it's contains.",
        "Game has to be played with 3 354.8ml cans or  2 0.5l cans in Europe (exceptions apply to unranked and easy caps which are played with 1.5 354.8ml can or 1 0.5l can in Europe)",
        "No practice shots are allowed, either in or out of the game.",
        "Game of Caps features two possible game modes: Overtime and Sudden Death. Sudden Death is played until one of the teams scores 11 points. Overtime is played until one of the teams scores 11 or more points with 2 point advantage.",
        "Team with more points (11 or more in case of Overtime) wins the game.",
        "Shooting order defines which player shoots a cap after which.",
        "Shooting order is created by the team which win the game of Rock Paper Scissors. Order has to alternate between both teams" +
        " (one team never shoots twice in a row, unless rule violation occurred).",
        "'Guest Starring shot' is when a person temporarily joins one of the teams to shoot one cap instead of one of the team players. ('Guest Starring shot' cannot occur while any o the teams is on match point').",
        "Each team can award one 'Guest starring shot' to any caps player in proximity not being one of the players as long as both team agree on this."
    ]

    const fieldRules = [
        "Game is played with two 680ml Tervis tumblers or equivalent.",
        "Caps playing field has to be 287cm wide.",
        "Both sides of the field should feature solid rest for the back.",
        "Shooting player back has to always contact the wall behind.",
        "Playing field should be free of any obstacles. If any of the players get up, field is invaded by a person not participating in the game," +
        "or other conditions that could impact the game result arise the game is stopped and resumed after the came can continue unobstructed.",
        "Tumblers have to be placed mid thigh opposite to each other on the center axis of the playing field.",
        "Playing field should have two towels or rags under tumblers to prevent beer being spilled on the floor."
    ]

    const scoringRules = [
        "Sink is a cap landing in one of the tumblers and staying there after motion stops.",
        "All caps sunk by players count as valid sinks (even if bounced of surfaces or players), as long as they sit in their places with their back against the wall or other rules were not violated (eg. rules from section 'Conflict resolution').",
        "After cap lands in one of the tumblers (sink is awarded), receiving team has one chance of rebuttal (unless rule violation occurred), rebuttal is taken by next player in shooting order.",
        "Rebuttal happens when receiving team is able to put a cap in opposing teams tumbler after receiving a cap in their tumbler. " +
        "After that rebuttal situation reverses and the team that landed cap first becomes receiving and they have to land a cap themselves. " +
        "If they manage to do so rebuttal chain continues if they don't scoring rules below apply.",
        "Score is awarded when on of the teams manages to land a cap in opposing team tumbler and the throw is not rebutted or rebuttal chain is broken (team that broke it loses one point).",
        "Caps accidentally placed by the team in their own tumbler immediately count as a point for the opposing team and next shot is performed by opposing team player in accordance to shooting order.",
    ]

    const conflictRules = [
        "If one of the players violates shooting order, game continues according to shooting order. The player that" +
        "committed the violation is skipped during his next turn.",
        "If shooting order is consecutively violated by both teams no sinks are awarded and no players are skipped. Team representatives " +
        "play a Rock Paper Scissors game and winning team chooses a player that continues the game. After that player shoots next ones go according to the shooting order",
        "If beer is spilled during the game spilling team loses as many points as would correspond to the usual drinking progress " +
        "(eg. if a team spills half of their their beer they lose half of the points corresponding to that beer). Spilling team has to have at least one point left " +
        "in a beer and they need to lose at least one point, unless it would violate the one sip left rule (game cannot end with the beer being spilled). If teams cannot agree on" +
        "how many sips were spilled game of Rock Paper Scissors decides who is right",
        "All conflicts that occurred and their resolution is not present in these rules are resolved by Rock Paper Scissors game between the teams " +
        "winning team decides who is right (resolution obtained this way cannot violate any rules in this rulebook)."
    ]

    const goodManners = [
        "Scoring team should perform a celebration after scoring a point.",
        "Winning team should pick up the losing team from their places."
    ]


    let index = 0;

    return (
        <div>
            <PageHeader title={'RulesScreen'} icon={<GavelIcon fontSize={"large"}/>}/>
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <div className={classes.standardBorder} style={{maxWidth: 800}}>
                    <Typography color={"primary"} variant={"h4"}>Official Game of Caps rulebook</Typography>
                    <div className={styles.section}>
                        <Typography>Official Game of Caps rulebook originally written down on October 16 2020 by Mike.
                            Any changes made after October 16 2020 will be marked as modifications and stated in rules
                            version
                            section.
                        </Typography>
                    </div>
                    <div className={styles.section}>
                        <Typography>
                            Any player who wants to play a complete game that counts as a valid singles game and changes
                            singles
                            player rating
                            has to follow these rules while playing. Other game variants like easy caps, unranked or
                            doubles
                            follow the rules
                            more loosely (differences will be marked and explained).
                        </Typography>
                    </div>
                    <Typography variant={"h5"}>Basic rules</Typography>

                    {basicRules.map(rule => {
                            index++;
                            return <div className={styles.section} key={rule}>
                                <Typography>{index}. {rule}</Typography>
                            </div>
                        }
                    )}
                    <Typography variant={"h5"}>Playing field rules</Typography>

                    {fieldRules.map(rule => {
                            index++;
                            return <div className={styles.section} key={rule}>
                                <Typography>{index}. {rule}</Typography>
                            </div>
                        }
                    )}

                    <Typography variant={"h5"}>Scoring rules</Typography>

                    {scoringRules.map(rule => {
                            index++;
                            return <div className={styles.section} key={rule}>
                                <Typography>{index}. {rule}</Typography>
                            </div>
                        }
                    )}

                    <Typography variant={"h5"}>Conflict resolution</Typography>

                    {conflictRules.map(rule => {
                            index++;
                            return <div className={styles.section} key={rule}>
                                <Typography>{index}. {rule}</Typography>
                            </div>
                        }
                    )}

                    <Typography variant={"h5"}>Good manners</Typography>

                    {goodManners.map(rule => {
                            index++;
                            return <div className={styles.section} key={rule}>
                                <Typography>{index}. {rule}</Typography>
                            </div>
                        }
                    )}

                    <Typography color={"primary"} variant={"h4"}>Rules version</Typography>
                    <div className={styles.section}>
                        <Typography>16.10.2020 - Caps rules written down.
                        </Typography>
                    </div>
                </div>
            </div>
        </div>
    );
};

const useStyles = makeStyles(theme => ({
    section: {
        padding: 10
    }
}))

export default RulesScreen;

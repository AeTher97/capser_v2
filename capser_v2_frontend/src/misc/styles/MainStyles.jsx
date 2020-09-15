import makeStyles from "@material-ui/core/styles/makeStyles";

const mainStyles = makeStyles(theme => ({
    root: {
        padding: 15,
        display: "flex",
        flexDirection: "column"
    },
    header: {
        display: "flex",
        flexDirection: "row",
        flex: 1,
        alignItems: "center",
    },
    content: {
        flex: 1,
        maxWidth: 900
    },
    leftOrientedWrapper: {
        padding: 20,
        display: "flex",
        flexDirection: "column",
        alignItems: "flex-start"
    },
    leftOrientedWrapperNoPadding: {
        display: "flex",
        flexDirection: "column",
        alignContent: "stretch"
    },
    twoColumnLayout: {
        display: "flex",
        flexDirection: "row",
        flex: 1,
    },
    textHeading: {
        paddingBottom: 10,
        paddingTop: 10
    },
    textSubheading: {
        paddingTop: 5,
        paddingBottom: 5
    },
    text: {
        paddingTop: 10
    },
    paddedContent: {
        flex: 1,
        padding: 10
    },
    column: {
        paddingTop: 20,
        paddingBottom: 20,
        display: 'flex',
        flexDirection: "column",
        alignItems: "center",

    },
    margin: {
        margin: 10
    },
    width200: {minWidth: 200},
    height700: {
        [theme.breakpoints.up('md')]: {
            minHeight: 700
        }
    },
    squareShine: {
        backgroundImage: `url(/reflection.svg)`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'bottom right',
        backgroundSize: 'cover',
    },
    horizontalShine: {
        backgroundImage: `url(/horizontalShine.svg)`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'bottom right',
        backgroundSize: 'cover',
    },
    neon: {
        boxShadow: '0 0 5px 2px rgba(255,255,255,0.4)',
    },
    redNeon: {
        boxShadow: '0 0 5px 2px rgba(255,0,0,0.4)',
    },
    empty: {}

}))


export default mainStyles;

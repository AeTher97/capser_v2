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
        paddingBottom: 1,
        paddingTop: 1
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
            minHeight: '35vw'
        }
    },
    height1000: {
        [theme.breakpoints.up('md')]: {
            minHeight: '40vw'
        }
    },
    neon: {
        boxShadow: '0 0 5px 2px rgba(255,255,255,0.4)',
    },
    redNeon: {
        boxShadow: '0 7px 5px -4px rgba(255,0,0,0.4)',
    },
    link: {
        color: 'red',
        '&:hover': {
            cursor: 'pointer',
            textDecoration: "underline red"
        }
    },
    neonTooltip: {
        padding: 4,
        boxShadow: '0 0 5px 2px rgba(255,255,255,0.4)',

    },
    centeredRow: {
        display: "flex",
        flexDirection: "row",
        flex: 1,
        justifyContent: "center",
        alignItems: 'center'
    },
    centeredRowNoFlex: {
        display: "flex",
        flexDirection: "row",
        justifyContent: "flex-start",
        alignItems: 'center'
    },
    centeredColumn: {
        display: "flex",
        flexDirection: "column",
        flex: 1,
        alignItems: "center"
    },
    empty: {},
    standardBorder: {
        border: "1px solid #5a6572",
        borderRadius: 6,
        padding: 15,
        margin: 10
    },
    disabledBorder: {
        border: "1px solid #1e2126",
        borderRadius: 6,
        padding: 15
    },
    twichHighlight: {
        margin: 10,
        '&:hover': {backgroundColor: 'rgba(255,255,255,0.15)'},
        borderRadius: 7
    },
    twichZoom: {
        borderRadius: 7,
        position: 'relative',
        left: 0,
        top: 0,
        transition: "all 0.2s",
        '&:hover': {
            left: 5,
            top: -7,

        }
    }

}))


export default mainStyles;

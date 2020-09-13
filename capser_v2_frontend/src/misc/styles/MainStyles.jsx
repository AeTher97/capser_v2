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
        alignItems:"center",
    },
    content: {
        flex : 1,
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
        // alignItems: "flex-start",
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
        display: 'flex',
        flexDirection: "column",
        alignItems: "flex-start"

    }

}))



export default mainStyles;

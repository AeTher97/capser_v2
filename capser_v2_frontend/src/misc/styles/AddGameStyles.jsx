import mainStyles from "./MainStyles";
import makeStyles from "@material-ui/core/styles/makeStyles";

const addGameStyles = makeStyles(theme => ({
    shine: {
        backgroundImage: `url(/frame.png)`,
        backgroundRepeat: 'repeat',
        backgroundPosition: 'center',
        backgroundSize: 'cover'
    },
    redShine: {
        backgroundImage: `url(/redShine.png)`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'right'
    },

    blueShine: {
        backgroundImage: `url(/blueShine.png)`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'right'
    }
}))

export default addGameStyles;

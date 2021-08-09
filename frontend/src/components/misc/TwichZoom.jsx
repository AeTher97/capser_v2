import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";

const TwichZoom = ({children, onClick,border = true, margin = true}) => {

    const mainStyles0 = mainStyles();
    return (
            <div style={{backgroundColor: 'red', borderRadius: 7, margin:margin ? 10 : 0, cursor: 'pointer'}} onClick={onClick}>
                <div className={[mainStyles0.twichZoom,border ? mainStyles0.standardBorder : ''].join(' ')} style={{margin: 0}}>{children}</div>
            </div>
    );
};

export default TwichZoom;

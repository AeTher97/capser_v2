import React from 'react';
import mainStyles from "../../misc/styles/MainStyles";

const ZoomElement = ({children}) => {

    const mainStyles0 = mainStyles();
    return (
        <>
            <div style={{backgroundColor: 'red', borderRadius: 7}}>
                <div className={mainStyles0.twichZoom}>{children}</div>
            </div>
        </>
    );
};

export default ZoomElement;

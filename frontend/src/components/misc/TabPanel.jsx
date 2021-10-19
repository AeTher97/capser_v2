import React from 'react';

const TabPanel = ({children, showValue, value, ...other}) => {
    if (value === showValue) {
        return (
            <div
                role={"tabpanel"}
                {...other}>
                {children}
            </div>)
    } else {
        return <></>;
    }

};


export default TabPanel;

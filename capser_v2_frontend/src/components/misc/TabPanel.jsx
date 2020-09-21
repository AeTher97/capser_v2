import React from 'react';

const TabPanel = ({children, showValue, value, ...other}) => {
    return (
        <div
            role={"tabpanel"}
            hidden={value !== showValue}
            {...other}>
            {children}
        </div>)

};



export default TabPanel;
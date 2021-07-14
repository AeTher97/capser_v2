import React from 'react';

const CenteredColumn = ({children}) => {
    return (
        <div style={{display: "flex", flex: 1, flexDirection: 'column', alignItems: "center", width: '100%'}}>
            {children}
        </div>
    );
};


CenteredColumn.propTypes = {};

export default CenteredColumn;
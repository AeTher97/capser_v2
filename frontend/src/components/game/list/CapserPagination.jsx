import React from 'react';
import mainStyles from "../../../misc/styles/MainStyles";
import {useTheme} from "@material-ui/core";

const CapserPagination = ({currentPage, onChange, pageCount, edgeButtons = true}) => {
    const mainStyle = mainStyles();
    const theme = useTheme();

    const active = {
        cursor: 'pointer',
        padding: 3,
        margin: 1
    }

    const disabled = {
        ...active,
        visibility: 'hidden'
    }


    return (
        <div style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
            alignItems: 'center',
            color: theme.palette.primary.main,
            fontWeight: 'bold'
        }}>
            {edgeButtons && <div style={currentPage !== 1 ? active : disabled} className={mainStyle.twichHighlight}
                                 onClick={() => onChange(1)}>{"<<"}</div>}
            <div style={currentPage !== 1 ? active : disabled} className={mainStyle.twichHighlight}
                 onClick={() => onChange(currentPage - 1)}>{"<"}</div>
            {pageCount !== 1 && pageCount !== 0 && <div style={{margin: 3}}>
                {currentPage}/{pageCount}
            </div>}
            {<div style={(currentPage !== pageCount && pageCount > 0) ? active : disabled}
                  className={mainStyle.twichHighlight}
                  onClick={() => onChange(currentPage + 1)}>></div>}
            {edgeButtons &&
            <div style={(currentPage !== pageCount && pageCount > 0) ? active : disabled}
                 className={mainStyle.twichHighlight}
                 onClick={() => onChange(pageCount)}>>></div>}
        </div>
    );
};

CapserPagination.propTypes = {};

export default CapserPagination;

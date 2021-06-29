import React from 'react';
import Button from "@material-ui/core/Button";

const CapserPagination = ({currentPage, onNext, onPrevious, minPage = 1, pageCount}) => {
    return (
        <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
            {currentPage !== 1 && <Button variant={"text"} onClick={onPrevious}>{"< Previous"}</Button>}
            {currentPage !== pageCount && <Button variant={"text"} onClick={onNext}>Next ></Button>}
        </div>
    );
};

CapserPagination.propTypes = {};

export default CapserPagination;

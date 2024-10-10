import React from 'react';

const FillingDots = ({count, filledCount}) => {

    const dotsArray = [];

    for(let i=0;i<count;i++){
        dotsArray.push({
            filled: i < filledCount
        })
    }

    return (
        <div style={{display: "flex", gap: 3}}>
            {dotsArray.map(dot => {
                return <div
                    style={{height: 8, width: 8, borderRadius: 5, border: "1px solid red", backgroundColor: dot.filled ? "red" : "transparent"}}
                />
            })}
        </div>
    );
};

export default FillingDots;
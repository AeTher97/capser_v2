import React from 'react';
import {SvgIcon} from "@material-ui/core";


export const PointIcon = (props) => {
    const color = props.color;
    return (
        <SvgIcon {...props}>
            <svg id="Warstwa_1" data-name="Warstwa 1" xmlns="http://www.w3.org/2000/svg"
                 viewBox="0 0 900 900">

                <title>Point</title>
                <path fill={color}
                      d="M450,50A400.12,400.12,0,0,1,605.68,818.59,400.12,400.12,0,0,1,294.32,81.41,397.51,397.51,0,0,1,450,50m0-50C201.47,0,0,201.47,0,450S201.47,900,450,900,900,698.53,900,450,698.53,0,450,0Z"/>
                <path fill={color} d="M445.62,405.11v77.44H216.46V405.11ZM370.08,572.69h-78.4V315h78.4Z"/>
                <path fill={color} d="M625.25,656.47H542.73V290.85l-57.44,24.44V226.74L548.13,195h77.12Z"/>
            </svg>
        </SvgIcon>
    );
};

export const RebuttalIcon = (props) => {
    const color = props.color;
    return (
        <SvgIcon {...props}>
            <svg id="Warstwa_1" data-name="Warstwa 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 900 900">
                <title>Rebuttal</title>
                <path fill={color}
                      d="M450,50A400.12,400.12,0,0,1,605.68,818.59,400.12,400.12,0,0,1,294.32,81.41,397.51,397.51,0,0,1,450,50m0-50C201.47,0,0,201.47,0,450S201.47,900,450,900,900,698.53,900,450,698.53,0,450,0Z"/>
                <path fill={color}
                      d="M242.33,212V565.9C242.33,629.8,351.69,749,450,749c97.08,0,207.67-108.14,207.67-161V218.14S527.43,273.44,450,273.44C368.9,273.44,242.33,212,242.33,212Z"/>
            </svg>
        </SvgIcon>
    );
};

export const SinkIcon = (props) => {
    const color = props.color;
    return (
        <SvgIcon {...props}>
            <svg id="Warstwa_1" data-name="Warstwa 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 900 900">
                <title>Sink</title>
                <path fill={color}
                      d="M450,50A400.12,400.12,0,0,1,605.68,818.59,400.12,400.12,0,0,1,294.32,81.41,397.51,397.51,0,0,1,450,50m0-50C201.47,0,0,201.47,0,450S201.47,900,450,900,900,698.53,900,450,698.53,0,450,0Z"/>
                <polygon fill={color} points="157 345 450 570.2 750 345 750 430.48 453 666 157 435.64 157 345"/>
            </svg>
        </SvgIcon>
    );
};



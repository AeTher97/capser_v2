import {ReactComponent as SinglesIconSvg} from "../../utils/icons/singlesIcon.svg";
import {ReactComponent as DoublesIconSvg} from "../../utils/icons/doublesIcon.svg";
import {ReactComponent as EasyIconSvg} from "../../utils/icons/easyIcon.svg";
import {ReactComponent as UnrankedIconSvg} from "../../utils/icons/unrankedIcon.svg";
import {SvgIcon} from "@material-ui/core";
import React from 'react';


export const SinglesIcon = (props) => {
    return (<SvgIcon {...props}>
        <SinglesIconSvg/>
    </SvgIcon>)
}

export const DoublesIcon = (props) => {
    return (<SvgIcon {...props}>
        <DoublesIconSvg/>
    </SvgIcon>)
}

export const EasyIcon = (props) => {
    return (<SvgIcon {...props}>
        <EasyIconSvg/>
    </SvgIcon>)
}

export const UnrankedIcon = (props) => {
    return (<SvgIcon {...props}>
        <UnrankedIconSvg/>
    </SvgIcon>)
}
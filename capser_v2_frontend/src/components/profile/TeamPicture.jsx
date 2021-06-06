import React from 'react';
import ProfilePicture from "./ProfilePicture";

const TeamPicture = ({size='large',player1Hash, player2Hash,overrideSize, overrideOverlap}) => {

    let baseSize;
    let overLap = overrideOverlap || 0.5;

    switch (size) {
        case 'large':
            baseSize = 160;
            break;
        case 'medium':
            baseSize = 80;
            break;
        case 'small':
            baseSize = 40;
            break
        case 'tiny':
            baseSize = 30;
            break;
        case 'micro':
            baseSize = 10;
            break;
        default:
            baseSize = 160;
    }

    return (
        <div style={{width: baseSize + baseSize - baseSize*overLap+20, height: baseSize + baseSize * overLap /2+20, padding: 5}}>
            <div style={{zIndex: 100, position: 'relative', top: baseSize * overLap/2}}>
            <ProfilePicture size={size} avatarHash={player1Hash}/>
            </div>
            <div style={{zIndex:0, position:'relative',left: baseSize -baseSize*overLap, top: -baseSize-10}}>
            <ProfilePicture size={size} avatarHash={player2Hash}/>
            </div>
        </div>
    );
};

export default TeamPicture;
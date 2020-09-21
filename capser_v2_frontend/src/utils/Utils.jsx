export const getGameModeString  = (string) => {
    switch (string) {
        case 'SUDDEN_DEATH' :
            return 'Sudden Death'
        case 'OVERTIME' :
            return 'Overtime'
    }
}


export const getGameTypeString  = (string) => {
    switch (string) {
        case 'SINGLES' :
            return 'Singles'
        case 'EASY_CAPS' :
            return 'Easy Caps'
        case 'DOUBLES' :
            return 'Doubles'
        case 'UNRANKED' :
            return 'Unranked'
    }
}

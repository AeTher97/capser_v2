export const getGameModeString = (string) => {
    switch (string) {
        case 'SUDDEN_DEATH' :
            return 'Sudden Death'
        case 'OVERTIME' :
            return 'Overtime'
    }
}


export const getGameTypeString = (string) => {
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

export const getRequestGameTypeString = (type) => {

    switch (type) {
        case 'SINGLES' :
            return 'singles'
        case 'EASY_CAPS' :
            return 'easy'
        case 'DOUBLES' :
            return 'doubles'
        case 'UNRANKED' :
            return 'unranked'
    }

}

export const getStatsString = (type) => {
    switch (type) {
        case 'SINGLES' :
            return 'userSinglesStats'
        case  'EASY_CAPS' :
            return 'userEasyStats'
        case 'UNRANKED' :
            return 'userUnrankedStats'
        case 'DOUBLES' :
            return 'userDoublesStats'
    }

}

export const getGameModeString  = (string) => {
    switch (string) {
        case 'SUDDEN_DEATH' :
            return 'Sudden Death'
        case 'OVERTIME' :
            return 'Overtime'
    }
}

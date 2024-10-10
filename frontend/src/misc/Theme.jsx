import Fade from "@material-ui/core/Fade";
import red from "@material-ui/core/colors/red";
import {grey} from "@material-ui/core/colors";
import {createTheme} from "@material-ui/core";


export const baseDarkTheme = createTheme({
    palette: {
        primary: red,
        secondary: grey,
        text: {
            primary: 'rgba(255,255,255,0.87)',
            secondary: 'rgba(255,255,255,0.54)',
            disabled: 'rgba(255,255,255,0.38)',
            hint: 'rgba(255,255,255,0.38)'

        }
    },
})


const FakeTransitionComponent = ({children}) => children;

const dividerDark = 'rgba(255, 255, 255, 0.8)'
const paperColor = 'rgb(29,29,29)'


const getOverrides = (divider, baseTheme, type) => {
    return {
        MuiButton: {
            root: {
                textTransform: "none",
                padding: '2px 15px 2px 15px',
                borderRadius: 7,
                backgroundColor: baseTheme.palette.primary.main,
                color: baseTheme.palette.text.primary
            },
            textPrimary: {
                color: baseTheme.palette.text.primary
            },
            outlinedPrimary: {
                color: divider,
                padding: '2px 15px 2px 15px',
                border: '1px solid ' + '#626875',
                backgroundColor: "#35383e",
                "&:hover": {
                    border: '1px solid ' + 'white',
                    backgroundColor: "#555656",

                }
            }
        },
        MuiInputLabel: {
            formControl: {
                transform: 'translate(0, 8px) scale(1)'
            },
            outlined: {
                transform: 'translate(10px, 9px) scale(1)'

            },

        },
        MuiOutlinedInput: {
            input: {padding: 6, paddingLeft: 10, height: 20},


        },
        MuiInput: {
            formControl: {
                'label +&': {
                    marginTop: 0,
                },
                '&': {
                    marginTop: 0
                }
            },

            underline: {
                "&&&&:hover:before": {
                    borderBottom: "1px solid " + divider
                },
                '&&&&:before': {
                    borderBottom: "1px solid " + divider,
                    transition: 'none'
                },
                '&&&&:after': {
                    borderBottom: "1px solid " + baseTheme.palette.primary,
                    transition: 'none'
                }

            }
        },
        MuiToolbar: {
            root: {
                borderBottom: "1px solid " + divider
            }
        },
        MuiLink: {
            underlineHover: {
                fontWeight: 500,
                '&:hover': {
                    color: baseTheme.palette.secondary.main,
                    cursor: 'pointer'
                }
            }
        },
        MuiMenu: {},

        MuiTooltip: {
            tooltip: {
                padding: 0,
                backgroundColor: 'rgba(52,52,52,1)'
            },
            touch: {
                padding: 0
            }
        },
        MuiIconButton: {
            root: {
                padding: 10,
                '&:hover': {
                    backgroundColor: 'rgba(0,0,0,0)'
                }
            }
        },
        MuiAccordion: {
            root: {
                backgroundColor: "transparent",
                '&$expanded': {
                    margin: 0,
                    '&:first-child': {
                        marginTop: 0,
                    },
                },
                '&:before': {
                    backgroundColor: paperColor,
                }
            }
        },
        MuiAccordionSummary: {
            root: {
                backgroundColor: 'transparent',
                minHeight: 0,
                padding: 0,
                '&$expanded': {
                    minHeight: 0,
                },

            },
            content: {
                margin: 5,
                '&$expanded': {
                    margin: 5
                }
            }
        },
        MuiAccordionDetails: {
            root: {
                backgroundColor: 'transparent',
                padding: "0 0 0 60px"
            }
        },
        MuiTableRow: {



        },
        MuiTableCell: {
            root: {
                borderBottom: "none"
            }

        },
        MuiTabs: {
            root: {
                minHeight: 34,
                borderBottom: "1px solid " + '#353b43',
                color: 'white'
            },
            indicator: {
                backgroundColor: 'transparent',
            },
        },
        MuiTab: {
            root: {
                minHeight: 24,
                textTransform: 'none',
                fontWeight: baseTheme.typography.fontWeightBold,
                marginRight: baseTheme.spacing(1),
                borderBottom: "3px solid transparent",
                '&$selected': {
                    opacity: 1,
                    borderBottom: "3px solid " + baseTheme.palette.primary.main
                },
                '&:hover': {
                    opacity: 0.9,
                    borderBottom: "3px solid " + 'rgb(23,58,79)'
                },
                '&$selected:hover': {
                    opacity: 1,
                    borderBottom: "3px solid " + baseTheme.palette.primary.main
                },
            }
        },
        MuiPaginationItem: {
            root: {
                minWidth: 22,
                height: 22,
                borderRadius: 5
            },
            rounded: {
                borderRadius: 0,
                border: "1px solid " + baseTheme.palette.divider,
            },
            page: {
                transition: "none",
                '&$selected': {
                    backgroundColor: baseTheme.palette.action.selected,
                    border: "1px solid " + baseTheme.palette.primary.main,
                },
                '&:hover': {
                    backgroundColor: 'rgba(255,255,255,0.2)',
                },
            }
        },
        MuiTypography: {
            root: {
                color: baseTheme.palette.text.primary
            }

        },
        MuiCard: {
            root: {
                padding: 10,
                border: "1px solid " + 'rgba(255,255,255,0.1)',
                backgroundColor: paperColor,
            }
        }
        ,
        MuiLinearProgress: {
            bar: {transition: 'none'},
            bar1Determinate: {
                transition: 'none',
            },
        },
        MuiDrawer: {
            root: {
                minHeight: '100vh'
            },
            docked: {
                minHeight: 0
            }
        },
        MuiTextField: {
            root: {borderRadius: 10}
        }


    }
}

const getProps = (baseTheme) => {
    return {
        MuiButton: {
            disableRipple: true,
            variant: "text",
            color: "primary",
            disableElevation: true,
            size: "small"
        },
        MuiTextField: {
            InputLabelProps: {
                shrink: false,
                disableAnimation: true,
            },
            variant: 'outlined'
        },
        MuiOutlinedInput: {
            root: {
                padding: 0
            }

        },
        MuiAppBar: {
            color: "transparent",
            elevation: 0,
        },
        MuiIconButton: {
            disableRipple: true,
            disableFocusRipple: true
        },
        MuiMenu: {
            elevation: 2,
            TransitionComponent: Fade
        },
        MuiCheckbox: {
            disableRipple: true,
            color: "primary",
            disableFocusRipple: true,
            disableTouchRipple: true
        },
        MuiAccordion: {
            elevation: 0
        },
        MuiTab: {
            disableRipple: true
        },
        MuiList: {
            disablePadding: true
        },
        MuiButtonBase: {
            disableRipple: true
        },
        MuiTableCell: {
            size: "medium"
        },
        MuiTooltip: {
            elevation: 2,
            TransitionComponent: FakeTransitionComponent
        },
        MuiCard: {
            elevation: 0
        },
        MuiSelect: {
            variant: 'outlined',
            displayEmpty: true,
        }


    }
}


export const darkTheme = createTheme({
    palette: {
        type: "dark",
        primary: red,
        secondary: grey,
        background: {
            default: '#0a1018',
            paper: 'rgb(0,19,31)'
        },
        divider: '#353b43',
        action: {
            hoverOpacity: 0.7
        }
    },

    overrides: getOverrides(dividerDark, baseDarkTheme, "dark"),
    shape: {
        borderRadius: 7
    },
    props: getProps(baseDarkTheme)
})

import React from 'react'
import { Button, makeStyles } from '@material-ui/core';


const useStyles = makeStyles(theme => ({
    root: {
        minWidth: 0,
        margin: theme.spacing(0.5)
    },
    secondary: {
        backgroundColor: "wheat",
        '& .MuiButton-label': {
            color: "#e53935",
        },
        '&:hover': {
            backgroundColor: "#8f7172ea",
        }
    },
    primary: {
        backgroundColor: "wheat",
        '& .MuiButton-label': {
            color: "#212121",
        },
        '&:hover': {
            backgroundColor: "#8f7172ea",
        }
    },
}))

export default function ActionButton(props) {

    const { color, children, onClick } = props;
    const classes = useStyles();

    return (
        <Button
            className={`${classes.root} ${classes[color]}`}
            onClick={onClick}>
            {children}
        </Button>
    )
}
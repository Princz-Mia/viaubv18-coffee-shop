import React from 'react';
import classes from './loadingSpinner.module.css';
import { useLoading } from '../../hooks/useLoading';

export default function LoadingSpinner() {
    const { isLoading } = useLoading();
    
    if (!isLoading) return null;

    return (
        <div className={classes.container}>
            <div className={classes.cup}>
                <div className={classes.handle}></div>
            </div>
        </div>
    );
}
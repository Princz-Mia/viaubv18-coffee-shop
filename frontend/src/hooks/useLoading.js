import React, { createContext, useContext, useState } from 'react';

const LoadingContext = createContext();

export const useLoading = () => useContext(LoadingContext);

export const LoadingProvider = ({ children }) => {
    const [isLoading, setIsLoading] = useState(false);
    let loadingCount = 0;

    const showLoading = () => {
        loadingCount += 1;
        setIsLoading(true);
    };

    const hideLoading = () => {
        loadingCount -= 1;
        if (loadingCount <= 0) {
            loadingCount = 0;
            setIsLoading(false);
        }
    };

    return (
        <LoadingContext.Provider value={{ isLoading, showLoading, hideLoading }}>
            {children}
        </LoadingContext.Provider>
    );
};

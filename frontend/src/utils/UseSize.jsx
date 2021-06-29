import {useEffect, useState} from "react";

export const useWindowSize = (frequency = 100) => {
    const [windowSize, setWindowSize] = useState({
        width: undefined,
        height: undefined,
    });
    const [callback, setCallback] = useState(null);

    useEffect(() => {
        // Handler to call on window resize

        function handleResize() {
            // Set window width/height to state
            setWindowSize({
                width: window.innerWidth,
                height: window.innerHeight,
            });

        }

        const timeoutFunction = () => {
            const timeout = setTimeout(handleResize, frequency);
            setCallback(timeout);

        }


        // Add event listener
        window.addEventListener("resize", timeoutFunction);

        // Call handler right away so state gets updated with initial window size
        handleResize();

        // Remove event listener on cleanup
        return () => {
            window.removeEventListener("resize", handleResize);
            clearTimeout(callback);
        }
    }, []); // Empty array ensures that effect is only run on mount

    return windowSize;
}

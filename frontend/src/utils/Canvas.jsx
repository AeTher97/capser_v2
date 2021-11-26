import React, {useEffect, useRef} from 'react';

const Canvas = ({drawFunction}) => {

    const canvasRef = useRef();
    const containerRef = useRef();

    useEffect(() => {
        if (canvasRef.current) {
            const context = canvasRef.current.getContext('2d');
            resizeCanvas(canvasRef.current)
            let frameCount = 0;
            let animationFrameId;

            const render = () => {
                frameCount++;
                drawFunction(context, frameCount);
                animationFrameId = window.requestAnimationFrame(render);
            }

            render();
            return () => {
                window.cancelAnimationFrame(animationFrameId);
            }

        }
    }, [drawFunction])


    function resizeCanvas(canvas) {
        const {width, height} = canvas.getBoundingClientRect()

        if (canvas.width !== width || canvas.height !== height) {
            const {devicePixelRatio: ratio = 1} = window
            const context = canvas.getContext('2d')
            canvas.width = width * ratio
            canvas.height = height * ratio
            context.scale(ratio, ratio)
            return true
        }

        return false
    }

    return (
        <div ref={containerRef}>
            <canvas ref={canvasRef} style={{width: '100%', height: '100%'}}/>
        </div>
    );
};

export default Canvas;
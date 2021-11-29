import React, {useEffect, useRef} from 'react';

var x;
var y;

const Canvas = React.memo(({drawFunction, setPosition}) => {

    const canvasRef = useRef();
    const containerRef = useRef();

    useEffect(() => {
        if (canvasRef.current) {
            const listener = (e) => {
                x = e.clientX;
                y = e.clientY;
            };

            window.addEventListener('mousemove', listener, true);

            const context = canvasRef.current.getContext('2d');
            resizeCanvas(canvasRef.current)
            let frameCount = 0;
            let animationFrameId;

            const render = () => {
                frameCount++;
                const box = canvasRef.current.getBoundingClientRect();
                if (x - box.left > 0 && x - box.left < box.width && y - box.top > 0 && y - box.top < box.height) {
                    drawFunction(context, frameCount, {x: x - box.left, y: y - box.top});
                } else {
                    drawFunction(context, frameCount);
                }
                animationFrameId = window.requestAnimationFrame(render);
            }

            render();
            return () => {
                window.cancelAnimationFrame(animationFrameId);
                window.removeEventListener('mousemove', listener, true);
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
});

export default Canvas;
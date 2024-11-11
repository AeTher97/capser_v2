import React from 'react';
import Canvas from "../../utils/Canvas";

const Plot = React.memo(({seriesData}) => {

    const lineColor = 'rgba(255,255,255,0.4)';
    const textColor = 'rgba(255,255,255,1)';

    const dateOptions = {month: 'long', day: 'numeric'};

    const verticalAxisOffset = 30;
    const horizontalAxisOffset = 30;

    if (seriesData == null) {
        return;
    }


    const roundRect = (ctx, x, y, width, height, radius, fill, stroke) => {
        if (typeof stroke === "undefined") {
            stroke = true;
        }
        if (typeof radius === "undefined") {
            radius = 5;
        }
        if (typeof radius === 'number') {
            radius = {tl: radius, tr: radius, br: radius, bl: radius};
        } else {
            const defaultRadius = {tl: 0, tr: 0, br: 0, bl: 0};
            for (let side in defaultRadius) {
                radius[side] = radius[side] || defaultRadius[side];
            }
        }
        ctx.beginPath();
        ctx.moveTo(x + radius.tl, y);
        ctx.lineTo(x + width - radius.tr, y);
        ctx.quadraticCurveTo(x + width, y, x + width, y + radius.tr);
        ctx.lineTo(x + width, y + height - radius.br);
        ctx.quadraticCurveTo(x + width, y + height, x + width - radius.br, y + height);
        ctx.lineTo(x + radius.bl, y + height);
        ctx.quadraticCurveTo(x, y + height, x, y + height - radius.bl);
        ctx.lineTo(x, y + radius.tl);
        ctx.quadraticCurveTo(x, y, x + radius.tl, y);
        ctx.closePath();
        if (fill) {
            ctx.fill();
        }
        if (stroke) {
            ctx.stroke();
        }

    }

    const drawContent = (ctx, frame, series, actualLength, min, max) => {
        const width = ctx.canvas.scrollWidth;
        const height = ctx.canvas.scrollHeight;
        const lastElement = seriesData.lastElement;

        const step = frame > 100 ? 100 : frame;


        const center = (max + min) / 2;
        let span = (max - min);
        if (span === 0) {
            span = 1;
        }
        const horizontalStepSize = (width - 10 - horizontalAxisOffset) / actualLength;
        const lineHeight = height - verticalAxisOffset - 2;

        ctx.strokeStyle = 'rgb(255,0,0)'
        ctx.lineWidth = 2.0;
        ctx.beginPath();

        let startingValue = series.data[(lastElement + 1) % 365];
        if (startingValue === -100000) {
            startingValue = series.data[0];
        }

        const startingVerticalOffset = 2 + lineHeight / 2
            - ((startingValue - center) / span * lineHeight);


        ctx.moveTo(horizontalAxisOffset, startingVerticalOffset);
        for (let i = 0; i < 365; i++) {
            const seriesValue = series.data[(lastElement + i) % 365];
            if (seriesValue === -100000) {
                continue;
            }
            ctx.lineTo(horizontalAxisOffset + horizontalStepSize * i,
                2 + (lineHeight / 2 - ((seriesValue - center) / span)
                    * lineHeight));
        }

        ctx.stroke();

    }

    const drawAxes = (ctx, series, actualLength, min, max) => {
        const width = ctx.canvas.scrollWidth;
        const height = ctx.canvas.scrollHeight;


        let span = (max - min);
        if (span === 0) {
            span = 1;
        }

        const verticalAxisPoints = Math.floor((height - horizontalAxisOffset) / 40);


        ctx.fillStyle = textColor;


        const verticalLabels = [];
        for (let i = 0; i < verticalAxisPoints; i++) {
            verticalLabels.push(min + span / verticalAxisPoints * i);
        }


        for (const element of verticalLabels) {
            let text;
            if (Math.abs(element) < 10) {
                text = element.toFixed(2);
            } else if (Math.abs(element) < 100) {
                text = element.toFixed(1);
            } else {
                text = element.toFixed(0);
            }
            ctx.fillText(text, 1, 3 + (height - horizontalAxisOffset) - (element - min) / span * (height - horizontalAxisOffset));
        }

        ctx.strokeStyle = lineColor;
        ctx.lineWidth = 0.2;
        for (let i = 0; i < 365; i++) {
            ctx.beginPath()
            ctx.moveTo(verticalAxisOffset, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.lineTo(width, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.stroke();
        }


        const startDateDays = Math.floor(new Date(series.lastLogged).getTime() / 86400000) - actualLength

        const horizontalAxisPoints = Math.floor((width - height - horizontalAxisOffset) / 40);
        const horizontalLabels = [];

        for (let i = 0; i < horizontalAxisPoints; i++) {
            horizontalLabels.push(new Date((startDateDays + Math.floor(i / horizontalAxisPoints * actualLength)) * 86400000));
        }


        for (let i = 0; i < horizontalLabels.length; i++) {
            ctx.fillText(horizontalLabels[i].toLocaleDateString('en-US', dateOptions), verticalAxisOffset + i / horizontalLabels.length * (width - verticalAxisOffset), height - 3)
        }


        ctx.strokeStyle = lineColor;
        ctx.lineWidth = 0.6;
        ctx.beginPath();
        ctx.moveTo(verticalAxisOffset, 0);
        ctx.lineTo(verticalAxisOffset, height - horizontalAxisOffset);
        ctx.lineTo(width, height - horizontalAxisOffset);
        ctx.stroke();
        ctx.closePath();
    }

    const drawTooltip = (ctx, series, actualLength, event, min, max, step) => {
        if (step > 100) {
            step = 100;
        }
        const center = (max + min) / 2;
        let span = (max - min);
        if (span === 0) {
            span = 1;
        }

        const size = ctx.canvas.scrollWidth - horizontalAxisOffset - 10;
        let i = Math.floor((event.x - verticalAxisOffset) / size * actualLength);
        if (i < 0) {
            return;
        }
        let entryPos = (i + series.lastElement) % 365;

        const entry = series.data[entryPos];


        const padding = 10;
        let height = 2 + (ctx.canvas.scrollHeight - horizontalAxisOffset) / 2 - ((entry - center) / span * (ctx.canvas.scrollHeight - horizontalAxisOffset)) * step / 100;
        const circleHeight = height;
        height += 10;
        if (ctx.canvas.scrollHeight - horizontalAxisOffset - height < 30) {
            height -= 50;
        }

        if (entry || entry === 0) {

            ctx.fillStyle = 'rgb(171,171,171)';
            ctx.beginPath();
            ctx.arc(event.x, circleHeight, 3, 50, 0, 2 * Math.PI);
            ctx.fill();

            ctx.fillStyle = 'rgb(51,51,51)';
            ctx.beginPath();
            const tooltipWidth = ctx.measureText(entry.toFixed(2)).width + 20;
            roundRect(ctx, event.x - (ctx.canvas.scrollWidth - event.x < 30 ? 30 : 0) - tooltipWidth / 2,
                height, ctx.measureText(entry.toFixed(2)).width + 20, 35, 7, true, false)
            ctx.fill()


            ctx.fillStyle = textColor;
            ctx.fillText(entry.toFixed(2), event.x - (ctx.canvas.scrollWidth - event.x < 30 ? 30 : 0) - tooltipWidth / 2 + padding, height + padding + 12);
        }
    }


    return (
        <div style={{padding: 5}}>
            <Canvas drawFunction={(ctx, frame, event) => {
                ctx.clearRect(0, 0, ctx.canvas.scrollWidth, ctx.canvas.scrollHeight)
                ctx.font = '13px Arial'

                const actualLength = seriesData.data.filter(obj => obj > -100000).length;
                const min = Math.min(...seriesData.data.filter(obj => obj > -100000));
                const max = Math.max(...seriesData.data);
                const onlyZeros = seriesData.data.filter(obj => obj > -100000).length;

                if (onlyZeros !== 0) {
                    drawAxes(ctx, seriesData, actualLength, min, max);
                    drawContent(ctx, frame, seriesData, actualLength, min, max);
                    if (event) {
                        drawTooltip(ctx, seriesData, actualLength, event, min, max, frame)
                    }
                } else {
                    ctx.fillStyle = textColor;
                    ctx.font = '15px Arial';
                    const width = ctx.measureText('No data').width;

                    ctx.fillText('No data', ctx.canvas.scrollWidth / 2 - width / 2, ctx.canvas.scrollHeight / 2 - 3)
                }


            }}/>
        </div>
    );
});

export default Plot;
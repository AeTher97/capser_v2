import React from 'react';
import Canvas from "../../utils/Canvas";

const NewPlot = ({seriesData}) => {

    const lineColor = 'rgba(255,255,255,0.4)';
    const textColor = 'rgba(255,255,255,1)';

    const dateOptions = {month: 'long', day: 'numeric'};

    const verticalAxisOffset = 30;
    const horizontalAxisOffset = 30;


    const drawContent = (ctx, frame, series, actualLength, min, max) => {
        const width = ctx.canvas.width;
        const height = ctx.canvas.height;

        const step = frame > 100 ? 100 : frame;


        const center = (max + min) / 2;
        const span = (max - min);
        const horizontalOffset = (width - verticalAxisOffset) / actualLength;

        ctx.strokeStyle = 'rgb(255,0,0)'
        ctx.lineWidth = 2.0;
        ctx.beginPath();
        ctx.moveTo(verticalAxisOffset, (height - horizontalAxisOffset) / 2 - ((series.data[0] - center) / span * (height - horizontalAxisOffset)) * step / 100);
        for (let i = 0; i < 365; i++) {
            if (series.data[i] === -100000) {
                continue;
            }
            ctx.lineTo(verticalAxisOffset + horizontalOffset * i, (height - horizontalAxisOffset) / 2 - ((series.data[i] - center) / span * (height - horizontalAxisOffset)) * step / 100);
        }

        ctx.stroke();

    }

    const drawAxes = (ctx, series, actualLength, min, max) => {
        const width = ctx.canvas.width;
        const height = ctx.canvas.height;


        const span = (max - min);

        const verticalAxisPoints = Math.floor((height - horizontalAxisOffset) / 40);


        ctx.fillStyle = textColor;


        const verticalLabels = [];
        for (let i = 0; i < verticalAxisPoints; i++) {
            verticalLabels.push(min + span / verticalAxisPoints * i);
        }


        for (let i = 0; i < verticalLabels.length; i++) {
            let text;
            if (Math.abs(verticalLabels[i]) < 10) {
                text = verticalLabels[i].toFixed(2);
            } else if (Math.abs(verticalLabels[i]) < 100) {
                text = verticalLabels[i].toFixed(1);
            } else {
                text = verticalLabels[i].toFixed(0);
            }
            ctx.fillText(text, 1, 3 + (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
        }

        ctx.strokeStyle = lineColor;
        ctx.lineWidth = 0.2;
        for (let i = 0; i < 365; i++) {
            ctx.beginPath()
            ctx.moveTo(verticalAxisOffset, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.lineTo(width, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.stroke();
        }


        const daysSpan = Math.floor(new Date(series.lastLogged).getTime() / 86400000) - Math.floor(new Date(series.lastLogged).getTime() / 86400000) + actualLength;
        const startDateDays = Math.floor(new Date(series.lastLogged).getTime() / 86400000) - daysSpan

        const horizontalAxisPoints = Math.floor((width - height - horizontalAxisOffset) / 40);
        const horizontalLabels = [];

        for (let i = 0; i < horizontalAxisPoints; i++) {
            horizontalLabels.push(new Date((startDateDays + Math.floor(i / horizontalAxisPoints * daysSpan)) * 86400000));
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


    }

    return (
        <div style={{padding: 5}}>
            <Canvas drawFunction={(ctx, frame) => {
                ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height)
                ctx.font = '12px Arial'

                const actualLength = seriesData.data.filter(obj => obj > -100000).length;
                const min = Math.min(...seriesData.data.filter(obj => obj > -100000));
                const max = Math.max(...seriesData.data);
                const onlyZeros = seriesData.data.filter(obj => obj !== 0 && obj > -100000).length;

                if (onlyZeros !== 0) {

                    drawAxes(ctx, seriesData, actualLength, min, max);
                    drawContent(ctx, frame, seriesData, actualLength, min, max);
                } else {
                    ctx.fillStyle = lineColor;
                    const width = ctx.measureText('No data').width;
                    const height = ctx.measureText('No data').height;
                    ctx.fillText('No data', ctx.canvas.width / 2 - width / 2, ctx.canvas.height / 2 - height / 2)
                }


            }}/>
        </div>
    );
};

export default NewPlot;
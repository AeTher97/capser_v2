import React from 'react';
import Canvas from "../../utils/Canvas";

const NewPlot = ({seriesData}) => {

    const lineColor = 'rgba(255,255,255,0.4)';
    const textColor = 'rgba(255,255,255,1)';

    const dateOptions = {month: 'long', day: 'numeric'};

    const verticalAxisOffset = 30;
    const horizontalAxisOffset = 30;


    const drawContent = (ctx, frame, series) => {
        const width = ctx.canvas.width;
        const height = ctx.canvas.height;

        const step = frame > 100 ? 100 : frame;


        const min = Math.min(...series.data.filter(obj => obj > -100000));
        const max = Math.max(...series.data);
        const center = (max + min) / 2;
        const span = (max - min);
        const actualLength = series.data.filter(obj => obj > -100000).length;
        const horizontalOffset = (width - verticalAxisOffset) / actualLength;

        ctx.strokeStyle = 'rgb(255,0,0)'
        ctx.lineWidth = 2.0;
        ctx.beginPath();
        ctx.moveTo(verticalAxisOffset, (height - horizontalAxisOffset) / 2 - (series.data[0] - center) / span * (height - horizontalAxisOffset));
        for (let i = 0; i < 365; i++) {
            if (series.data[i] === -100000) {
                continue;
            }
            ctx.lineTo(verticalAxisOffset + horizontalOffset * i, (height - horizontalAxisOffset) / 2 - (series.data[i] - center) / span * (height - horizontalAxisOffset));
        }

        ctx.stroke();

    }

    const drawAxes = (ctx, series) => {
        const width = ctx.canvas.width;
        const height = ctx.canvas.height;
        const actualLength = series.data.filter(obj => obj > -100000).length;
        const min = Math.min(...series.data.filter(obj => obj > -100000));
        const max = Math.max(...series.data);

        const span = (max - min);

        const verticalAxisPoints = Math.floor((height - horizontalAxisOffset) / 40);


        ctx.font = '12px Arial'
        ctx.fillStyle = textColor;


        const verticalLabels = [];
        for (let i = 0; i < verticalAxisPoints; i++) {
            verticalLabels.push(min + span / verticalAxisPoints * i);
        }

        for (let i = 0; i < verticalLabels.length; i++) {
            ctx.fillText(verticalLabels[i].toFixed(0), 1, 3 + (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
        }

        ctx.strokeStyle = lineColor;
        ctx.lineWidth = 0.2;
        for (let i = 0; i < 365; i++) {
            ctx.beginPath()
            ctx.moveTo(verticalAxisOffset, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.lineTo(width, (height - horizontalAxisOffset) - (verticalLabels[i] - min) / span * (height - horizontalAxisOffset));
            ctx.stroke();
        }


        console.log(verticalLabels)
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
        <div style={{padding: 20}}>
            <Canvas drawFunction={(ctx, frame) => {
                ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height)
                frame = 100;


                console.log(seriesData)
                drawAxes(ctx, seriesData);
                drawContent(ctx, frame, seriesData, 30);


            }}/>
        </div>
    );
};

export default NewPlot;
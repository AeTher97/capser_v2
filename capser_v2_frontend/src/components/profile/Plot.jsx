import React, {useMemo} from 'react';
import {Chart} from 'react-charts'
import {Typography} from "@material-ui/core";

const Plot = ({width, timeSeries, title}) => {


        const plotData = useMemo(() => {
            if (timeSeries) {
                const cursor = timeSeries.lastElement;
                const startDate = new Date(timeSeries.lastLogged);
                const unwrapped = [];
                let index = 0;
                for (let i = cursor + 1; i < 365; i++) {
                    let date = new Date();
                    if (timeSeries.data[i] !== -100000) {
                        unwrapped.push([date.setDate(startDate.getDate() - 365 + index), timeSeries.data[i] === -100000 ? null : timeSeries.data[i]]);
                    }
                    index++;
                }
                for (let i = 0; i <= cursor; i++) {
                    let date = new Date();
                    if (timeSeries.data[i] !== -100000) {
                        unwrapped.push([date.setDate(startDate.getDate() - 365 + index), timeSeries.data[i]]);
                    }
                    index++;
                }
                return unwrapped;
            } else {
                return [];
            }
        }, [timeSeries])

        const series = React.useMemo(
            () => ({
                showPoints: false
            }),
            []
        )
        const axes = React.useMemo(
            () => [
                {primary: true, type: 'time', position: 'bottom'},
                {type: 'linear', position: 'left'}
            ],
            []
        )

        const seriesStyle = React.useCallback(series => ({
            color: 'red'
        }))

        const tooltip = React.useMemo(
            () => ({
                render: ({datum, primaryAxis, getStyle}) => {
                    return <CustomTooltip {...{getStyle, primaryAxis, datum}} title={title}/>
                }
            }),
            []
        )

        return (
            <div style={{position: 'relative', height: '30vh', padding: 15}}>
                <Typography>{title}</Typography>

                {(!timeSeries || timeSeries.data.filter(entry => entry !== -100000).length === 1) &&
                <div style={{display: "flex", justifyContent: 'center', height: '100%', alignItems: 'center'}}>
                    <Typography variant={"h4"}>No data available yet</Typography>
                </div>}

                {timeSeries && timeSeries.data.filter(entry => entry !== -100000).length !== 1 &&
                <div style={{display: 'flex', flexDirection: 'column', height: '30vh', position: 'absolute'}}>
                    <div style={{flex: 2, width: width - 10, height: '100%'}}>
                        <Chart data={[{label: 'xd', data: plotData}]} series={series}
                               getSeriesStyle={seriesStyle} axes={axes} dark tooltip={tooltip}
                        />
                    </div>

                </div>}
            </div>
        );
    }
;

const CustomTooltip = (
    {
        title, getStyle, primaryAxis, datum
    }
) => {
    const data = React.useMemo(
        () =>
            datum
                ? [
                    {
                        data: datum.group.map(d => {
                            return ({
                                primary: d.series.label,
                                secondary: d.secondary,
                                color: getStyle(d).fill,
                                date: new Date(d.originalDatum[0])
                            })
                        })
                    }
                ]
                : [],
        [datum, getStyle]
    )

    return datum ? (
        <div
            style={{
                color: 'black',
                pointerEvents: 'none'
            }}
        >
            <Typography color={"inherit"}>{title}</Typography>
            <Typography color={"inherit"}>{data[0].data[0].secondary}</Typography>
            <Typography color={"inherit"}>{new Date(data[0].data[0].date).toDateString()}</Typography>
        </div>) : null;
}

export default Plot;

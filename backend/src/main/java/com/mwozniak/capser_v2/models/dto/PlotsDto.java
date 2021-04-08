package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.models.database.TimeSeries;
import lombok.Data;

@Data
public class PlotsDto {

    private TimeSeries pointSeries;
    private TimeSeries rebuttalsSeries;
}

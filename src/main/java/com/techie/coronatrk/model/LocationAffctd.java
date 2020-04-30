package com.techie.coronatrk.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LocationAffctd {

    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;
}

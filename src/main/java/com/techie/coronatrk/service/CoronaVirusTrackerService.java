package com.techie.coronatrk.service;

import com.techie.coronatrk.model.LocationAffctd;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusTrackerService {

    private static final String URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String STATE = "Province/State";
    private static final String COUNTRY = "Country/Region";
    List<LocationAffctd> dataList = new ArrayList<>();

    public List<LocationAffctd> getDataList() {
        return dataList;
    }

    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    public void fetchCoronaRecords() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        StringReader in = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            LocationAffctd locationAffctd = new LocationAffctd();
            locationAffctd.setState(record.get(STATE));
            locationAffctd.setCountry(record.get(COUNTRY));
            locationAffctd.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            if(latestCases > prevDayCases) {
                locationAffctd.setDiffFromPrevDay(latestCases-prevDayCases);
            } else {
                locationAffctd.setDiffFromPrevDay(prevDayCases-latestCases);
            }

            dataList.add(locationAffctd);
            System.out.println(locationAffctd);
        }


    }

}

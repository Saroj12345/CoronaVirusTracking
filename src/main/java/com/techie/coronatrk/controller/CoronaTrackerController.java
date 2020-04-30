package com.techie.coronatrk.controller;

import com.techie.coronatrk.service.CoronaVirusTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoronaTrackerController {


    @Autowired
    CoronaVirusTrackerService coronaVirusTrackerService;

    @GetMapping("/")
    public String fetchCoronaDetails(Model model) {
        model.addAttribute("locationAffectedList",coronaVirusTrackerService.getDataList());
        int totalReportedCasesAcrossGlobe = coronaVirusTrackerService.getDataList().stream().mapToInt(locationAffctd -> locationAffctd.getLatestTotalCases()).sum();
        int totalNewCasesAcrossGlobe = coronaVirusTrackerService.getDataList().stream().mapToInt(locationAffctd -> locationAffctd.getDiffFromPrevDay()).sum();
        model.addAttribute("totalReportedCasesAcrossGlobe",totalReportedCasesAcrossGlobe);
        model.addAttribute("totalNewCasesAcrossGlobe",totalNewCasesAcrossGlobe);
        return "coronatracker";
    }
}

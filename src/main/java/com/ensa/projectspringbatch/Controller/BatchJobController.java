package com.ensa.projectspringbatch.Controller;

import com.ensa.projectspringbatch.Service.BatchJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/mutuelle")
public class BatchJobController {

    @Autowired
    private BatchJobService batchJobService;

    // API REST pour démarrer le job batch
    @PostMapping("/run-batch-job")
    public String runBatchJob() {
        return batchJobService.runJob();
    }
}

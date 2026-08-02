package com.homebase.homebase.controller;

import com.homebase.homebase.job.AlertEvaluationJob;
import com.homebase.homebase.job.PropertyIngestionJob;
import com.homebase.homebase.job.StockIngestionJob;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/jobs")
public class DebugJobController {
    final private AlertEvaluationJob alertEvaluationJob;
    final private PropertyIngestionJob propertyIngestionJob;
    final private StockIngestionJob stockIngestionJob;

    public DebugJobController(AlertEvaluationJob alertEvaluationJob, PropertyIngestionJob propertyIngestionJob, StockIngestionJob stockIngestionJob) {
        this.alertEvaluationJob = alertEvaluationJob;
        this.propertyIngestionJob = propertyIngestionJob;
        this.stockIngestionJob = stockIngestionJob;
    }

    @PostMapping("/property-ingestion")
    public ResponseEntity<String> triggerPropertyIngestion(){
        propertyIngestionJob.fetchProperties();
        return ResponseEntity.ok("Property ingestion triggered - check Job Log for results!");
    }

    @PostMapping("/stock-ingestion")
    public ResponseEntity<String> triggerStockIngestion(){
        stockIngestionJob.fetchStockQuote();
        return ResponseEntity.ok("StockIngestionJob triggered (chains AlertEvaluationJob on success) — check JobLog for result, costs 5 API requests");
    }

    @PostMapping("/alert-evaluation")
    public ResponseEntity<String> triggerAlertEvaluation(){
        alertEvaluationJob.evaluatePriceAlerts();
        return ResponseEntity.ok("Alerts evaluated - check Job Log for results!");
    }
}

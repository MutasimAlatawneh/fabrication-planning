package com.fabricationplanner.backend.controller;

import com.fabricationplanner.backend.dto.response.BatchGenerationResponse;
import com.fabricationplanner.backend.dto.response.BatchResponse;
import com.fabricationplanner.backend.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping("/generate")
    public ResponseEntity<BatchGenerationResponse> generateBatch() {
        BatchGenerationResponse result = batchService.generateBatch();
        HttpStatus status = result.getBatchedCount() > 0 ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(result);
    }

    @GetMapping
    public ResponseEntity<Page<BatchResponse>> getAllBatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(batchService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getById(id));
    }
}
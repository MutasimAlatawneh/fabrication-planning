package com.fabricationplanner.backend.controller;

import com.fabricationplanner.backend.dto.request.SpoolMaterialRequest;
import com.fabricationplanner.backend.dto.request.SpoolRequest;
import com.fabricationplanner.backend.dto.response.SpoolMaterialResponse;
import com.fabricationplanner.backend.dto.response.SpoolResponse;
import com.fabricationplanner.backend.service.SpoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SpoolController {

    private final SpoolService spoolService;

    @GetMapping("/spools")
    public ResponseEntity<Page<SpoolResponse>> getAllSpools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(spoolService.getAll(page, size, search));
    }

    @GetMapping("/spools/{id}")
    public ResponseEntity<SpoolResponse> getSpoolById(@PathVariable Long id) {
        return ResponseEntity.ok(spoolService.getById(id));
    }

    @GetMapping("/isos/{isoId}/spools")
    public ResponseEntity<List<SpoolResponse>> getSpoolsByIso(@PathVariable Long isoId) {
        return ResponseEntity.ok(spoolService.getByIsoId(isoId));
    }

    @PostMapping("/isos/{isoId}/spools")
    public ResponseEntity<SpoolResponse> createSpool(@PathVariable Long isoId,
                                                      @Valid @RequestBody SpoolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spoolService.create(isoId, request));
    }

    @PutMapping("/spools/{id}")
    public ResponseEntity<SpoolResponse> updateSpool(@PathVariable Long id,
                                                      @Valid @RequestBody SpoolRequest request) {
        return ResponseEntity.ok(spoolService.update(id, request));
    }

    @DeleteMapping("/spools/{id}")
    public ResponseEntity<Void> deleteSpool(@PathVariable Long id) {
        spoolService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/spools/{spoolId}/materials")
    public ResponseEntity<SpoolMaterialResponse> addMaterial(@PathVariable Long spoolId,
                                                              @Valid @RequestBody SpoolMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spoolService.addMaterial(spoolId, request));
    }

    @DeleteMapping("/spools/materials/{smId}")
    public ResponseEntity<Void> removeMaterial(@PathVariable Long smId) {
        spoolService.removeMaterial(smId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/spools/pending")
    public ResponseEntity<List<SpoolResponse>> getPendingSpools() {
        return ResponseEntity.ok(spoolService.getPendingSpools());
    }
}

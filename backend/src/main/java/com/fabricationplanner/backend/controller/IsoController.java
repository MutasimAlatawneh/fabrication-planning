package com.fabricationplanner.backend.controller;

import com.fabricationplanner.backend.dto.request.IsoRequest;
import com.fabricationplanner.backend.dto.response.IsoResponse;
import com.fabricationplanner.backend.service.IsoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/isos")
@RequiredArgsConstructor
public class IsoController {

    private final IsoService isoService;

    @GetMapping
    public ResponseEntity<Page<IsoResponse>> getAllIsos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(isoService.getAll(page, size, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IsoResponse> getIsoById(@PathVariable Long id) {
        return ResponseEntity.ok(isoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<IsoResponse> createIso(@Valid @RequestBody IsoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(isoService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IsoResponse> updateIso(@PathVariable Long id,
                                                  @Valid @RequestBody IsoRequest request) {
        return ResponseEntity.ok(isoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIso(@PathVariable Long id) {
        isoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

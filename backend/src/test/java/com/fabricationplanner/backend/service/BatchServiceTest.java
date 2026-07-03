package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.response.BatchGenerationResponse;
import com.fabricationplanner.backend.model.*;
import com.fabricationplanner.backend.repository.BatchRepository;
import com.fabricationplanner.backend.repository.MaterialStockRepository;
import com.fabricationplanner.backend.repository.SpoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceTest {

    @Mock
    private SpoolRepository spoolRepository;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private MaterialStockRepository materialStockRepository;

    @InjectMocks
    private BatchService batchService;

    @Mock
    private SpoolService spoolService;

    private MaterialStock steelPipe;
    private Spool spool;
    private SpoolMaterial spoolRequirement;

    @BeforeEach
    void setUp() {
        // Set up dummy data before each test runs
        steelPipe = MaterialStock.builder()
                .id(1L)
                .code("PIPE-01")
                .description("Steel Pipe")
                .quantity(new BigDecimal("50.00"))
                .build();

        spool = new Spool();
        spool.setId(1L);
        spool.setSpoolNo("SPL-TEST-01");
        spool.setStatus("PENDING");

        spoolRequirement = new SpoolMaterial();
        spoolRequirement.setMaterialStock(steelPipe);
        spoolRequirement.setSpool(spool);

        List<SpoolMaterial> requirements = new ArrayList<>();
        requirements.add(spoolRequirement);
        spool.setRequiredMaterials(requirements);
    }

    @Test
    void testGenerateBatch_HappyPath_SufficientStock() {
        // Arrange: Spool requires 10 pipes. We have 50 in stock.
        spoolRequirement.setQtyRequired(new BigDecimal("10.00"));
        when(spoolRepository.findByStatus("PENDING")).thenReturn(List.of(spool));
        when(batchRepository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Run the algorithm
        BatchGenerationResponse response = batchService.generateBatch();

        // Assert: Verify the logic worked exactly as intended
        assertNotNull(response, "A response should have been generated");
        assertEquals(1, response.getBatchedCount());
        assertNotNull(response.getBatchNo(), "Batch number should be generated");
        assertEquals("BATCHED", spool.getStatus(), "Spool status should be updated to BATCHED");

        // Verify math: 50 - 10 = 40 remaining
        assertEquals(new BigDecimal("40.00"), steelPipe.getQuantity(), "Stock should be deducted");

        // Verify database saves were called
        verify(materialStockRepository, times(1)).save(steelPipe);
        verify(spoolRepository, times(1)).save(spool);
    }

    @Test
    void testGenerateBatch_FailurePath_InsufficientStock() {
        // Arrange: Spool requires 100 pipes. We only have 50 in stock.
        spoolRequirement.setQtyRequired(new BigDecimal("100.00"));
        when(spoolRepository.findByStatus("PENDING")).thenReturn(List.of(spool));

        // Act: Run the algorithm
        BatchGenerationResponse response = batchService.generateBatch();

        // Assert: Verify the logic prevented the batching
        assertNotNull(response, "A response should be generated");
        assertEquals(0, response.getBatchedCount());
        assertNull(response.getBatchNo(), "No batch should be created");
        assertEquals("PENDING_MATERIAL", spool.getStatus(), "Spool status should change to PENDING_MATERIAL");

        // Verify math: Stock should NOT be deducted
        assertEquals(new BigDecimal("50.00"), steelPipe.getQuantity(), "Stock must remain unchanged");

        // Verify the batch repository was NEVER called
        verify(batchRepository, never()).save(any());
    }
}
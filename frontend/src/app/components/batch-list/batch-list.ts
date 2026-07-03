import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FabricationService } from '../../services/fabrication';
import { Batch } from '../../models/fabrication.model';

@Component({
  selector: 'app-batch-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './batch-list.html',
  styleUrl: './batch-list.css'
})
export class BatchListComponent implements OnInit {
  batches: Batch[] = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  expandedBatchId: number | null = null;

  constructor(private fabricationService: FabricationService) {}

  ngOnInit(): void {
    this.loadBatches(this.currentPage);
  }

  loadBatches(page: number): void {
    this.fabricationService.getBatches(page, 10).subscribe({
      next: (data) => {
        this.batches = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => console.error('Failed to load batches:', err)
    });
  }

  toggleBatch(batchId: number): void {
    if (this.expandedBatchId === batchId) {
      this.expandedBatchId = null;
    } else {
      // Load full batch detail with spools
      this.fabricationService.getBatch(batchId).subscribe({
        next: (batch) => {
          const idx = this.batches.findIndex(b => b.id === batchId);
          if (idx >= 0) this.batches[idx] = batch;
          this.expandedBatchId = batchId;
        }
      });
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.loadBatches(this.currentPage + 1);
  }

  prevPage(): void {
    if (this.currentPage > 0) this.loadBatches(this.currentPage - 1);
  }
}

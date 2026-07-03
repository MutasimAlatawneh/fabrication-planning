import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FabricationService } from '../../services/fabrication';
import { Spool, BatchGenerationResult } from '../../models/fabrication.model';

@Component({
  selector: 'app-spool-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './spool-list.html',
  styleUrl: './spool-list.css'
})
export class SpoolListComponent implements OnInit {
  spools: Spool[] = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  searchTerm = '';

  isProcessing = false;
  batchResult: BatchGenerationResult | null = null;
  batchError = '';

  constructor(private fabricationService: FabricationService) {}

  ngOnInit(): void {
    this.loadSpools(this.currentPage);
  }

  loadSpools(page: number): void {
    const search = this.searchTerm.trim() || undefined;
    this.fabricationService.getSpools(page, 10, search).subscribe({
      next: (data) => {
        this.spools = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => console.error('Failed to load spools:', err)
    });
  }

  onSearch(): void {
    this.loadSpools(0);
  }

  triggerBatchGeneration(): void {
    this.isProcessing = true;
    this.batchResult = null;
    this.batchError = '';

    this.fabricationService.generateBatch().subscribe({
      next: (result) => {
        this.batchResult = result;
        this.isProcessing = false;
        this.loadSpools(this.currentPage);
      },
      error: (err) => {
        this.batchError = err.error?.message || 'Failed to generate batch';
        this.isProcessing = false;
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'BATCHED': return 'bg-success';
      case 'PENDING_MATERIAL': return 'bg-warning text-dark';
      case 'PENDING': return 'bg-info';
      default: return 'bg-secondary';
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.loadSpools(this.currentPage + 1);
  }

  prevPage(): void {
    if (this.currentPage > 0) this.loadSpools(this.currentPage - 1);
  }
}
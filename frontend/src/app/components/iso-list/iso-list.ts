import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FabricationService } from '../../services/fabrication';
import { Iso } from '../../models/fabrication.model';

@Component({
  selector: 'app-iso-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './iso-list.html',
  styleUrl: './iso-list.css'
})
export class IsoListComponent implements OnInit {
  isos: Iso[] = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  searchTerm = '';

  showForm = false;
  editingId: number | null = null;
  formData = { isoNo: '', revision: '', status: 'RELEASED' };
  errorMessage = '';

  constructor(private fabricationService: FabricationService, private router: Router) {}

  ngOnInit(): void {
    this.loadIsos(this.currentPage);
  }

  loadIsos(page: number): void {
    const search = this.searchTerm.trim() || undefined;
    this.fabricationService.getIsos(page, 10, search).subscribe({
      next: (data) => {
        this.isos = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => console.error('Failed to load ISOs:', err)
    });
  }

  onSearch(): void {
    this.loadIsos(0);
  }

  openAddForm(): void {
    this.editingId = null;
    this.formData = { isoNo: '', revision: '', status: 'RELEASED' };
    this.errorMessage = '';
    this.showForm = true;
  }

  openEditForm(iso: Iso): void {
    this.editingId = iso.id;
    this.formData = { isoNo: iso.isoNo, revision: iso.revision, status: iso.status };
    this.errorMessage = '';
    this.showForm = true;
  }

  cancelForm(): void {
    this.showForm = false;
    this.errorMessage = '';
  }

  saveIso(): void {
    this.errorMessage = '';
    if (this.editingId) {
      this.fabricationService.updateIso(this.editingId, this.formData).subscribe({
        next: () => { this.showForm = false; this.loadIsos(this.currentPage); },
        error: (err) => this.errorMessage = err.error?.message || 'Failed to update ISO'
      });
    } else {
      this.fabricationService.createIso(this.formData).subscribe({
        next: () => { this.showForm = false; this.loadIsos(this.currentPage); },
        error: (err) => this.errorMessage = err.error?.message || 'Failed to create ISO'
      });
    }
  }

  // Delete confirmation modal state
  showDeleteModal = false;
  deleteTargetId: number | null = null;
  deleteTargetNo = '';

  confirmDelete(iso: Iso): void {
    this.deleteTargetId = iso.id;
    this.deleteTargetNo = iso.isoNo;
    this.showDeleteModal = true;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.deleteTargetId = null;
    this.deleteTargetNo = '';
  }

  proceedDelete(): void {
    if (this.deleteTargetId === null) return;
    this.fabricationService.deleteIso(this.deleteTargetId).subscribe({
      next: () => {
        this.showDeleteModal = false;
        this.deleteTargetId = null;
        this.deleteTargetNo = '';
        this.loadIsos(this.currentPage);
      },
      error: (err) => {
        this.showDeleteModal = false;
        this.errorMessage = err.error?.message || 'Failed to delete ISO';
      }
    });
  }

  viewDetail(id: number): void {
    this.router.navigate(['/isos', id]);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.loadIsos(this.currentPage + 1);
  }

  prevPage(): void {
    if (this.currentPage > 0) this.loadIsos(this.currentPage - 1);
  }
}

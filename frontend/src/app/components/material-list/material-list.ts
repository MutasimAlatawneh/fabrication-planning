import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FabricationService } from '../../services/fabrication';
import { MaterialStock } from '../../models/fabrication.model';

@Component({
  selector: 'app-material-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './material-list.html',
  styleUrl: './material-list.css'
})
export class MaterialListComponent implements OnInit {
  materials: MaterialStock[] = [];
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  searchTerm = '';

  // Form state
  showForm = false;
  editingId: number | null = null;
  formData = { code: '', description: '', quantity: 0 };
  errorMessage = '';

  // Delete confirmation modal state
  showDeleteModal = false;
  deleteTargetId: number | null = null;
  deleteTargetCode = '';

  constructor(private fabricationService: FabricationService) {}

  ngOnInit(): void {
    this.loadMaterials(this.currentPage);
  }

  loadMaterials(page: number): void {
    const search = this.searchTerm.trim() || undefined;
    this.fabricationService.getMaterials(page, 10, search).subscribe({
      next: (data) => {
        this.materials = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => console.error('Failed to load materials:', err)
    });
  }

  onSearch(): void {
    this.loadMaterials(0);
  }

  openAddForm(): void {
    this.editingId = null;
    this.formData = { code: '', description: '', quantity: 0 };
    this.errorMessage = '';
    this.showForm = true;
  }

  openEditForm(m: MaterialStock): void {
    this.editingId = m.id;
    this.formData = { code: m.code, description: m.description, quantity: m.quantity };
    this.errorMessage = '';
    this.showForm = true;
  }

  cancelForm(): void {
    this.showForm = false;
    this.errorMessage = '';
  }

  saveMaterial(): void {
    this.errorMessage = '';
    if (this.editingId) {
      this.fabricationService.updateMaterial(this.editingId, this.formData).subscribe({
        next: () => { this.showForm = false; this.loadMaterials(this.currentPage); },
        error: (err) => this.errorMessage = err.error?.message || 'Failed to update material'
      });
    } else {
      this.fabricationService.createMaterial(this.formData).subscribe({
        next: () => { this.showForm = false; this.loadMaterials(this.currentPage); },
        error: (err) => this.errorMessage = err.error?.message || 'Failed to create material'
      });
    }
  }

  confirmDelete(material: MaterialStock): void {
    this.deleteTargetId = material.id;
    this.deleteTargetCode = material.code;
    this.showDeleteModal = true;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.deleteTargetId = null;
    this.deleteTargetCode = '';
  }

  proceedDelete(): void {
    if (this.deleteTargetId === null) return;
    this.fabricationService.deleteMaterial(this.deleteTargetId).subscribe({
      next: () => {
        this.showDeleteModal = false;
        this.deleteTargetId = null;
        this.deleteTargetCode = '';
        this.loadMaterials(this.currentPage);
      },
      error: (err) => {
        this.showDeleteModal = false;
        this.errorMessage = err.error?.message || 'Cannot delete: material is referenced by spools';
      }
    });
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.loadMaterials(this.currentPage + 1);
  }

  prevPage(): void {
    if (this.currentPage > 0) this.loadMaterials(this.currentPage - 1);
  }
}
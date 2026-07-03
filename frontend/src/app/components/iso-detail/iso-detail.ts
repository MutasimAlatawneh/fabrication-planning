import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FabricationService } from '../../services/fabrication';
import { Iso, Spool, MaterialStock } from '../../models/fabrication.model';

@Component({
  selector: 'app-iso-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './iso-detail.html',
  styleUrl: './iso-detail.css'
})
export class IsoDetailComponent implements OnInit {
  iso: Iso | null = null;
  allMaterials: MaterialStock[] = [];

  // Add spool form
  showSpoolForm = false;
  spoolFormData = { spoolNo: '' };
  spoolError = '';

  // Add material to spool
  expandedSpoolId: number | null = null;
  materialFormData = { materialId: 0, qtyRequired: 0 };
  materialError = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fabricationService: FabricationService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadIso(id);
      this.loadAllMaterials();
    }
  }

  loadIso(id: number): void {
    this.fabricationService.getIso(id).subscribe({
      next: (data) => this.iso = data,
      error: () => this.router.navigate(['/isos'])
    });
  }

  loadAllMaterials(): void {
    this.fabricationService.getMaterials(0, 100).subscribe({
      next: (data) => this.allMaterials = data.content,
      error: (err) => console.error('Failed to load materials:', err)
    });
  }

  // ─── Spool CRUD ────────────────────────────────────
  openSpoolForm(): void {
    this.spoolFormData = { spoolNo: '' };
    this.spoolError = '';
    this.showSpoolForm = true;
  }

  cancelSpoolForm(): void {
    this.showSpoolForm = false;
    this.spoolError = '';
  }

  addSpool(): void {
    if (!this.iso) return;
    this.spoolError = '';
    this.fabricationService.createSpool(this.iso.id, this.spoolFormData).subscribe({
      next: () => { this.showSpoolForm = false; this.loadIso(this.iso!.id); },
      error: (err) => this.spoolError = err.error?.message || 'Failed to create spool'
    });
  }

  // Delete confirmation modal state
  showDeleteModal = false;
  deleteTargetId: number | null = null;
  deleteTargetNo = '';
  deleteErrorMessage = '';

  confirmDelete(spool: Spool): void {
    this.deleteTargetId = spool.id;
    this.deleteTargetNo = spool.spoolNo;
    this.showDeleteModal = true;
    this.deleteErrorMessage = '';
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.deleteTargetId = null;
    this.deleteTargetNo = '';
  }

  proceedDelete(): void {
    if (this.deleteTargetId === null || !this.iso) return;
    this.fabricationService.deleteSpool(this.deleteTargetId).subscribe({
      next: () => {
        this.showDeleteModal = false;
        this.deleteTargetId = null;
        this.deleteTargetNo = '';
        this.loadIso(this.iso!.id);
      },
      error: (err) => {
        this.showDeleteModal = false;
        this.deleteErrorMessage = err.error?.message || 'Failed to delete spool';
      }
    });
  }

  // ─── Spool Material Management ─────────────────────
  toggleMaterials(spoolId: number): void {
    this.expandedSpoolId = this.expandedSpoolId === spoolId ? null : spoolId;
    this.materialError = '';
    this.materialFormData = { materialId: 0, qtyRequired: 0 };
  }

  addMaterialToSpool(spoolId: number): void {
    this.materialError = '';
    if (!this.materialFormData.materialId || this.materialFormData.qtyRequired <= 0) {
      this.materialError = 'Select a material and enter a quantity > 0';
      return;
    }
    this.fabricationService.addSpoolMaterial(spoolId, this.materialFormData).subscribe({
      next: () => {
        this.materialFormData = { materialId: 0, qtyRequired: 0 };
        this.loadIso(this.iso!.id);
        this.loadAllMaterials();
      },
      error: (err) => this.materialError = err.error?.message || 'Failed to add material'
    });
  }

  removeMaterial(smId: number): void {
    this.fabricationService.removeSpoolMaterial(smId).subscribe({
      next: () => { this.loadIso(this.iso!.id); this.loadAllMaterials(); },
      error: (err) => alert(err.error?.message || 'Failed to remove material')
    });
  }

  goBack(): void {
    this.router.navigate(['/isos']);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'BATCHED': return 'bg-success';
      case 'PENDING_MATERIAL': return 'bg-warning text-dark';
      case 'PENDING': return 'bg-info';
      default: return 'bg-secondary';
    }
  }
}

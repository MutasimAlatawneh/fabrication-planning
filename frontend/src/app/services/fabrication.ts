import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  MaterialStock, Iso, Spool, SpoolMaterial,
  Batch, BatchGenerationResult, DashboardMetrics, PaginatedResponse
} from '../models/fabrication.model';

@Injectable({
  providedIn: 'root'
})
export class FabricationService {
  private baseUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  // ─── Materials ───────────────────────────────────────
  getMaterials(page = 0, size = 10, search?: string): Observable<PaginatedResponse<MaterialStock>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) params = params.set('search', search);
    return this.http.get<PaginatedResponse<MaterialStock>>(`${this.baseUrl}/materials`, { params });
  }

  getMaterial(id: number): Observable<MaterialStock> {
    return this.http.get<MaterialStock>(`${this.baseUrl}/materials/${id}`);
  }

  createMaterial(material: { code: string; description: string; quantity: number }): Observable<MaterialStock> {
    return this.http.post<MaterialStock>(`${this.baseUrl}/materials`, material);
  }

  updateMaterial(id: number, material: { code: string; description: string; quantity: number }): Observable<MaterialStock> {
    return this.http.put<MaterialStock>(`${this.baseUrl}/materials/${id}`, material);
  }

  deleteMaterial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/materials/${id}`);
  }

  // ─── ISOs ────────────────────────────────────────────
  getIsos(page = 0, size = 10, search?: string): Observable<PaginatedResponse<Iso>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) params = params.set('search', search);
    return this.http.get<PaginatedResponse<Iso>>(`${this.baseUrl}/isos`, { params });
  }

  getIso(id: number): Observable<Iso> {
    return this.http.get<Iso>(`${this.baseUrl}/isos/${id}`);
  }

  createIso(iso: { isoNo: string; revision: string; status?: string }): Observable<Iso> {
    return this.http.post<Iso>(`${this.baseUrl}/isos`, iso);
  }

  updateIso(id: number, iso: { isoNo: string; revision: string; status?: string }): Observable<Iso> {
    return this.http.put<Iso>(`${this.baseUrl}/isos/${id}`, iso);
  }

  deleteIso(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/isos/${id}`);
  }

  // ─── Spools ──────────────────────────────────────────
  getSpools(page = 0, size = 10, search?: string): Observable<PaginatedResponse<Spool>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) params = params.set('search', search);
    return this.http.get<PaginatedResponse<Spool>>(`${this.baseUrl}/spools`, { params });
  }

  getSpool(id: number): Observable<Spool> {
    return this.http.get<Spool>(`${this.baseUrl}/spools/${id}`);
  }

  getSpoolsByIso(isoId: number): Observable<Spool[]> {
    return this.http.get<Spool[]>(`${this.baseUrl}/isos/${isoId}/spools`);
  }

  createSpool(isoId: number, spool: { spoolNo: string }): Observable<Spool> {
    return this.http.post<Spool>(`${this.baseUrl}/isos/${isoId}/spools`, spool);
  }

  updateSpool(id: number, spool: { spoolNo: string; status?: string }): Observable<Spool> {
    return this.http.put<Spool>(`${this.baseUrl}/spools/${id}`, spool);
  }

  deleteSpool(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/spools/${id}`);
  }

  getPendingSpools(): Observable<Spool[]> {
    return this.http.get<Spool[]>(`${this.baseUrl}/spools/pending`);
  }

  // ─── Spool Materials ─────────────────────────────────
  addSpoolMaterial(spoolId: number, req: { materialId: number; qtyRequired: number }): Observable<SpoolMaterial> {
    return this.http.post<SpoolMaterial>(`${this.baseUrl}/spools/${spoolId}/materials`, req);
  }

  removeSpoolMaterial(smId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/spools/materials/${smId}`);
  }

  // ─── Batches ─────────────────────────────────────────
  generateBatch(): Observable<BatchGenerationResult> {
    return this.http.post<BatchGenerationResult>(`${this.baseUrl}/batches/generate`, {});
  }

  getBatches(page = 0, size = 10): Observable<PaginatedResponse<Batch>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PaginatedResponse<Batch>>(`${this.baseUrl}/batches`, { params });
  }

  getBatch(id: number): Observable<Batch> {
    return this.http.get<Batch>(`${this.baseUrl}/batches/${id}`);
  }

  // ─── Dashboard ───────────────────────────────────────
  getDashboardMetrics(): Observable<DashboardMetrics> {
    return this.http.get<DashboardMetrics>(`${this.baseUrl}/dashboard/metrics`);
  }
}
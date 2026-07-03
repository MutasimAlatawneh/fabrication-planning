export interface MaterialStock {
  id: number;
  code: string;
  description: string;
  quantity: number;
}

export interface Iso {
  id: number;
  isoNo: string;
  revision: string;
  status: string;
  spools?: Spool[];
}

export interface Spool {
  id: number;
  spoolNo: string;
  status: string;
  isoId: number;
  isoNo: string;
  requiredMaterials?: SpoolMaterial[];
}

export interface SpoolMaterial {
  id: number;
  materialId: number;
  materialCode: string;
  materialDescription: string;
  qtyRequired: number;
  availableQuantity: number;
}

export interface Batch {
  id: number;
  batchNo: string;
  createdDate: string;
  spools: Spool[];
}

export interface BatchGenerationResult {
  message: string;
  batchNo: string | null;
  batchedCount: number;
  pendingCount: number;
  batchedSpools: Spool[];
  pendingSpools: Spool[];
}

export interface DashboardMetrics {
  totalIsos: number;
  totalSpools: number;
  availableMaterials: number;
  generatedBatches: number;
  pendingSpools: number;
  batchedSpools: number;
}

// Emulates Spring Data's default Page response layout
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
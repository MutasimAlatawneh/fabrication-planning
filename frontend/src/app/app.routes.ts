import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { MaterialListComponent } from './components/material-list/material-list';
import { SpoolListComponent } from './components/spool-list/spool-list';
import { IsoListComponent } from './components/iso-list/iso-list';
import { IsoDetailComponent } from './components/iso-detail/iso-detail';
import { BatchListComponent } from './components/batch-list/batch-list';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'materials', component: MaterialListComponent, canActivate: [authGuard] },
  { path: 'isos', component: IsoListComponent, canActivate: [authGuard] },
  { path: 'isos/:id', component: IsoDetailComponent, canActivate: [authGuard] },
  { path: 'spools', component: SpoolListComponent, canActivate: [authGuard] },
  { path: 'batches', component: BatchListComponent, canActivate: [authGuard] }
];
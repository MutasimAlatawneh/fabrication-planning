import { Component, OnInit, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';
import { FabricationService } from '../../services/fabrication';
import { DashboardMetrics, MaterialStock } from '../../models/fabrication.model';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  metrics: DashboardMetrics = {
    totalIsos: 0,
    totalSpools: 0,
    availableMaterials: 0,
    generatedBatches: 0,
    pendingSpools: 0,
    batchedSpools: 0
  };
  materials: MaterialStock[] = [];
  private chartsRendered = false;

  constructor(private fabricationService: FabricationService) {
    afterNextRender(() => {
      if (this.metrics.totalSpools > 0) {
        this.renderCharts();
      }
    });
  }

  ngOnInit(): void {
    this.loadMetrics();
    this.loadMaterials();
  }

  loadMetrics(): void {
    this.fabricationService.getDashboardMetrics().subscribe({
      next: (data) => {
        this.metrics = data;
        if (!this.chartsRendered) {
          setTimeout(() => this.renderCharts(), 100);
        }
      },
      error: (err) => console.error('Failed to load dashboard metrics:', err)
    });
  }

  loadMaterials(): void {
    this.fabricationService.getMaterials(0, 10).subscribe({
      next: (data) => {
        this.materials = data.content;
        if (!this.chartsRendered) {
          setTimeout(() => this.renderCharts(), 100);
        }
      },
      error: (err) => console.error('Failed to load materials:', err)
    });
  }

  renderCharts(): void {
    if (this.chartsRendered) return;

    const doughnutCanvas = document.getElementById('fabricationChart') as HTMLCanvasElement;
    const barCanvas = document.getElementById('materialChart') as HTMLCanvasElement;
    if (!doughnutCanvas || !barCanvas) return;
    if (this.metrics.totalSpools === 0 && this.materials.length === 0) return;

    this.chartsRendered = true;

    // Doughnut Chart - Spool Batching Status
    new Chart(doughnutCanvas, {
      type: 'doughnut',
      data: {
        labels: ['Batched Spools', 'Pending Spools'],
        datasets: [{
          label: 'Spool Status',
          data: [this.metrics.batchedSpools, this.metrics.pendingSpools],
          backgroundColor: ['rgba(40, 167, 69, 0.85)', 'rgba(255, 193, 7, 0.85)'],
          borderColor: ['#28a745', '#ffc107'],
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });

    // Bar Chart - Material Inventory Levels
    const labels = this.materials.map(m => m.code);
    const quantities = this.materials.map(m => m.quantity);
    const barColors = this.materials.map((_, i) => {
      const colors = [
        'rgba(54, 162, 235, 0.8)',
        'rgba(75, 192, 192, 0.8)',
        'rgba(153, 102, 255, 0.8)',
        'rgba(255, 159, 64, 0.8)',
        'rgba(255, 99, 132, 0.8)',
        'rgba(255, 205, 86, 0.8)',
        'rgba(201, 203, 207, 0.8)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(75, 192, 192, 0.6)',
        'rgba(153, 102, 255, 0.6)'
      ];
      return colors[i % colors.length];
    });

    new Chart(barCanvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Available Quantity',
          data: quantities,
          backgroundColor: barColors,
          borderColor: barColors.map(c => c.replace('0.8', '1').replace('0.6', '1')),
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            title: { display: true, text: 'Quantity' }
          },
          x: {
            title: { display: true, text: 'Material Code' }
          }
        }
      }
    });
  }
}
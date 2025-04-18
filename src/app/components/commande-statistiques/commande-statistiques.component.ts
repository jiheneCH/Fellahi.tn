import { Component, OnInit, ViewChild } from '@angular/core';
import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexLegend,
  ChartComponent
} from 'ng-apexcharts';
import { CommandesService } from 'src/app/commandes.service';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  responsive: ApexResponsive[];
  legend: ApexLegend;
};

@Component({
  selector: 'app-commande-statistiques',
  templateUrl: './commande-statistiques.component.html',
  styleUrls: ['./commande-statistiques.component.css']
})
export class CommandeStatistiquesComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent | undefined;
  public chartOptions: ChartOptions = {
    series: [0, 0, 0],
    chart: {
      type: 'donut'
    },
    labels: ['Pending', 'Cancelled', 'Confirmed'],
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: {
            width: 300
          },
          legend: {
            position: 'bottom'
          }
        }
      }
    ],
    legend: {
      position: 'right'
    }
  };

  statistics: any = {
    totalOrders: 0,
    pendingPercentage: 0,
    cancelledPercentage: 0,
    confirmedPercentage: 0
  };

  constructor(private commandesService: CommandesService) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.commandesService.getCommandeStats().subscribe((data: any) => {
      this.statistics = data;
      this.updateChartData();
    });
  }

  updateChartData(): void {
    this.chartOptions.series = [
      this.statistics.pendingPercentage,
      this.statistics.cancelledPercentage,
      this.statistics.confirmedPercentage
    ];
  }
}

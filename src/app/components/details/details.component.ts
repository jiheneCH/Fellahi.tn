import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommandesService } from 'src/app/commandes.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  commande: any;

  constructor(
    private route: ActivatedRoute,
    private commandesService: CommandesService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.commandesService.getCommandeById(id).subscribe(data => {
        this.commande = data;
      });
    }
  }
  getStatusIcon(status: string): string {
    switch(status.toLowerCase()) {
      case 'confirmed': return 'fas fa-check-circle';
      case 'cancelled': return 'fas fa-times-circle';
      case 'processing': return 'fas fa-cog';
      default: return 'fas fa-clock'; // pending
    }
  }
  goBack(): void {
    window.history.back();
  }

}

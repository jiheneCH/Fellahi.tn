import { Component, OnInit, AfterViewInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-showlocation',
  templateUrl: './showlocation.component.html',
  styleUrls: ['./showlocation.component.css']
})
export class ShowlocationComponent implements AfterViewInit {
 
  adresse: string = '';
  ville: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngAfterViewInit(): void {
    this.route.paramMap.subscribe(params => {
      this.adresse = params.get('adresse') || '';
      this.ville = params.get('ville') || '';

      const fullAddress = `${this.adresse}, ${this.ville}, Tunisia`;

      // Appel API de géocodage
      this.http.get<any[]>(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(fullAddress)}&format=json`)
        .subscribe(results => {
          if (results.length > 0) {
            const lat = parseFloat(results[0].lat);
            const lon = parseFloat(results[0].lon);

            const map = L.map('map').setView([lat, lon], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
              attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            L.marker([lat, lon]).addTo(map)
              .bindPopup(`📍 ${fullAddress}`)
              .openPopup();
          } else {
            alert("Adresse introuvable !");
          }
        });
    });
  }
  openInGoogleMaps(): void {
    const fullAddress = `${this.adresse}, ${this.ville}`;
    const url = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(fullAddress)}`;
    window.open(url, '_blank');
  }
  
}

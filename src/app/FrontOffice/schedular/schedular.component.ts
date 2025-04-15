import { Component, OnInit } from '@angular/core';
import { View } from '@syncfusion/ej2-angular-schedule';

@Component({
  selector: 'app-schedular',
  //template : '<ejs-schedule></ejs-schedule>',
  templateUrl: './schedular.component.html',
  styleUrls: ['./schedular.component.css']
})
export class SchedularComponent implements OnInit {
  public selectedDate: Date = new Date();
  public currentView: string = 'Month';

  public eventSettings: any = {
    dataSource: [
      {
        Id: 1,
        Subject: 'Conférence',
        StartTime: new Date(2025, 3, 15, 10, 0),
        EndTime: new Date(2025, 3, 15, 11, 30)
      },
      // d'autres événements ici
    ]
  };

  ngOnInit(): void {}
}

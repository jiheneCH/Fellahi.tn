import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { rmSync } from 'fs';
import { SupervisorAdminService } from 'src/app/service/adminService/supervisorAdmin.service';
import Swal from 'sweetalert2';


@Component({
    templateUrl: './prof-table.html',
})

export class ProfTableComponent {
    store: any;
    lineChart: any;
    areaChart: any;
    columnChart: any;
    simpleColumnStacked: any;
    barChart: any;
    mixedChart: any;
    radarChart: any;
    pieChart: any;
    donutChart: any;
    polarAreaChart: any;
    radialBarChart: any;
    bubbleChart: any;
    isLoading = true;
    constructor(private dataService: SupervisorAdminService,public storeData: Store<any>) {
         this.initStore();
        this.isLoading = false;}
    search1 = '';
    search2 = '';
    datatable1Cols = [
        { field: 'firstName', title: 'Name' },
        { field: 'email', title: 'Email' },
        { field: 'phone', title: 'Phone No.' },
        { field: 'dob', title: 'Date of Birth' },
        { field: 'availability', title: 'Status' },
    ];

   /* datatable2Cols = [
        { field: 'firstName', title: 'Name' },
        { field: 'age', title: 'Progress' },
        { field: 'company', title: 'Company' },
        { field: 'dob', title: 'Start Date' },
        { field: 'email', title: 'Email' },
        { field: 'phone', title: 'Phone No.' },
        { field: 'action', title: 'Action', sort: false },
    ];*/
    rows: any[]=[];

  /*  rows = [
       {
            id: 1,
            firstName: 'Caroline',
            lastName: 'Jensen',
            email: 'carolinejensen@zidant.com',
            dob: '2004-05-28',
            address: {
                street: '529 Scholes Street',
                city: 'Temperanceville',
                zipcode: 5235,
                geo: {
                    lat: 23.806115,
                    lng: 164.677197,
                },
            },
            phone: '+1 (821) 447-3782',
            isActive: true,
            age: 39,
            company: 'POLARAX',
        }];
    ];*/
    async initStore() {
        this.storeData
            .select((d) => d.index)
            .subscribe((d) => {
                const hasChangeTheme = this.store?.theme !== d?.theme;
                const hasChangeLayout = this.store?.layout !== d?.layout;
                const hasChangeMenu = this.store?.menu !== d?.menu;
                const hasChangeSidebar = this.store?.sidebar !== d?.sidebar;

                this.store = d;

                if (hasChangeTheme || hasChangeLayout || hasChangeMenu || hasChangeSidebar) {
                    if (this.isLoading || hasChangeTheme) {
                        this.initCharts(); //init charts
                    } else {
                        setTimeout(() => {
                            this.initCharts(); // refresh charts
                        }, 300);
                    }
                }
            });
            this.dataService.getProfessors()
        .subscribe(response => {
            this.rows = response;
            console.log(this.rows);
            });
    }

    
assignSupervisor(){

    Swal.fire('Success', 'Supervisors assigned to students.', 'success');


        this.dataService.assignProfessors()
        .subscribe(response => {
            this.rows = response;
            console.log(this.rows);
            this.initCharts();
            this.dataService.getProfessors()
            .subscribe(response => {
                this.rows = response;
                });
            });

}
    formatDate(date: any) {
        if (date) {
            const dt = new Date(date);
            const month = dt.getMonth() + 1 < 10 ? '0' + (dt.getMonth() + 1) : dt.getMonth() + 1;
            const day = dt.getDate() < 10 ? '0' + dt.getDate() : dt.getDate();
            return day + '/' + month + '/' + dt.getFullYear();
        }
        return '';
    }

    Color() {


    }
    randomColor() {
        const color = ['primary', 'secondary', 'success', 'danger', 'warning', 'info'];
        const random = Math.floor(Math.random() * color.length);
        return color[random];
    }

    randomStatus() {
        const status = ['PAID', 'APPROVED', 'FAILED', 'CANCEL', 'SUCCESS', 'PENDING', 'COMPLETE'];
        const random = Math.floor(Math.random() * status.length);
        return status[random];
    }
    totalValue:any;
    percentages:any;
    studentNumber:any[]=[];
    initCharts() {
        console.log("lol");

    this.dataService.getStatsSupervisor().subscribe((response: object) => {
        // Create a Map instance and iterate over the object, adding entries
        const hashMap = new Map<string, number>(); // Replace types based on your actual data
        for (const [key, value] of Object.entries(response)) {
          hashMap.set(key, value as number); // Cast value assuming number type
        }

        console.log(hashMap); // Map { key1 => value1, key2 => value2 }
        // Use the hashMap variable further in your code
        const keys = Array.from(hashMap.keys()).map(key => `${key} Students`); // Get keys as an array
        const values = Array.from(hashMap.values()); // Get values as an array
        if (values.length === 0) {
            console.error("No data found in the response.");
            return; // Exit the subscription callback if there's no data
          }

          // Calculate the total value (sum of all values)
          this.totalValue = values.reduce((acc, cur) => acc + cur, 0);

          // Calculate percentage for each value
          this.percentages= values.map(value => {
            // Handle potential division by zero
            if (this.totalValue === 0) {
              return 0; // Return 0% if total is zero to avoid errors
            }
            return Math.round((value / this.totalValue) * 100); // Round to 2 decimal places
          });
        console.log("Keys:", keys);
        console.log("Values:", values);
        const isDark = this.store.theme === 'dark' || this.store.isDarkMode ? true : false;
        const isRtl = this.store.rtlClass === 'rtl' ? true : false;
        this.pieChart = {
            series:/* [44, 55, 13, 43, 22]*/values,
            chart: {
                height: 300,
                type: 'pie',
                zoom: {
                    enabled: false,
                },
                toolbar: {
                    show: false,
                },
            },
            labels: /*['Team A', 'Team B', 'Team C', 'Team D', 'Team E']*/keys,
            colors: ['#4361ee', '#805dca', '#00ab55', '#e7515a', '#e2a03f'],
            responsive: [
                {
                    breakpoint: 480,
                    options: {
                        chart: {
                            width: 200,
                        },
                    },
                },
            ],
            stroke: {
                show: false,
            },
            legend: {
                position: 'bottom',
            },
        };

   });
}

}
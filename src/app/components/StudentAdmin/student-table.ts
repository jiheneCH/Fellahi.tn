import { Component } from '@angular/core';

import { Store } from '@ngrx/store';
import { StudentAdminService } from 'src/app/service/adminService/studentAdmin.service';
import Swal from 'sweetalert2';
@Component({
    templateUrl: './student-table.html',
})

export class StudentTableComponent {
    codeArr: any = [];
    toggleCode = (name: string) => {
        if (this.codeArr.includes(name)) {
            this.codeArr = this.codeArr.filter((d: string) => d != name);
        } else {
            this.codeArr.push(name);
        }
    };
   

    constructor(private dataService: StudentAdminService,public storeData: Store<any>) {   }
       
    search = '';
   /* datatable1Cols = [
        { field: 'firstName', title: 'Name' },
        { field: 'email', title: 'Email' },
        { field: 'phone', title: 'Phone No.' },
        { field: 'company', title: 'Company' },
        { field: 'supervisor', title: 'Supervisor' },
        { field: 'report', title: 'Report' },
        { field: 'internshipDiary', title: 'Diary' },
        { field: 'internshipCertificate', title: 'Certificate' },
        { field: 'action', title: 'Action', sort: false, headerClass: 'justify-center' },
    ];
*/
 cols  = [
        { field: 'firstName', title: 'Name' },
        { field: 'email', title: 'Email' },
        { field: 'phoneNumber', title: 'Phone No.' },
        { field: 'internshipCertificate', title: 'Certificate' },

        { field: 'validation', title: 'Validation' },
        { field: 'action', title: 'Action', sort: false },
    ];
    rows: any[]=[];
ngOnInit() {
    
      this.dataService.getStudents().subscribe(response => {
        this.rows = response.map((student: {
          studentFiles: {
            accpetanceLetter: any;
            coverLetter: any;
            internshipCertificate: any;
            resume: any;
            validation: any;
            id: any;
          };
          firstName: any;
          lastName: any;
          phoneNumber: any;
          email: any;
        }) => {
          let firstName = student.firstName;
          let lastName = student.lastName;
          let phoneNumber = student.phoneNumber;
          let email = student.email;
          
          // Vérifier si studentFiles est défini
          if (student.studentFiles) {
            let validation = student.studentFiles.validation ? student.studentFiles.validation : null;
            let idFile = student.studentFiles.id ? student.studentFiles.id: null;
            let internshipCertificate = student.studentFiles.internshipCertificate ? student.studentFiles.internshipCertificate : null;
            
            return {
              firstName: `${firstName} ${lastName}`,
              phoneNumber: phoneNumber,
              email: email,
              validation: validation,
              idFile: idFile,
              internshipCertificate: internshipCertificate,
              action: 'Action buttons or links'
            };
          } else {
            // Si studentFiles n'est pas défini, retourner un objet avec des valeurs par défaut
            return {
              firstName: `${firstName} ${lastName}`,
              phoneNumber: phoneNumber,
              email: email,
              validation: null,
              idFile: null,
              internshipCertificate: null,
              action: 'Action buttons or links'
            };
          }
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

    openPdf(file: string) {
        //const pdfUrl = 'file:///C:/Users/hayth/Downloads/Haythem%20BenKhaled%20Resume%20.pdf';
        console.log('PDF File Path:', file);
        this.dataService.getPdf(file).subscribe(
            (blob: Blob) => {
              const fileURL = URL.createObjectURL(blob);
              window.open(fileURL, '_blank');
            },
            (error) => {
              console.error('Error fetching PDF:', error);
              // Handle error, e.g., display an error message to the user
            }
          );    
    }
    downloadPdf(fileName: string) {
        this.dataService.downloadPdf(fileName).subscribe(
          (blob: Blob) => {
            const url = window.URL.createObjectURL(blob);
            window.open(url);
          },
          (error) => {
            console.error('Error downloading PDF:', error);
          }
        );
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

    jsonData = this.rows;



    exportTable(type: string) {
        let columns: any = this.cols.map((d: { field: any }) => {
            return d.field;
        });

        let records = this.rows;
        let filename = 'table';

        let newVariable: any;
        newVariable = window.navigator;

        if (type == 'csv') {
            let coldelimiter = ';';
            let linedelimiter = '\n';
            let result = columns
                .map((d: any) => {
                    return this.capitalize(d);
                })
                .join(coldelimiter);
            result += linedelimiter;
            records.map((item: { [x: string]: any }) => {
                columns.map((d: any, index: number) => {
                    if (index > 0) {
                        result += coldelimiter;
                    }
                    let val = item[d] ? item[d] : '';
                    result += val;
                });
                result += linedelimiter;
            });

            if (result == null) return;
            if (!result.match(/^data:text\/csv/i) && !newVariable.msSaveOrOpenBlob) {
                var data = 'data:application/csv;charset=utf-8,' + encodeURIComponent(result);
                var link = document.createElement('a');
                link.setAttribute('href', data);
                link.setAttribute('download', filename + '.csv');
                link.click();
            } else {
                var blob = new Blob([result]);
                if (newVariable.msSaveOrOpenBlob) {
                    newVariable.msSaveBlob(blob, filename + '.csv');
                }
            }
        } else if (type == 'print') {
            var rowhtml = '<p>' + filename + '</p>';
            rowhtml +=
                '<table style="width: 100%; " cellpadding="0" cellcpacing="0"><thead><tr style="color: #515365; background: #eff5ff; -webkit-print-color-adjust: exact; print-color-adjust: exact; "> ';
            columns.map((d: any) => {
                rowhtml += '<th>' + this.capitalize(d) + '</th>';
            });
            rowhtml += '</tr></thead>';
            rowhtml += '<tbody>';

            records.map((item: { [x: string]: any }) => {
                rowhtml += '<tr>';
                columns.map((d: any) => {
                    let val = item[d] ? item[d] : '';
                    rowhtml += '<td>' + val + '</td>';
                });
                rowhtml += '</tr>';
            });
            rowhtml +=
                '<style>body {font-family:Arial; color:#495057;}p{text-align:center;font-size:18px;font-weight:bold;margin:15px;}table{ border-collapse: collapse; border-spacing: 0; }th,td{font-size:12px;text-align:left;padding: 4px;}th{padding:8px 4px;}tr:nth-child(2n-1){background:#f7f7f7; }</style>';
            rowhtml += '</tbody></table>';
            var winPrint: any = window.open('', '', 'left=0,top=0,width=1000,height=600,toolbar=0,scrollbars=0,status=0');
            winPrint.document.write('<title>Print</title>' + rowhtml);
            winPrint.document.close();
            winPrint.focus();
            winPrint.print();
            // winPrint.close();
        } else if (type == 'txt') {
            let coldelimiter = ',';
            let linedelimiter = '\n';
            let result = columns
                .map((d: any) => {
                    return this.capitalize(d);
                })
                .join(coldelimiter);
            result += linedelimiter;
            records.map((item: { [x: string]: any }) => {
                columns.map((d: any, index: number) => {
                    if (index > 0) {
                        result += coldelimiter;
                    }
                    let val = item[d] ? item[d] : '';
                    result += val;
                });
                result += linedelimiter;
            });

            if (result == null) return;
            if (!result.match(/^data:text\/txt/i) && !newVariable.msSaveOrOpenBlob) {
                var data = 'data:application/txt;charset=utf-8,' + encodeURIComponent(result);
                var link = document.createElement('a');
                link.setAttribute('href', data);
                link.setAttribute('download', filename + '.txt');
                link.click();
            } else {
                var blob = new Blob([result]);
                if (newVariable.msSaveOrOpenBlob) {
                    newVariable.msSaveBlob(blob, filename + '.txt');
                }
            }
        }
    }

    excelColumns() {
        return {
            Id: 'id',
            FirstName: 'firstName',
            LastName: 'lastName',
            Company: 'company',
            Age: 'age',
            'Start Date': 'dob',
            Email: 'email',
            'Phone No.': 'phone',
        };
    }

    excelItems() {
        return this.rows;
    }

    capitalize(text: string) {
        return text
            .replace('_', ' ')
            .replace('-', ' ')
            .toLowerCase()
            .split(' ')
            .map((s: string) => s.charAt(0).toUpperCase() + s.substring(1))
            .join(' ');
    }

    file:any;
    validateData(validator:any,id:any){
    
        console.log(validator);
        console.log(id);
        this.dataService.getStudentFileById(id).subscribe(response=>{this.file=response;
            this.file.validation=validator;
            this.dataService.validate(this.file).subscribe(
                response=>{
                    
this.dataService.getStudents().subscribe(response => {
    this.rows = response.map((student: {
      studentFiles: {
        accpetanceLetter: any;
        coverLetter: any;
        internshipCertificate: any;
        resume: any;
        validation: any;
        id: any;
      };
      firstName: any;
      lastName: any;
      phoneNumber: any;
      email: any;
    }) => {
      let firstName = student.firstName;
      let lastName = student.lastName;
      let phoneNumber = student.phoneNumber;
      let email = student.email;
      
      // Vérifier si studentFiles est défini
      if (student.studentFiles) {
        let validation = student.studentFiles.validation ? student.studentFiles.validation : null;
        let idFile = student.studentFiles.id ? student.studentFiles.id: null;
        let internshipCertificate = student.studentFiles.internshipCertificate ? student.studentFiles.internshipCertificate : null;
        
        return {
          firstName: `${firstName} ${lastName}`,
          phoneNumber: phoneNumber,
          email: email,
          validation: validation,
          idFile: idFile,
          internshipCertificate: internshipCertificate,
          action: 'Action buttons or links'
        };
      } else {
        // Si studentFiles n'est pas défini, retourner un objet avec des valeurs par défaut
        return {
          firstName: `${firstName} ${lastName}`,
          phoneNumber: phoneNumber,
          email: email,
          validation: null,
          idFile: null,
          internshipCertificate: null,
          action: 'Action buttons or links'
        };
      }
    });
  });

                }







                
            );
            console.log(this.file);

    
        if(validator=="Validated")
 {      
     Swal.fire('Success', 'Data Validated', 'success');
    }
    else{
Swal.fire('Error', 'Data Refused.', 'error');
}
 


        });
}
}

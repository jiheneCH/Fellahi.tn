
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

//Routes
import { routes } from '../app.route';

import { AppComponent } from '../app.component';

// service
import { AppService } from '../service/app.service';

// store
import { StoreModule } from '@ngrx/store';
import { indexReducer } from '../store/index.reducer';

// i18n
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// perfect-scrollbar
import { NgScrollbarModule } from 'ngx-scrollbar';

// apexchart
import { NgApexchartsModule } from 'ng-apexcharts';

// highlightjs
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';

// tippy
import { NgxTippyModule } from 'ngx-tippy-wrapper';

// headlessui
import { MenuModule } from 'headlessui-angular';

// modal
import { ModalModule } from 'angular-custom-modal';
import { AllTemplateFrontComponent } from '../FrontOffice/all-template-front/all-template-front.component';
import { FooterFrontComponent } from '../FrontOffice/footer-front/footer-front.component';
import { HeaderFrontComponent } from '../FrontOffice/header-front/header-front.component';
import { HomeFrontComponent } from '../FrontOffice/home-front/home-front.component';
// sortable
import { SortablejsModule } from '@dustfoundation/ngx-sortablejs';

// quill editor
import { QuillModule } from 'ngx-quill';

// dashboard
import { IndexComponent } from '../index';
import { AnalyticsComponent } from '../components/analytics/analytics';
import { FinanceComponent } from '../components/company-analytics/finance';
import { CryptoComponent } from '../components/crypto/crypto';

// widgets
import { WidgetsComponent } from '../components/widgets/widgets';

// tables
import { TablesComponent } from '../components/tables/tables';

// font-icons
import { FontIconsComponent } from '../components/font-icons/font-icons';

// charts
import { ChartsComponent } from '../components/charts/charts';

// dragndrop
import { DragndropComponent } from '../components/dragDrop/dragndrop';

// pages
import { KnowledgeBaseComponent } from '../pages/knowledge-base';
import { FaqComponent } from '../pages/faq';

// Layouts
import { AppLayout } from './app-layout';
import { AuthLayout } from './auth-layout';

import { HeaderComponent } from './header/header';
import { FooterComponent } from './footer/footer';
import { SidebarComponent } from './sidebar/sidebar';
import { IconModule } from '../shared/icon/icon.module';
import { WebinarsComponent } from '../components/webinars/webinars.component';


import { CompanyAnalyticsComponent } from '../components/company-analytics/companyAnalytics';
import { NgxPaginationModule } from 'ngx-pagination';
import { InternshipService } from '../service/internshipStudent/internship.service';
import { InternshipStudentInterceptor } from '../service/internshipStudent/internship-student.interceptor';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FilterPipe } from '../filter.pipe';
import { CompanyComponent } from '../components/manage/company/company.component';

import { ChatComponent } from '../components/StudentChat/chat';
import { DataTableModule } from '@bhplugin/ng-datatable';
import { AngJson2excelBtnModule } from 'ang-json2excel-btn';
import { StudentListComponent } from '../components/StudentSupervisor/student-list';
import { StudentTableComponent } from '../components/StudentAdmin/student-table';
import { ProfTableComponent } from '../components/SupervisorAdmin/prof-table';
import { PdfViewerComponent, PdfViewerModule } from 'ng2-pdf-viewer';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { CommonModule } from '@angular/common';
import { ResumeComponent } from '../components/resume/resume.component';
import { InternshpsComponent } from '../components/internshps/internshps.component';
import { ArticleListComponent } from '../components/article-list/article-list.component';
import { ArticleFormComponent } from '../components/article-form/article-form.component';
import { ArticleEditComponent } from '../components/article-edit/article-edit.component';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { TendanceArticleComponent } from '../components/tendance-article/tendance-article.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { ShopComponent } from '../FrontOffice/shop/shop.component';
import { NgxQRCodeModule } from 'ngx-qrcode2';
import { LoginComponent } from '../FrontOffice/login/login.component';
import { FarmerDashboardComponent } from '../components/farmer-dashboard/farmer-dashboard.component';
import { StatistiquesAdminFarmerComponent } from '../components/statistiques-admin-farmer/statistiques-admin-farmer.component';





@NgModule({
    imports: [
        RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        MatDatepickerModule,
        NgxPaginationModule,
        ReactiveFormsModule,
        HttpClientModule,
        Ng2SearchPipeModule,
        FormsModule,
        MatDialogModule,
        MatSnackBarModule,
        DataTableModule,
        MatFormFieldModule,
        MatNativeDateModule,
        MatFormFieldModule,
        MatButtonModule,
        IconModule,
        AngJson2excelBtnModule,
        CommonModule,
        MatNativeDateModule,
         

        
        


        
        
        
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: httpTranslateLoader,
                deps: [HttpClient],
            },
        }),
        MenuModule,
        StoreModule.forRoot({ index: indexReducer }),
        NgxTippyModule,
        NgApexchartsModule,
        NgScrollbarModule.withConfig({
            visibility: 'hover',
            appearance: 'standard',
        }),
        HighlightModule,
        SortablejsModule,
        ModalModule,
        QuillModule.forRoot(),
        IconModule,
        HttpClientModule,
        PdfViewerModule
    ],
    declarations: [
        ChatComponent,
        AppComponent,
        HeaderComponent,
        FooterComponent,
        SidebarComponent,
        TablesComponent,
        FontIconsComponent,
        ChartsComponent,
        IndexComponent,
        AnalyticsComponent,
        FinanceComponent,
        CompanyAnalyticsComponent,
        CryptoComponent,
        WidgetsComponent,
        DragndropComponent,
        AppLayout,
        AuthLayout,
        AllTemplateFrontComponent,
        FooterFrontComponent,
        HeaderFrontComponent,
        HomeFrontComponent,
        KnowledgeBaseComponent,
        FaqComponent,
        WebinarsComponent,
        FilterPipe,
        StudentListComponent,
        StudentTableComponent,
        ProfTableComponent,
        ArticleListComponent,
        ArticleFormComponent,
        ArticleEditComponent,
        ConfirmationDialogComponent,
        TendanceArticleComponent,
        ShopComponent,
        LoginComponent,
        FarmerDashboardComponent,
        StatistiquesAdminFarmerComponent


        

    ],

    providers: [
        AppService,
        Title,
        {
            provide: HIGHLIGHT_OPTIONS,
            useValue: {
                coreLibraryLoader: () => import('highlight.js/lib/core'),
                languages: {
                    json: () => import('highlight.js/lib/languages/json'),
                    typescript: () => import('highlight.js/lib/languages/typescript'),
                    xml: () => import('highlight.js/lib/languages/xml'),
                },
            },
        }
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AppComponent],
})
export class AppModule {}

// AOT compilation support
export function httpTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
platformBrowserDynamic().bootstrapModule(AppModule);
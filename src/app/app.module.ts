import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

//Routes
import { routes } from './app.route';

import { AppComponent } from './app.component';

// service
import { AppService } from './service/app.service';

// store
import { StoreModule } from '@ngrx/store';
import { indexReducer } from './store/index.reducer';

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
import { AllTemplateFrontComponent } from './FrontOffice/all-template-front/all-template-front.component';
import { FooterFrontComponent } from './FrontOffice/footer-front/footer-front.component';
import { HeaderFrontComponent } from './FrontOffice/header-front/header-front.component';
import { HomeFrontComponent } from './FrontOffice/home-front/home-front.component';
// sortable
import { SortablejsModule } from '@dustfoundation/ngx-sortablejs';

// quill editor
import { QuillModule } from 'ngx-quill';

// dashboard
import { IndexComponent } from './index';
import { AnalyticsComponent } from './components/analytics/analytics';
import { FinanceComponent } from './components/company-analytics/finance';
import { CryptoComponent } from './components/crypto/crypto';

// widgets
import { WidgetsComponent } from './components/widgets/widgets';

// tables
import { TablesComponent } from './components/tables/tables';

// font-icons
import { FontIconsComponent } from './components/font-icons/font-icons';

// charts
import { ChartsComponent } from './components/charts/charts';

// dragndrop
import { DragndropComponent } from './components/dragDrop/dragndrop';

// pages
import { KnowledgeBaseComponent } from './pages/knowledge-base';
import { FaqComponent } from './pages/faq';

// Layouts
import { AppLayout } from './layouts/app-layout';
import { AuthLayout } from './layouts/auth-layout';

import { HeaderComponent } from './layouts/header/header';
import { FooterComponent } from './layouts/footer/footer';
import { SidebarComponent } from './layouts/sidebar/sidebar';
import { IconModule } from './shared/icon/icon.module';
import { WebinarsComponent } from './components/webinars/webinars.component';


import { CompanyAnalyticsComponent } from './components/company-analytics/companyAnalytics';
import { NgxPaginationModule } from 'ngx-pagination';
import { InternshipService } from './service/internshipStudent/internship.service';
import { InternshipStudentInterceptor } from './service/internshipStudent/internship-student.interceptor';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FilterPipe } from './filter.pipe';
import { CompanyComponent } from './components/manage/company/company.component';

import { ChatComponent } from './components/StudentChat/chat';
import { DataTableModule } from '@bhplugin/ng-datatable';
import { AngJson2excelBtnModule } from 'ang-json2excel-btn';
import { StudentListComponent } from './components/StudentSupervisor/student-list';
import { StudentTableComponent } from './components/StudentAdmin/student-table';
import { ProfTableComponent } from './components/SupervisorAdmin/prof-table';
import { PdfViewerComponent, PdfViewerModule } from 'ng2-pdf-viewer';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { CommonModule } from '@angular/common';
import { ResumeComponent } from './components/resume/resume.component';
import { InternshpsComponent } from './components/internshps/internshps.component';
import { CommandesListeComponent } from './components/commandes-liste/commandes-liste.component';
import { PanierComponent } from './FrontOffice/panier/panier.component';
import { ShopComponent } from './FrontOffice/shop/shop.component';
import { CheckoutComponent } from './FrontOffice/checkout/checkout.component';
import { PaymentComponent } from './FrontOffice/payment/payment.component';
import { DetailsComponent } from './components/details/details.component';
import { OnlineComponent } from './FrontOffice/online/online.component';



@NgModule({
    imports: [
        RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        NgxPaginationModule,
        ReactiveFormsModule,
        HttpClientModule,
        Ng2SearchPipeModule,
        DataTableModule,
        AngJson2excelBtnModule,
        CommonModule,
        
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
        CommandesListeComponent,
        PanierComponent,
        ShopComponent,
        CheckoutComponent,
        PaymentComponent,
        DetailsComponent,
        OnlineComponent
        

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
    bootstrap: [AppComponent],
})
export class AppModule {}

// AOT compilation support
export function httpTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
platformBrowserDynamic().bootstrapModule(AppModule);
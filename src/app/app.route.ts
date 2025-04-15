 import { Routes } from '@angular/router';

// dashboard
import { IndexComponent } from './index';
import { AnalyticsComponent } from './components/analytics/analytics';
import { CryptoComponent } from './components/crypto/crypto';

// widgets
import { WidgetsComponent } from './components/widgets/widgets';

// tables
import { TablesComponent } from './components/tables/tables';

// font-icons
import { FontIconsComponent } from './components/font-icons/font-icons';

// charts
import { ChartsComponent } from './components/charts/charts';
import { AllTemplateFrontComponent } from './FrontOffice/all-template-front/all-template-front.component';
import { HomeFrontComponent } from './FrontOffice/home-front/home-front.component';
import { AboutComponent } from './FrontOffice/about/about.component';
import { TeamComponent } from './FrontOffice/team/team.component';
import { ProduitsComponent } from './FrontOffice/produits/produits.component';
// dragndrop
import { DragndropComponent } from './components/dragDrop/dragndrop';

// layouts
import { AppLayout } from './layouts/app-layout';
import { AuthLayout } from './layouts/auth-layout';

// pages
import { KnowledgeBaseComponent } from './pages/knowledge-base';
import { FaqComponent } from './pages/faq';

import { WebinarsComponent } from './components/webinars/webinars.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ResumeComponent } from './components/resume/resume.component';
import { InternshpsComponent } from './components/orders/internshps.component';
import { ForumComponent } from './apps/forum/forum.component';
import { CompanyAnalyticsComponent } from './components/company-analytics/companyAnalytics';
import { StudentComponent } from './components/manage/student/student.component';
import { SupervisorComponent } from './components/manage/supervisor/supervisor.component';
import { CompanyComponent } from './components/manage/company/company.component';
import { InternshipsOfferComponent } from './components/internshipsOffer/internshipsOffer';
import { ProfTableComponent } from './components/SupervisorAdmin/prof-table';
import { StudentTableComponent } from './components/StudentAdmin/student-table';
import { StudentListComponent } from './components/StudentSupervisor/student-list';
import { ChatComponent } from './components/StudentChat/chat';
import { LoginComponent } from './FrontOffice/login/login.component';
import { RegisterComponent } from './FrontOffice/register/register.component';
import { OrdersComponent } from './components/orders/orders.component';
import { TransporteurLayoutComponent } from './layouts/transporteur-layout/transporteur-layout.component';
import { DeliveriesComponent } from './components/deliveries/deliveries.component';
import { DetailLivraisonComponent } from './components/detail-livraison/detail-livraison.component';
import { AdminDeliveriesComponent } from './components/admin-deliveries/admin-deliveries.component';
import { ModifierLivraisonComponent } from './components/modifier-livraison/modifier-livraison.component';
import { ShowlocationComponent } from './components/showlocation/showlocation.component';
import { AdmindashboardComponent } from './components/admindashboard/admindashboard.component';
import { TransporteurDashboardComponent } from './components/transporteur-dashboard/transporteur-dashboard.component';
import { ReclamationTransporteurComponent } from './components/reclamation-transporteur/reclamation-transporteur.component';
import { DeliveriesClientComponent } from './FrontOffice/deliveries-client/deliveries-client.component';


export const routes: Routes = [
    {
        path:"",
        component:AllTemplateFrontComponent,
        children:[
          {
            path:"",
            component:HomeFrontComponent
          },
          {
            path:"home",
            component:HomeFrontComponent
          },
          {
            path:'login',
            component:LoginComponent},
         {
              path:'register',
              component:RegisterComponent},
        {
          path:'about',
          component:AboutComponent},
          {
            path:'team',
            component:TeamComponent},
          {
              path:'produit',
              component:ProduitsComponent}
           
         
        ]
      },
    {
        path: 'admin',
        component: AppLayout,
        children: [
            // dashboard
            { path: 'template', component: IndexComponent, title: 'Sales Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'analytics', component: AnalyticsComponent, title: 'Analytics Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            // { path: 'finance', component: FinanceComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'crypto', component: CryptoComponent, title: 'Crypto Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },

            //apps
            { path: '', loadChildren: () => import('./apps/apps.module').then((d) => d.AppsModule) },

            // widgets
            { path: 'widgets', component: WidgetsComponent, title: 'Widgets | VRISTO - Multipurpose Tailwind Dashboard Template' },

            // components
            { path: '', loadChildren: () => import('./components/components.module').then((d) => d.ComponentsModule) },

            // elements
            // { path: '', loadChildren: () => import('./elements/elements.module').then((d) => d.ElementsModule) },

            // forms
            // { path: '', loadChildren: () => import('./forms/form.module').then((d) => d.FormModule) },

            // users
            { path: '', loadChildren: () => import('./users/user.module').then((d) => d.UsersModule) },

            // tables
            { path: 'tables', component: TablesComponent, title: 'Tables | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: '', loadChildren: () => import('./datatables/datatables.module').then((d) => d.DatatablesModule) },

            // font-icons
            { path: 'font-icons', component: FontIconsComponent, title: 'Font Icons | VRISTO - Multipurpose Tailwind Dashboard Template' },

            // charts
            { path: 'charts', component: ChartsComponent, title: 'Charts | VRISTO - Multipurpose Tailwind Dashboard Template' },

            // dragndrop
            { path: 'dragndrop', component: DragndropComponent, title: 'Dragndrop | VRISTO - Multipurpose Tailwind Dashboard Template' },

            // pages
            { path: 'pages/knowledge-base', component: KnowledgeBaseComponent, title: 'Knowledge Base | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'pages/faq', component: FaqComponent, title: 'FAQ | VRISTO - Multipurpose Tailwind Dashboard Template' },

            { path: 'webinars', component: WebinarsComponent, title: 'FAQ | VRISTO - Multipurpose Tailwind Dashboard Template' },

            { path: 'forum', component: ForumComponent, title: 'FAQ | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'supervisor/chat', component: ChatComponent, title: 'Chat | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'profile', component: ProfileComponent, title: 'Resume | VRISTO - Multipurpose Tailwind Dashboard Template'},
            { path: 'resume', component: ResumeComponent, title: 'Resume | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'myInternships', component: InternshpsComponent, title: 'Internships | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'dashboard', component: IndexComponent, title: 'Sales Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'analytics', component: AnalyticsComponent, title: 'Analytics Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'company-analytics', component: CompanyAnalyticsComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'manage-students', component: StudentComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'manage-supervisors', component: SupervisorComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'manage-companies', component: CompanyComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { path: 'order', component: OrdersComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
            { 
                path: 'deliveriesAdmin', 
                component: AdminDeliveriesComponent,
                title: 'Livraisons' 
            },
            { 
                path: 'deliveries/details/:id',  // Chemin complet et hiérarchique
                component: DetailLivraisonComponent,
                title: 'Détails de livraison'  // Titre plus approprié
            },
            { 
                path: 'dashboardAdmin', 
                component: AdmindashboardComponent,
                title: 'dashboard' 
            }

        ],
    },
    {
        path: 'transporteur/:id',
        component: TransporteurLayoutComponent,
        children: [
            { 
                path: 'deliveries', 
                component: DeliveriesComponent,
                title: 'Livraisons' 
            },
            { 
                path: 'deliveries/details/:id',  // Chemin complet et hiérarchique
                component: DetailLivraisonComponent,
                title: 'Détails de livraison'  // Titre plus approprié
            },
            { 
                path: 'deliveries/modifier/:id',  // Chemin complet et hiérarchique
                component: ModifierLivraisonComponent,
                title: 'Modification de livraison'  // Titre plus approprié
            },
            { path: 'deliveries/show-location/:adresse/:delegation'
                , component: ShowlocationComponent },
            { path: 'dashboardTransporteur'
            , component: TransporteurDashboardComponent },

            { path: 'deliveries/reclamationTransporteur/:id', 
                component: ReclamationTransporteurComponent,
                title: 'Réclamation'  }
        ],
    },
    {
        path: 'client/:id',
        component:AllTemplateFrontComponent,
        children: [
            { 
                path: 'deliveries', 
                component: DeliveriesClientComponent,
                title: 'Livraisons' 
            },
            { 
                path: 'deliveries/details/:id', 
                component: DetailLivraisonComponent,
                title: 'details' 
            },
            { 
                path: 'deliveries/modifier/:id',  // Chemin complet et hiérarchique
                component: ModifierLivraisonComponent,
                title: 'Modification de livraison'  // Titre plus approprié
            },
           
        ],
    },
    {
        path: '',
        component: AuthLayout,
        children: [
            // pages
            { path: '', loadChildren: () => import('./pages/pages.module').then((d) => d.PagesModule) },

            // auth
            { path: '', loadChildren: () => import('./components/auth/auth.module').then((d) => d.AuthModule) },
        ],
    },
];



// import { Routes } from '@angular/router';

// // dashboard
// import { IndexComponent } from './index';
// import { AnalyticsComponent } from './components/analytics/analytics';
// import { FinanceComponent } from './components/company-analytics/finance';
// import { CryptoComponent } from './components/crypto/crypto';

// // widgets
// import { WidgetsComponent } from './components/widgets/widgets';

// // tables
// import { TablesComponent } from './components/tables/tables';

// // font-icons
// import { FontIconsComponent } from './components/font-icons/font-icons';

// // charts
// import { ChartsComponent } from './components/charts/charts';

// // dragndrop
// import { DragndropComponent } from './components/dragDrop/dragndrop';

// // layouts
// import { AppLayout } from './layouts/app-layout';
// import { AuthLayout } from './layouts/auth-layout';

// // pages
// import { KnowledgeBaseComponent } from './pages/knowledge-base';
// import { FaqComponent } from './pages/faq';
// import { CompanyAnalyticsComponent } from './components/company-analytics/companyAnalytics';

// export const routes: Routes = [
//     {
//         path: '',
//         component: AppLayout,
//         children: [
//             // dashboard
//             { path: 'template', component: IndexComponent, title: 'Sales Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
//             { path: 'analytics', component: AnalyticsComponent, title: 'Analytics Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
//             { path: 'company-analytics', component: CompanyAnalyticsComponent, title: 'Finance Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },
//             { path: 'crypto', component: CryptoComponent, title: 'Crypto Admin | VRISTO - Multipurpose Tailwind Dashboard Template' },

//             //apps
//             { path: '', loadChildren: () => import('./apps/apps.module').then((d) => d.AppsModule) },

//             // widgets
//             { path: 'widgets', component: WidgetsComponent, title: 'Widgets | VRISTO - Multipurpose Tailwind Dashboard Template' },

//             // components
//             { path: '', loadChildren: () => import('./components/components.module').then((d) => d.ComponentsModule) },

//             // elements
//             { path: '', loadChildren: () => import('./components/elements/elements.module').then((d) => d.ElementsModule) },

//             // forms
//             { path: '', loadChildren: () => import('./components/forms/form.module').then((d) => d.FormModule) },

//             // users
//             { path: '', loadChildren: () => import('./users/user.module').then((d) => d.UsersModule) },

//             // tables
//             { path: 'tables', component: TablesComponent, title: 'Tables | VRISTO - Multipurpose Tailwind Dashboard Template' },
//             { path: '', loadChildren: () => import('./datatables/datatables.module').then((d) => d.DatatablesModule) },

//             // font-icons
//             { path: 'font-icons', component: FontIconsComponent, title: 'Font Icons | VRISTO - Multipurpose Tailwind Dashboard Template' },

//             // charts
//             { path: 'charts', component: ChartsComponent, title: 'Charts | VRISTO - Multipurpose Tailwind Dashboard Template' },

//             // dragndrop
//             { path: 'dragndrop', component: DragndropComponent, title: 'Dragndrop | VRISTO - Multipurpose Tailwind Dashboard Template' },

//             // pages
//             { path: 'pages/knowledge-base', component: KnowledgeBaseComponent, title: 'Knowledge Base | VRISTO - Multipurpose Tailwind Dashboard Template' },
//             { path: 'pages/faq', component: FaqComponent, title: 'FAQ | VRISTO - Multipurpose Tailwind Dashboard Template' },
//         ],
//     },

//     {
//         path: '',
//         component: AuthLayout,
//         children: [
//             // pages
//             { path: '', loadChildren: () => import('./pages/pages.module').then((d) => d.PagesModule) },

//             // auth
//             { path: '', loadChildren: () => import('./components/auth/auth.module').then((d) => d.AuthModule) },
//         ],
//     },
// ];

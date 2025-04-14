import { Routes } from '@angular/router';

// dashboard
import { IndexComponent } from './index';
import { AnalyticsComponent } from './components/analytics/analytics';
import { CryptoComponent } from './components/crypto/crypto';

// widgets
import { WidgetsComponent } from './components/widgets/widgets';

// tables
import { TablesComponent } from './components/tables/tables';

// icônes de police
import { FontIconsComponent } from './components/font-icons/font-icons';

// graphiques
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
import { InternshpsComponent } from './components/internshps/internshps.component';
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
import { ArticleListComponent } from './components/article-list/article-list.component';
import { ArticleFormComponent } from './components/article-form/article-form.component';
import { ArticleEditComponent } from './components/article-edit/article-edit.component';
import { TendanceArticleComponent } from './components/tendance-article/tendance-article.component';
import { ArticleDetailsComponent } from './components/article-details/article-details.component';
import { ShopComponent } from './FrontOffice/shop/shop.component';
import { FarmerDashboardComponent } from './components/farmer-dashboard/farmer-dashboard.component';
import { StatistiquesAdminFarmerComponent } from './components/statistiques-admin-farmer/statistiques-admin-farmer.component';

export const routes: Routes = [
    {
        path: '',
        component: AllTemplateFrontComponent,
        children: [
            {
                path: '',
                component: HomeFrontComponent
            },
            {
                path: 'login',
                component: LoginComponent
            },
            {
                path: 'register',
                component: RegisterComponent
            },
            {
                path: 'about',
                component: AboutComponent
            },
            {
                path: 'team',
                component: TeamComponent
            },
            {
                path: 'produit',
                component: ProduitsComponent
            },
            { path: 'shop',
                component: ShopComponent },
            
        ]
    },
    {
        path: 'admin',
        component: AppLayout,
        children: [
            // dashboard
            { path: 'template', component: IndexComponent, title: 'Admin | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'analytics', component: AnalyticsComponent, title: 'Analytique Admin | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'crypto', component: CryptoComponent, title: 'Crypto Admin | VRISTO - Tableau de bord multipurpose Tailwind' },

            // apps
            { path: '', loadChildren: () => import('./apps/apps.module').then((d) => d.AppsModule) },

            // widgets
            { path: 'widgets', component: WidgetsComponent, title: 'Widgets | VRISTO - Tableau de bord multipurpose Tailwind' },

            // composants
            { path: '', loadChildren: () => import('./components/components.module').then((d) => d.ComponentsModule) },

            // utilisateurs
            { path: '', loadChildren: () => import('./users/user.module').then((d) => d.UsersModule) },

            // tables
            { path: 'tables', component: TablesComponent, title: 'Tables | VRISTO - Tableau de bord multipurpose Tailwind' },

            // icônes de police
            { path: 'font-icons', component: FontIconsComponent, title: 'Icônes de police | VRISTO - Tableau de bord multipurpose Tailwind' },

            // graphiques
            { path: 'charts', component: ChartsComponent, title: 'Graphiques | VRISTO - Tableau de bord multipurpose Tailwind' },

            // dragndrop
            { path: 'dragndrop', component: DragndropComponent, title: 'Dragndrop | VRISTO - Tableau de bord multipurpose Tailwind' },

            // pages
            { path: 'pages/knowledge-base', component: KnowledgeBaseComponent, title: 'Base de connaissances | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'pages/faq', component: FaqComponent, title: 'FAQ | VRISTO - Tableau de bord multipurpose Tailwind' },

            { path: 'webinars', component: WebinarsComponent, title: 'Webinaires | VRISTO - Tableau de bord multipurpose Tailwind' },

            { path: 'forum', component: ForumComponent, title: 'Forum | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'supervisor/chat', component: ChatComponent, title: 'Chat | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'profile', component: ProfileComponent, title: 'Profil | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'resume', component: ResumeComponent, title: 'CV | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'myInternships', component: InternshpsComponent, title: 'Stages | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'dashboard', component: IndexComponent, title: 'Tableau de bord | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'company-analytics', component: CompanyAnalyticsComponent, title: 'Analytique entreprise | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'manage-students', component: StudentComponent, title: 'Gérer les étudiants | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'manage-supervisors', component: SupervisorComponent, title: 'Gérer les superviseurs | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'manage-companies', component: CompanyComponent, title: 'Gérer les entreprises | VRISTO - Tableau de bord multipurpose Tailwind' },
            { path: 'internships', component: InternshipsOfferComponent, title: 'Offres de stage | VRISTO - Tableau de bord multipurpose Tailwind' },
            {
                path: 'admin/supervisors',
                component: ProfTableComponent,
                title: 'Tableau des superviseurs | VRISTO - Tableau de bord multipurpose Tailwind',
            },
            {
                path: 'admin/students',
                component: StudentTableComponent,
                title: 'Tableau des étudiants | VRISTO - Tableau de bord multipurpose Tailwind',
            },
            {
                path: 'supervisor/students',
                component: StudentListComponent,
                title: 'Liste des étudiants | VRISTO - Tableau de bord multipurpose Tailwind',
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
    { path: 'articles', component: ArticleListComponent },
    //{ path: 'articles/create', component: ArticleFormComponent },
    { path: 'edit-article/:id', component: ArticleEditComponent }, // Modifier un article
    {
        path: 'article-list',
        component: ArticleListComponent
      },
      { path: 'tendance', component: TendanceArticleComponent },
      { path: 'article-details/:id', component: ArticleDetailsComponent },


      { path: 'stat', component: StatistiquesAdminFarmerComponent },
      { path: 'ajouter-article/:id', component: ArticleFormComponent },


        { path: 'articles/:id', component: FarmerDashboardComponent }  // Route pour les articles d'un agriculteur


];

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// highlightjs
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';

// modal
import { ModalModule } from 'angular-custom-modal';

// counter
import { CountUpModule } from 'ngx-countup';

// lightbox
import { LightboxModule } from 'ngx-lightbox';

// headlessui
import { MenuModule } from 'headlessui-angular';

// icon
import { IconModule } from 'src/app/shared/icon/icon.module';

import { TabsComponent } from './tabs/tabs';
import { AccordionsComponent } from './accordions/accordions';
import { ModalsComponent } from './modals/modals';
import { CardsComponent } from './cards/cards';
import { CarouselComponent } from './carousel/carousel';
import { CountdownComponent } from './countdown/countdown';
import { CounterComponent } from './counter/counter';
import { SweetalertComponent } from './sweetalert/sweetalert';
import { TimelineComponent } from './timeline/timeline';
import { NotificationsComponent } from './notification/notifications';
import { MediaObjectComponent } from './media-object/media-object';
import { ListGroupComponent } from './listGroup/list-group';
import { PricingTableComponent } from './price-table/pricing-table';
import { LightboxComponent } from './lightbox/lightbox';

import { ForumComponent } from '../apps/forum/forum.component';

import { ResumeComponent } from './resume/resume.component';
import { ProfileComponent } from './profile/profile.component';
import { InternshpsComponent } from './internshps/internshps.component';
import { NgScrollbar, NgScrollbarModule } from 'ngx-scrollbar';
import { StudentComponent } from './manage/student/student.component';
import { CompanyComponent } from './manage/company/company.component';
import { SupervisorComponent } from './manage/supervisor/supervisor.component';
import { PdfViewerComponent, PdfViewerModule } from 'ng2-pdf-viewer';
import { CommonModule } from '@angular/common';

const routes: Routes = [
    { path: 'components/tabs', component: TabsComponent, title: 'Tabs | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/accordions', component: AccordionsComponent, title: 'Accordions | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/modals', component: ModalsComponent, title: 'Modals | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/cards', component: CardsComponent, title: 'Cards | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/carousel', component: CarouselComponent, title: 'Carousel | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/countdown', component: CountdownComponent, title: 'Countdown | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/counter', component: CounterComponent, title: 'Counter | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/sweetalert', component: SweetalertComponent, title: 'Sweetalert | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/timeline', component: TimelineComponent, title: 'Timeline | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/notifications', component: NotificationsComponent, title: 'Notifications | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/media-object', component: MediaObjectComponent, title: 'Media Object | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/list-group', component: ListGroupComponent, title: 'List Group | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/pricing-table', component: PricingTableComponent, title: 'Pricing Table | VRISTO - Multipurpose Tailwind Dashboard Template' },
    { path: 'components/lightbox', component: LightboxComponent, title: 'Lightbox | VRISTO - Multipurpose Tailwind Dashboard Template' },

];
@NgModule({
    imports: [
        RouterModule.forChild(routes),
        FormsModule,
        ReactiveFormsModule,
        HighlightModule,
        ModalModule,
        CountUpModule,
        LightboxModule,
        MenuModule,
        IconModule,
        NgScrollbarModule,
        PdfViewerModule,
        CommonModule
    ],
    declarations: [
        TabsComponent,
        AccordionsComponent,
        ModalsComponent,
        CardsComponent,
        CarouselComponent,
        CountdownComponent,
        CounterComponent,
        SweetalertComponent,
        TimelineComponent,
        NotificationsComponent,
        MediaObjectComponent,
        ListGroupComponent,
        PricingTableComponent,
        LightboxComponent,
        ProfileComponent,
        InternshpsComponent,
        StudentComponent,
        CompanyComponent,
        SupervisorComponent,
        ResumeComponent

        
        
        
    ],
    providers: [
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
        },
    ],
})
export class ComponentsModule {}

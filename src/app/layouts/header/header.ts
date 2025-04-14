import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Store } from '@ngrx/store';
import { Router, NavigationEnd } from '@angular/router';
import { AppService } from 'src/app/service/app.service';
import { animate, style, transition, trigger, state } from '@angular/animations';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

interface Notification {
  id: number;
  profile: string;
  message: string;
  time: string;
}

interface Message {
  id: number;
  image: SafeHtml;
  title: string;
  message: string;
  time: string;
}

@Component({
  moduleId: module.id,
  selector: 'header',
  templateUrl: './header.html',
  animations: [
    trigger('toggleAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'scale(0.95)' }),
        animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' })),
      ]),
      transition(':leave', [
        animate('75ms ease-in', style({ opacity: 0, transform: 'scale(0.95)' })),
      ]),
    ]),
    trigger('avatarClickAnimation', [
      state('inactive', style({ transform: 'scale(1)' })),
      state('active', style({ transform: 'scale(1.1)' })),
      transition('inactive => active', animate('100ms ease-in')),
      transition('active => inactive', animate('100ms ease-out')),
    ]),
  ],
})
export class HeaderComponent implements OnInit, OnDestroy {
  store: any;
  search = false;

  dropdownOpen: Record<'notifications' | 'messages' | 'profile', boolean> = {
    notifications: false,
    messages: false,
    profile: false,
  };

  notifications: Notification[] = [];
  messages: Message[] = [];

  private storeSubscription!: Subscription;
  private routerSubscription!: Subscription;

  constructor(
    public translate: TranslateService,
    public storeData: Store<any>,
    public router: Router,
    private appSetting: AppService,
    private sanitizer: DomSanitizer
  ) {
    this.initMessages();
  }

  ngOnInit(): void {
    this.storeSubscription = this.storeData
      .select((state) => state.index)
      .subscribe((state) => {
        this.store = state;
      });

    this.routerSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setActiveDropdown();
      }
    });

    this.setActiveDropdown();
  }

  ngOnDestroy(): void {
    this.storeSubscription?.unsubscribe();
    this.routerSubscription?.unsubscribe();
  }

  @HostListener('document:click', ['$event'])
  onWindowClick(event: Event): void {
    const target = event.target as HTMLElement;

    if (!target.closest('.profile-dropdown') && !target.closest('.icon-avatar')) {
      this.dropdownOpen.profile = false;
    }

    if (!target.closest('.dropdown-container')) {
      this.dropdownOpen.notifications = false;
      this.dropdownOpen.messages = false;
    }
  }

  toggleDropdown(type: 'notifications' | 'messages' | 'profile', event?: Event): void {
    event?.stopPropagation();
    this.closeAllDropdowns(type);
    this.dropdownOpen[type] = !this.dropdownOpen[type];
  }

  private closeAllDropdowns(except?: 'notifications' | 'messages' | 'profile'): void {
    for (const key of Object.keys(this.dropdownOpen) as (keyof typeof this.dropdownOpen)[]) {
      if (key !== except) {
        this.dropdownOpen[key] = false;
      }
    }
  }

  setActiveDropdown(): void {
    const currentPath = window.location.pathname;
    const activeLink = document.querySelector(`ul.horizontal-menu a[routerLink="${currentPath}"]`);

    if (activeLink) {
      document.querySelectorAll('ul.horizontal-menu .nav-link.active').forEach((el) => {
        el.classList.remove('active');
      });

      activeLink.classList.add('active');

      const subMenu = activeLink.closest('ul.sub-menu');
      const parentMenu = subMenu?.closest('li.menu')?.querySelector('.nav-link');

      if (parentMenu) {
        setTimeout(() => {
          parentMenu.classList.add('active');
        });
      }
    }
  }

  removeNotification(id: number): void {
    this.notifications = this.notifications.filter((n) => n.id !== id);
  }

  removeMessage(id: number): void {
    this.messages = this.messages.filter((m) => m.id !== id);
  }

  changeLanguage(item: { code: string }): void {
    this.translate.use(item.code);
    this.appSetting.toggleLanguage(item);

    const isArabic = ['ae', 'ar'].includes(item.code.toLowerCase());
    this.storeData.dispatch({ type: 'toggleRTL', payload: isArabic ? 'rtl' : 'ltr' });

    window.location.reload();
  }

  private initMessages(): void {
    this.messages = [
      {
        id: 1,
        image: this.safeSvg('success'),
        title: 'Congratulations!',
        message: 'Your OS has been updated.',
        time: '1hr',
      },
      {
        id: 2,
        image: this.safeSvg('info'),
        title: 'Did you know?',
        message: 'You can switch between artboards.',
        time: '2hr',
      },
      {
        id: 3,
        image: this.safeSvg('danger'),
        title: 'Something went wrong!',
        message: 'Send Report',
        time: '2days',
      },
      {
        id: 4,
        image: this.safeSvg('warning'),
        title: 'Warning',
        message: 'Your password strength is low.',
        time: '5days',
      },
    ];
  }

  private safeSvg(type: 'success' | 'info' | 'danger' | 'warning'): SafeHtml {
    const svgMap: Record<string, string> = {
      success: `<span class="grid place-content-center w-9 h-9 rounded-full bg-success-light dark:bg-success text-success dark:text-success-light">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
        </svg>
      </span>`,
      info: `<span class="grid place-content-center w-9 h-9 rounded-full bg-info-light dark:bg-info text-info dark:text-info-light">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <circle cx="12" cy="12" r="10" /><line x1="12" y1="16" x2="12" y2="12" /><line x1="12" y1="8" x2="12.01" y2="8" />
        </svg>
      </span>`,
      danger: `<span class="grid place-content-center w-9 h-9 rounded-full bg-danger-light dark:bg-danger text-danger dark:text-danger-light">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
        </svg>
      </span>`,
      warning: `<span class="grid place-content-center w-9 h-9 rounded-full bg-warning-light dark:bg-warning text-warning dark:text-warning-light">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path d="M12 9v4m0 4h.01M12 5c-3.87 0-7 3.13-7 7s3.13 7 7 7 7-3.13 7-7-3.13-7-7-7z" />
        </svg>
      </span>`,
    };

    return this.sanitizer.bypassSecurityTrustHtml(svgMap[type]);
  }
}

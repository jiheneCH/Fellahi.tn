import {
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { slideDownUp } from 'src/app/shared/animations';
import { allNavItems } from './nav-items';

type UserRole = keyof typeof allNavItems; // 'admin' | 'farmer' | 'transporteur'

@Component({
  moduleId: module.id,
  selector: 'sidebar',
  templateUrl: './sidebar-modified.html',
  animations: [slideDownUp],
})
export class SidebarComponent implements OnInit {
  @Input() collapsed = false;

  navItems: any[] = [];
  active = false;
  activeDropdown: string[] = [];
  parentDropdown: string = '';

  constructor(
    public translate: TranslateService,
    public storeData: Store<any>,
    public router: Router
  ) {
    this.loadNavItems();
  }

  ngOnInit() {
    this.setActiveDropdown();
  }

  loadNavItems() {
    const roleFromStorage = localStorage.getItem('userRole')?.toLowerCase();
    const validRoles: UserRole[] = ['admin', 'farmer', 'transporteur'];

    if (roleFromStorage && validRoles.includes(roleFromStorage as UserRole)) {
      this.navItems = allNavItems[roleFromStorage as UserRole];
    } else {
      this.navItems = []; // Or provide a fallback: allNavItems['guest'] if defined
    }
  }

  setActiveDropdown() {
    const selector = document.querySelector(
      '.sidebar ul a[routerLink="' + window.location.pathname + '"]'
    );
    if (selector) {
      selector.classList.add('active');
      const ul: any = selector.closest('ul.sub-menu');
      if (ul) {
        let ele: any = ul.closest('li.menu')?.querySelectorAll('.nav-link') || [];
        if (ele.length) {
          ele = ele[0];
          setTimeout(() => {
            ele.click();
          });
        }
      }
    }
  }

  toggleMobileMenu() {
    if (window.innerWidth < 1024) {
      this.storeData.dispatch({ type: 'toggleSidebar' });
    }
  }

  toggleAccordion(name: string, parent?: string) {
    if (this.activeDropdown.includes(name)) {
      this.activeDropdown = this.activeDropdown.filter((d) => d !== name);
    } else {
      this.activeDropdown.push(name);
    }
  }
}

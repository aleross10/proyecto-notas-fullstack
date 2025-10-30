import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';

export const routes: Routes = [
  
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },

  // 2. Rutas corregidas
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },

  
  {
    path: 'notes',
    loadComponent: () => import('./notes/list/list').then(m => m.ListComponent),
    
  },
  {
    path: 'notes/create',
    loadComponent: () => import('./notes/form/form').then(m => m.FormComponent),
    
  },
  {
    path: 'notes/edit/:id',
    loadComponent: () => import('./notes/form/form').then(m => m.FormComponent),
    
  },
  {
    path: 'notes/share/:id',
    loadComponent: () => import('./notes/share/share').then(m => m.ShareComponent),
    
  },

  {
    path: '**',
    redirectTo: 'login'
  }
];


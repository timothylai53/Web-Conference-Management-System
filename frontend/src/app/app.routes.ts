import { Routes } from '@angular/router';

export const routes: Routes = [
  // Default redirect to auth/login
  { 
    path: '', 
    redirectTo: '/auth', 
    pathMatch: 'full' 
  },
  
  // Conference Management
  { 
    path: 'conference', 
    loadComponent: () => import('./conference/conference').then(m => m.ConferenceComponent)
  },
  { 
    path: 'conference/form', 
    loadComponent: () => import('./conference/conference-form').then(m => m.ConferenceFormComponent)
  },
  { 
    path: 'conference/form/:id', 
    loadComponent: () => import('./conference/conference-form').then(m => m.ConferenceFormComponent)
  },
  
  // Tracks Management
  { 
    path: 'tracks', 
    loadComponent: () => import('./tracks/tracks').then(m => m.TracksComponent)
  },
  { 
    path: 'tracks/form', 
    loadComponent: () => import('./tracks/track-form').then(m => m.TrackFormComponent)
  },
  { 
    path: 'tracks/form/:id', 
    loadComponent: () => import('./tracks/track-form').then(m => m.TrackFormComponent)
  },
  
  // Paper Submission
  { 
    path: 'paper-submission', 
    loadComponent: () => import('./paper-submission/paper-submission').then(m => m.PaperSubmission)
  },
  
  // Registration & Payment
  { 
    path: 'registration-payment', 
    loadComponent: () => import('./registration-payment/registration-payment.component').then(m => m.RegistrationPaymentComponent)
  },
  
  // Review Setup
  { 
    path: 'review-setup', 
    loadComponent: () => import('./review-setup/review-setup.component').then(m => m.ReviewSetupComponent)
  },
  
  // Review Execution (with child routes)
  { 
    path: 'review-execution',
    loadComponent: () => import('./review-execution/review-execution').then(m => m.ReviewExecution),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./review-execution/reviewer-dashboard/reviewer-dashboard').then(m => m.ReviewerDashboardComponent)
      },
      {
        path: 'grading/:assignmentId',
        loadComponent: () => import('./review-execution/grading/grading').then(m => m.GradingComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  
  // Scheduling Program
  { 
    path: 'scheduling-program', 
    loadComponent: () => import('./scheduling-program/schedule-view/schedule-view.component').then(m => m.ScheduleViewComponent)
  },
  { 
    path: 'scheduling-program/sessions', 
    loadComponent: () => import('./scheduling-program/session-creator/session-creator.component').then(m => m.SessionCreatorComponent)
  },
  { 
    path: 'scheduling-program/rooms', 
    loadComponent: () => import('./scheduling-program/room-manager/room-manager.component').then(m => m.RoomManagerComponent)
  },
  
  // User Profile
  { 
    path: 'user-profile', 
    loadComponent: () => import('./user-profile/user-profile').then(m => m.UserProfileComponent)
  },
  
  // Authentication
  { 
    path: 'auth', 
    loadComponent: () => import('./auth/auth').then(m => m.AuthComponent)
  },
  
  // Admin Dashboard
  { 
    path: 'admin-dashboard', 
    loadComponent: () => import('./admin-dashboard/admin-dashboard').then(m => m.AdminDashboard)
  },
  { 
    path: 'admin-dashboard/form', 
    loadComponent: () => import('./admin-dashboard/admin-form').then(m => m.AdminFormComponent)
  },
  
  // Wildcard route - 404
  { 
    path: '**', 
    redirectTo: '/conference' 
  }
];

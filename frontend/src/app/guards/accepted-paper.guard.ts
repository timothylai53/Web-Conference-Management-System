import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

export const acceptedPaperGuard: CanActivateFn = (route, state) => {
  const http = inject(HttpClient);
  const router = inject(Router);
  
  // Get current user from localStorage
  const currentUser = localStorage.getItem('currentUser');
  
  if (!currentUser) {
    // Not logged in, redirect to auth
    router.navigate(['/auth']);
    return false;
  }

  const user = JSON.parse(currentUser);
  const userId = user.user_id;

  // Check if user has accepted papers
  return http.get<boolean>(`http://localhost:8080/api/papers/user/${userId}/has-accepted`).pipe(
    map(hasAccepted => {
      if (hasAccepted) {
        return true;
      } else {
        // No accepted papers, show alert and redirect
        alert('Access denied: You must have at least one accepted paper to access registration and payment.');
        router.navigate(['/paper-submission']);
        return false;
      }
    }),
    catchError(error => {
      console.error('Error checking accepted papers:', error);
      alert('Error verifying paper acceptance status.');
      router.navigate(['/']);
      return of(false);
    })
  );
};

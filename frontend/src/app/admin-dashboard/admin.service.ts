import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ActivityLog } from './admin.model';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/log-activities';

  constructor(private http: HttpClient) {}

  // Get all activity logs
  getActivityLogs(): Observable<ActivityLog[]> {
    return this.http.get<ActivityLog[]>(this.apiUrl);  // Fetch all activity logs
  }

  // Get a single activity log by ID
  getActivityLogById(id: number): Observable<ActivityLog> {
    return this.http.get<ActivityLog>(`${this.apiUrl}/${id}`);  // Fetch a specific activity log by ID
  }

  // Create a new activity log
  createActivityLog(activityLog: ActivityLog): Observable<ActivityLog> {
    return this.http.post<ActivityLog>(this.apiUrl, activityLog);  // Create a new activity log
  }

  // Update an existing activity log
  updateActivityLog(id: number, activityLog: ActivityLog): Observable<ActivityLog> {
    return this.http.put<ActivityLog>(`${this.apiUrl}/${id}`, activityLog);  // Update an existing activity log by ID
  }

  // Delete an activity log
  deleteActivityLog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);  // Delete an activity log by ID
  }
}

export interface ActivityLog {
  log_id: number;
  user_id: number;
  user?: string;  // Optional user name
  action: string;
  details: string;
  time?: string;  // Optional time field
  login_time: string;
  logout_time: string;
}

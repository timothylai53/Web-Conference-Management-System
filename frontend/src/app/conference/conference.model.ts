export interface Conference {
  conference_id?: number;
  title: string;
  acronym?: string;
  start_date: string;
  end_date: string;
  location?: string;
  description?: string;
  website_url?: string;
  submission_deadline?: string;
  review_deadline?: string;
  rebuttal_deadline?: string;
  camera_ready_deadline?: string;
  registration_deadline?: string;
  late_submission_deadline?: string;
  status: 'upcoming' | 'ongoing' | 'completed';
  created_by?: number;
}

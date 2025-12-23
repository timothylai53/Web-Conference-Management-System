export interface Track {
  track_id?: number;
  conference_id: number; // Links this track to a specific conference
  name: string;
  description?: string;
}

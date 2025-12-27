# Paper Submission Module - Backend API Integration

## Overview
The paper submission module is now fully integrated with the Spring Boot backend REST API.

## Backend API Endpoints Used

### Papers API (`/api/papers`)
- `GET /api/papers` - Get all papers
- `GET /api/papers/{id}` - Get paper by ID
- `POST /api/papers` - Create new paper
- `PUT /api/papers/{id}` - Update paper
- `DELETE /api/papers/{id}` - Delete paper

### Authors API (`/api/author`)
- `POST /api/author` - Create author (auto-created from comma-separated names)

### Keywords API (`/api/keywords`)
- `POST /api/keywords` - Create keyword (auto-created from comma-separated keywords)

### Tracks API (`/api/tracks`)
- `GET /api/tracks` - Get all available tracks for selection

### Reviews API (`/api/reviews`)
- Available for future implementation

### Rebuttals API (`/api/rebuttals`)
- Available for future implementation

## Features Implemented

### 1. Paper Submission Form
- Connects to backend to submit papers
- Automatically creates authors from comma-separated input
- Automatically creates keywords from comma-separated input
- Validates all required fields
- Shows loading state during submission

### 2. View Submissions
- Fetches all papers from backend
- Displays paper details including status
- Shows submission date
- Allows deletion of papers (calls DELETE API)

### 3. Track Selection
- Dynamically loads tracks from backend API
- Falls back to default tracks if API fails

### 4. Error Handling
- Displays error messages for failed API calls
- Console logs errors for debugging
- User-friendly alert messages

## Configuration

### Backend URL
The backend API base URL is configured in `paper-submission.service.ts`:
```typescript
private baseUrl = 'http://localhost:8080/api';
```

**Important:** Update this URL if your backend runs on a different port or host.

### CORS Configuration
Make sure your Spring Boot backend has CORS enabled for the Angular frontend:
```java
@CrossOrigin(origins = "http://localhost:4200")
```

## Data Flow

### Submitting a Paper:
1. User fills form with title, authors (comma-separated), track, keywords (comma-separated), and abstract
2. Frontend validates input
3. Creates Paper object and POSTs to `/api/papers`
4. Backend returns saved paper with `paper_id`
5. Frontend creates Author entries for each comma-separated author name
6. Frontend creates Keyword entries for each comma-separated keyword
7. Success message shown and papers list refreshed

### Loading Papers:
1. Component loads on init or when user switches to submissions view
2. Calls GET `/api/papers` to fetch all papers
3. Calls GET `/api/tracks` to get track names
4. Maps paper data with track names for display
5. Renders papers in card layout

### Deleting Papers:
1. User clicks delete button
2. Confirmation dialog shown
3. Calls DELETE `/api/papers/{id}`
4. Removes paper from local list on success

## API Data Models

### Paper (Backend Model)
```typescript
{
  paper_id?: number;
  track_id: number;
  title: string;
  abstractText: string;
  submission_file?: string;
  file_type?: string;
  version?: number;
  plagiarism_score?: number;
  status?: string;
  submitted_by?: number;
  submitted_at?: Date;
  last_updated?: Date;
}
```

### Author (Backend Model)
```typescript
{
  author_id?: number;
  user_id?: number;
  paper_id?: number;
  first_name: string;
  last_name: string;
  email: string;
  affiliation?: string;
  is_corresponding_author?: boolean;
  author_order?: number;
}
```

### Keyword (Backend Model)
```typescript
{
  keyword_id?: number;
  keyword: string;
}
```

## Testing the Integration

1. **Start Backend Server:**
   ```bash
   # Navigate to backend directory
   ./mvnw spring-boot:run
   ```

2. **Start Frontend Server:**
   ```bash
   # Navigate to frontend directory
   npm start
   ```

3. **Access Application:**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080/api

4. **Test Scenarios:**
   - Submit a new paper with multiple authors and keywords
   - View all submitted papers
   - Delete a paper
   - Check browser console for API calls and responses

## Known Limitations

1. **Author Display:** Currently shows "Loading..." because authors are stored separately. Future enhancement can fetch and join author data.
2. **Keyword Display:** Currently shows "Loading..." because keywords are stored separately. Future enhancement can fetch and join keyword data.
3. **User Authentication:** Currently uses hardcoded `submitted_by: 1`. Should integrate with auth system.
4. **File Upload:** File upload feature not yet implemented (submission_file field).

## Future Enhancements

- [ ] Implement file upload for paper PDFs
- [ ] Fetch and display actual authors and keywords from database
- [ ] Integrate with authentication system for submitted_by field
- [ ] Add paper editing functionality
- [ ] Implement review and rebuttal features
- [ ] Add pagination for large paper lists
- [ ] Add search and filter capabilities
- [ ] Display plagiarism score and version information

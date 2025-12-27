# ScholarSync - Conference Management System

A complete Angular-based conference management system with integrated modules for conference management, paper submission, review execution, scheduling, and more.

## ✨ Features

### Core Modules
- **Conference Management** - Create, view, edit, and manage conferences
- **Track Management** - Organize conference tracks
- **Paper Submission** - Submit and manage academic papers with full author management
- **Registration & Payment** - Handle conference registrations and payment processing
- **Review Setup** - Assign reviewers and configure review criteria
- **Review Execution** - Execute peer reviews with grading and discussion
- **Scheduling Program** - Manage conference sessions, rooms, and schedules
- **User Profile** - User account and profile management
- **Admin Dashboard** - Administrative panel with activity logs
- **Authentication** - Login and registration system

### Technical Features
- ✅ **Standalone Components** - Modern Angular standalone architecture
- ✅ **Lazy Loading** - Optimized route-based code splitting
- ✅ **Material Design** - Angular Material UI components
- ✅ **Responsive Layout** - Header and footer across all pages
- ✅ **HTTP Client** - Integrated API service layer
- ✅ **Form Validation** - Reactive and template-driven forms
- ✅ **Router Navigation** - Complete routing configuration

## 🚀 Getting Started

### Prerequisites
- Node.js (v18 or higher)
- npm (v10 or higher)
- Angular CLI (v21 or higher)
- Backend API running on http://localhost:8080

### Installation

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```

4. **Open browser**
   Navigate to `http://localhost:4200`

## 📁 Project Structure

```
frontend/src/app/
├── layout/
│   ├── header/          # Navigation header component
│   └── footer/          # Footer component
├── admin-dashboard/     # Admin panel and management
├── auth/                # Authentication (login/register)
├── conference/          # Conference CRUD operations
├── tracks/              # Track management
├── paper-submission/    # Paper submission system
├── registration-payment/# Registration and payment
├── review-setup/        # Review configuration
├── review-execution/    # Review dashboard and grading
├── scheduling-program/  # Session and room scheduling
├── user-profile/        # User profile management
├── services/            # Shared services (AuthService, etc.)
├── app.routes.ts        # Main routing configuration
├── app.config.ts        # Application providers
└── app.ts               # Root component
```

## 🔗 Available Routes

| Route | Module | Description |
|-------|--------|-------------|
| `/` | Conference | Default landing page |
| `/conference` | Conference | View all conferences |
| `/conference/form` | Conference | Create new conference |
| `/conference/form/:id` | Conference | Edit conference |
| `/tracks` | Tracks | View all tracks |
| `/tracks/form` | Tracks | Create/Edit track |
| `/paper-submission` | Paper Submission | Submit and manage papers |
| `/registration-payment` | Registration | Register and pay for conferences |
| `/review-setup` | Review Setup | Configure reviews and assign reviewers |
| `/review-execution` | Review Execution | Reviewer dashboard |
| `/scheduling-program` | Scheduling | View conference schedule |
| `/scheduling-program/sessions` | Scheduling | Manage sessions |
| `/scheduling-program/rooms` | Scheduling | Manage rooms |
| `/user-profile` | User Profile | Edit user profile |
| `/auth` | Authentication | Login/Register |
| `/admin-dashboard` | Admin | Admin control panel |

## 🔧 Configuration

### API Endpoints
All services connect to backend API at `http://localhost:8080/api`

Key endpoints used:
- `/api/conferences` - Conference operations
- `/api/tracks` - Track management
- `/api/papers` - Paper submissions
- `/api/registrations` - Registration data
- `/api/payments` - Payment processing
- `/api/reviewers` - Reviewer management
- `/api/reviews` - Review operations
- `/api/sessions` - Session scheduling
- `/api/rooms` - Room management
- `/api/users` - User profile
- `/api/auth/login` - Authentication
- `/api/auth/register` - User registration

### Environment Variables
To change the API URL, update the service files in each module or create an environment configuration.

## 🎨 Design System

The application uses a consistent dark theme with:
- **Primary Navy**: `rgb(10, 22, 40)`
- **Accent Purple**: `rgb(139, 92, 246)`
- **Accent Blue**: `rgb(59, 130, 246)`
- **Text Light**: `rgba(255, 255, 255, 0.9)`

All components follow the design established in the header/footer for a cohesive look.

## 📦 Dependencies

### Production
- `@angular/animations` - Animation support
- `@angular/cdk` - Component Dev Kit
- `@angular/common` - Common Angular modules
- `@angular/core` - Angular core
- `@angular/forms` - Form handling
- `@angular/material` - Material Design components
- `@angular/platform-browser` - Browser support
- `@angular/router` - Routing
- `rxjs` - Reactive extensions
- `zone.js` - Zone support

### Development
- `@angular/cli` - Angular CLI
- `@angular/compiler-cli` - Template compiler
- `typescript` - TypeScript compiler
- `vitest` - Testing framework

## 🛠️ Development

### Build for production
```bash
npm run build
```

### Run tests
```bash
npm test
```

### Watch mode
```bash
npm run watch
```

## 🔌 Backend Integration

Ensure the backend server is running before starting the frontend. The backend should:
1. Run on `http://localhost:8080`
2. Have all required API endpoints implemented
3. Support CORS for `http://localhost:4200`
4. Return proper JSON responses

## 📝 Module Details

### Paper Submission
- Multi-step wizard interface
- Author management with order tracking
- Track selection
- File upload support
- Material Design components

### Review Execution
- Reviewer dashboard
- Assignment tracking
- Grading interface
- Discussion threads

### Scheduling Program
- Visual schedule view
- Session creation and management
- Room allocation
- Time slot management

### Registration & Payment
- Conference selection
- Registration type (student/professional)
- Early bird pricing
- Payment receipt upload
- Invoice management

## 🤝 Contributing

This is a group project. Each module was developed by different team members and integrated into a unified application.

## 📄 License

Educational project for UUM Web Engineering course.

## 👥 Team

JavaBoys3 - Assignment 3

---

**Note**: Make sure the backend API is running on port 8080 before starting the frontend application.

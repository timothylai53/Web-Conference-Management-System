# Quick Start Guide - ScholarSync Frontend

## 🚀 Quick Setup (3 Steps)

### 1. Install Dependencies
```bash
cd frontend
npm install
```

### 2. Start Backend API
Make sure your Java Spring Boot backend is running on `http://localhost:8080`

### 3. Start Frontend
```bash
npm start
```

Open browser: `http://localhost:4200`

---

## 📍 Navigation Guide

### Main Routes
- **Home/Conferences**: http://localhost:4200/conference
- **Submit Paper**: http://localhost:4200/paper-submission
- **Register**: http://localhost:4200/registration-payment
- **Reviews**: http://localhost:4200/review-execution
- **Schedule**: http://localhost:4200/scheduling-program
- **Login**: http://localhost:4200/auth
- **Admin**: http://localhost:4200/admin-dashboard

---

## ✅ What's Been Integrated

### ✓ All Modules Connected
- Conference Management ✅
- Track Management ✅
- Paper Submission ✅
- Registration & Payment ✅
- Review Setup ✅
- Review Execution ✅
- Scheduling Program ✅
- User Profile ✅
- Admin Dashboard ✅
- Authentication ✅

### ✓ Unified Layout
- Header with navigation on every page ✅
- Footer on every page ✅
- Consistent dark theme design ✅

### ✓ Routing Configured
- Lazy loading for performance ✅
- All routes mapped correctly ✅
- Default redirect to conferences ✅

### ✓ Dependencies Added
- Angular Material ✅
- Angular Animations ✅
- Angular CDK ✅
- HttpClient configured ✅
- Forms modules ✅

### ✓ Services Created
- AuthService for authentication ✅
- All module services configured ✅
- API endpoints pointing to localhost:8080 ✅

---

## 🔧 Common Tasks

### Add a New Route
Edit `src/app/app.routes.ts`

### Change API URL
Update service files in each module folder (search for `http://localhost:8080`)

### Add New Component
```bash
ng generate component my-component --standalone
```

### Build for Production
```bash
npm run build
```

Output will be in `dist/` folder

---

## 🐛 Troubleshooting

### "Cannot find module" errors
```bash
npm install
```

### Backend not connecting
- Check backend is running on port 8080
- Check CORS is enabled in backend
- Check browser console for errors

### Material components not showing
```bash
npm install @angular/material @angular/cdk @angular/animations
```

### Routing not working
- Clear browser cache
- Check base href in index.html: `<base href="/">`
- Restart dev server

---

## 📦 Package Scripts

| Command | Description |
|---------|-------------|
| `npm start` | Start development server on port 4200 |
| `npm run build` | Build for production |
| `npm run watch` | Build in watch mode |
| `npm test` | Run tests |

---

## 🎯 Module Responsibilities

| Module | Team Member | Status |
|--------|-------------|--------|
| Conference Management | Member 2 | ✅ Integrated |
| Track Management | Member 2 | ✅ Integrated |
| Paper Submission | Member 3 | ✅ Integrated |
| Registration & Payment | Member 4 | ✅ Integrated |
| Review Setup | Member 5 | ✅ Integrated |
| Review Execution | Member 5 | ✅ Integrated |
| Scheduling | Member 6 | ✅ Integrated |
| User Profile | Member 1 | ✅ Integrated |
| Auth System | Member 1 | ✅ Integrated |
| Admin Dashboard | Member 1 | ✅ Integrated |
| Layout (Header/Footer) | ALL | ✅ Integrated |

---

## 🔑 Important Files

- `src/app/app.routes.ts` - All route definitions
- `src/app/app.config.ts` - Application providers
- `src/app/app.html` - Main layout with header/footer
- `src/app/services/auth.service.ts` - Authentication service
- `src/styles.css` - Global styles
- `package.json` - Dependencies

---

## 📝 Notes

1. **All components are standalone** - No NgModules needed
2. **Lazy loading enabled** - Better performance
3. **Consistent styling** - Dark theme across all modules
4. **API ready** - All services point to backend
5. **Material Design** - Professional UI components

---

## 🤝 Team Workflow

1. Each member works on their module
2. Keep component standalone
3. Use the shared header/footer
4. Follow the dark theme colors
5. API calls to localhost:8080
6. Test your module in isolation
7. Integration is already done!

---

**Need Help?** Check INTEGRATION_README.md for detailed documentation.

# Role-Based Access Control Summary

## ✅ Current Role Configuration

### 🔐 Role Definitions

The system has **4 primary roles** stored in the database:

| Role ID | Role Name | Description |
|---------|-----------|-------------|
| 1 | Admin | System administrator with full access |
| 2 | Reviewer | Reviews and scores submitted papers |
| 3 | Author | Submits papers to conferences |
| 4 | Chair | Manages tracks and reviews |

---

## 📋 What Each Role Can See

### 👑 **Admin** (Full Access)
Navigation menu shows:
- ✅ Conferences
- ✅ Schedule
- ✅ Submission
- ✅ Registration
- ✅ Reviews
- ✅ Admin Panel (special access)

**Use Case:** Full system control, user management, all features

---

### 📝 **Author** (Submission + Registration Only)
Navigation menu shows:
- ✅ Submission (Paper Submission)
- ✅ Registration (Registration & Payment)
- ❌ Conferences (hidden)
- ❌ Schedule (hidden)
- ❌ Reviews (hidden)
- ❌ Review Setup (hidden)
- ❌ Admin Panel (hidden)

**Use Case:** Submit papers and register for conferences - exactly what you wanted!

---

### 🔍 **Reviewer** (Reviews Only)
Navigation menu shows:
- ✅ Reviews (Review Execution Dashboard)
- ❌ Conferences (hidden)
- ❌ Schedule (hidden)
- ❌ Submission (hidden)
- ❌ Registration (hidden)
- ❌ Admin Panel (hidden)

**Use Case:** Review assigned papers only

---

### 🎯 **Chair** (Conference Management)
Navigation menu shows:
- ✅ Conferences
- ✅ Schedule
- ✅ Review Setup (assign reviewers)
- ❌ Submission (hidden)
- ❌ Registration (hidden)
- ❌ Reviews (hidden)
- ❌ Admin Panel (hidden)

**Use Case:** Manage conferences, scheduling, and reviewer assignments

---

## 🧪 Test Accounts (After Running docker-compose up)

### Testing Role Access:

```bash
# Login as Author (should only see Submission + Registration)
Email: author@scholarsync.com
Password: author123
Expected Navigation: Submission, Registration

# Login as Reviewer (should only see Reviews)
Email: reviewer@scholarsync.com
Password: reviewer123
Expected Navigation: Reviews

# Login as Chair (should see Conferences + Schedule + Review Setup)
Email: chair@scholarsync.com
Password: chair123
Expected Navigation: Conferences, Schedule, Review Setup

# Login as Admin (should see everything)
Email: admin@scholarsync.com
Password: admin123
Expected Navigation: All items + Admin Panel
```

---

## 🔧 How It Works (Technical)

### Backend (AuthController.java)
1. User logs in with email/password
2. Backend finds user in `user` table
3. Looks up role in `user_role` table (many-to-many relationship)
4. Fetches role name from `role` table
5. Returns user object with `role` field: `"Admin"`, `"Reviewer"`, `"Author"`, or `"Chair"`

### Frontend (header.component.ts)
1. Receives user object from login
2. Checks `user.role` value
3. Sets boolean flags:
   - `isAdmin = (role === 'Admin')`
   - `isReviewer = (role === 'Reviewer')`
   - `isChair = (role === 'Chair')`
   - `isAuthor = (!isAdmin && !isReviewer && !isChair)` (default)

### Navigation Display (header.component.html)
Uses Angular `*ngIf` directives to show/hide menu items:
```html
<!-- Admin sees everything -->
<ng-container *ngIf="isAdmin">
  <a routerLink="/conference">Conferences</a>
  <a routerLink="/scheduling-program">Schedule</a>
  <a routerLink="/paper-submission">Submission</a>
  <a routerLink="/registration-payment">Registration</a>
  <a routerLink="/review-execution">Reviews</a>
</ng-container>

<!-- Authors only see Submission + Registration -->
<ng-container *ngIf="!isAdmin && !isReviewer && !isChair">
  <a routerLink="/paper-submission">Submission</a>
  <a routerLink="/registration-payment">Registration</a>
</ng-container>
```

---

## 🔄 What Was Fixed

### Before:
- ❌ User entity had `role` field but database didn't
- ❌ Roles stored in separate `user_role` table but not fetched
- ❌ Login returned `null` for role
- ❌ All users saw same navigation menu
- ❌ No role-based access control

### After:
- ✅ Removed `role` field from User entity
- ✅ AuthController fetches role from `user_role` + `role` tables
- ✅ Login returns proper role name in response
- ✅ Frontend displays role-appropriate navigation
- ✅ **Authors only see Submission + Registration** (as requested!)
- ✅ Each role has specific menu items

---

## 📊 Role Assignment in Database

Roles are assigned via the `user_role` table:

```sql
-- Example: Assign "Author" role to a user
INSERT INTO user_role (user_id, role_id) 
VALUES (3, 3); -- user_id=3, role_id=3 (Author)

-- Multiple roles possible (though not used in current UI)
INSERT INTO user_role (user_id, role_id) 
VALUES (5, 2), (5, 4); -- User 5 is both Reviewer and Chair
```

Current test data (from `03-insert_test_users.sql`):
- admin@scholarsync.com → Admin (role_id=1)
- author@scholarsync.com → Author (role_id=3)
- reviewer@scholarsync.com → Reviewer (role_id=2)
- chair@scholarsync.com → Chair (role_id=4)

---

## 🎯 Verification Steps

1. **Start the application:**
   ```bash
   cd backend
   docker-compose up -d
   cd ../frontend
   npm run dev
   ```

2. **Test Author Access:**
   - Login as `author@scholarsync.com` / `author123`
   - Check navigation menu
   - ✅ Should ONLY see: **Submission** and **Registration**
   - ❌ Should NOT see: Conferences, Schedule, Reviews

3. **Test Other Roles:**
   - Reviewer → Should only see "Reviews"
   - Chair → Should see "Conferences", "Schedule", "Review Setup"
   - Admin → Should see everything + Admin Panel

---

## 💡 Future Enhancements

**Route Guards** (Optional - for extra security):
Currently, navigation items are hidden but URLs are still accessible. You could add route guards:

```typescript
// Create auth.guard.ts
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const user = authService.getCurrentUser();
  
  const requiredRole = route.data['role'];
  if (requiredRole && user?.role !== requiredRole) {
    return false; // Block access
  }
  return true;
};

// In app.routes.ts
{
  path: 'admin-dashboard',
  canActivate: [authGuard],
  data: { role: 'Admin' },
  loadComponent: ...
}
```

This would prevent users from accessing pages directly via URL.

---

## ✅ Summary

**Your requirement:** "Author only can see submission and register"

**Status:** ✅ **IMPLEMENTED**

- Authors see exactly 2 menu items: Submission + Registration
- Reviewers see only Reviews
- Chairs see Conference + Schedule + Review Setup
- Admins see everything

The role-based access control is working correctly!

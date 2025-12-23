# Team Setup Guide - Conference Management System

## ✅ For New Team Members (First Time Setup)

When you clone this repository, follow these steps to get the full database with all data:

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd "TESTING BRANCH/backend"
```

### 2. Start Docker Containers
```bash
docker-compose up -d
```

**That's it!** 🎉 The database will automatically:
- Create all tables (users, conferences, papers, reviews, etc.)
- Insert sample data (test users, conferences, papers)
- Set up test accounts for login

### 3. Verify It Worked
```bash
# Check database has data
docker exec assignment-2-webcrafters-db-1 mysql -uroot -proot12345 cmsdb -e "SELECT COUNT(*) FROM user;"
```

You should see **10 users** in the database.

---

## 🔄 If You Need to Reset the Database

If your database gets corrupted or you want fresh data:

```bash
cd backend
docker-compose down -v   # ⚠️ -v flag deletes the data volume
docker-compose up -d     # Recreates everything from scratch
```

---

## 👤 Test Accounts Available

After initialization, you can login with these accounts:

| Email | Password | Role |
|-------|----------|------|
| admin@scholarsync.com | admin123 | Admin |
| author@scholarsync.com | author123 | Author |
| reviewer@scholarsync.com | reviewer123 | Reviewer |
| chair@scholarsync.com | chair123 | Chair |
| admin@cms.com | hash_admin | Admin (original) |

---

## 📊 What Data Gets Loaded

The auto-initialization includes:

1. **10 Users** - Mix of admins, authors, reviewers, chairs
2. **4 Roles** - Admin, Author, Reviewer, Chair
3. **1 Conference** - ICSC 2025 (Smart Computing)
4. **2 Papers** - Sample paper submissions
5. **Tracks, Reviews, Assignments** - Full workflow data

---

## 🛠 How It Works (For Developers)

MySQL Docker containers automatically run `.sql` files from `/docker-entrypoint-initdb.d/` on first startup:

1. `00-init.sql` - Documentation (no actual changes)
2. `01-cms_create.sql` - Creates all database tables
3. `02-cms_data.sql` - Inserts sample data
4. `03-insert_test_users.sql` - Adds test login accounts
5. `04-add_conference_id_to_paper.sql` - Schema migrations (idempotent)

**Important:** SQL files only run when the volume is created for the first time. If the volume exists, they are ignored.

---

## ➕ Adding Permanent Data

If you want to add data that everyone gets:

### Option 1: Edit existing data file
```bash
# Edit backend/sql/02-cms_data.sql
# Add your INSERT statements
# Commit and push to GitHub
```

### Option 2: Create a new numbered file
```bash
# Create backend/sql/05-my-custom-data.sql
# Add your SQL statements
# Files run in alphabetical order
# Commit and push to GitHub
```

**For team members to get the new data:**
```bash
docker-compose down -v
docker-compose up -d
```

---

## 🐛 Troubleshooting

### Database container won't start
```bash
# Check logs
docker-compose logs db

# Look for SQL errors
docker-compose logs db | grep ERROR
```

### Data not persisting
- ✅ Data persists between `docker-compose down` and `docker-compose up`
- ❌ Data is deleted with `docker-compose down -v` (the `-v` flag)
- ✅ This is intentional! Use `-v` to reset to fresh state

### SQL changes not appearing
```bash
# SQL files only run on FIRST container creation
# You must delete the volume to re-run them:
docker-compose down -v
docker-compose up -d
```

---

## 📁 File Structure

```
backend/
├── sql/
│   ├── README.md                        ← Detailed SQL documentation
│   ├── 00-init.sql                      ← Info file
│   ├── 01-cms_create.sql               ← Database schema (tables)
│   ├── 02-cms_data.sql                 ← Sample data
│   ├── 03-insert_test_users.sql        ← Test accounts
│   └── 04-add_conference_id_to_paper.sql ← Migrations
├── docker-compose.yml                   ← Database auto-init config
└── Dockerfile
```

---

## 🚀 Why This Matters

**Before:** Every team member had to:
1. Manually run SQL scripts
2. Insert test data themselves
3. Create user accounts
4. Set up conferences and papers

**After:** Team members just run:
```bash
docker-compose up -d
```

Everything is automatic! 🎯

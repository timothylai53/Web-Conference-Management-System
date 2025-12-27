# Database Auto-Initialization Setup

## How It Works

When you run `docker-compose up` for the **first time**, MySQL automatically executes all `.sql` files in the `sql/` folder in **alphabetical order**.

### Execution Order:
1. `00-init.sql` - Info file (documentation)
2. `01-cms_create.sql` - Creates all database tables
3. `02-cms_data.sql` - Inserts sample data (users, conferences, papers, etc.)
4. `03-insert_test_users.sql` - Adds additional test users
5. `04-add_conference_id_to_paper.sql` - Schema updates/migrations

## Fresh Start Instructions

If you want to reset the database and reload all data:

```bash
# Stop and remove containers + volumes
docker-compose down -v

# Start fresh (this will auto-run all SQL files)
docker-compose up -d
```

## Important Notes

- **SQL files are ONLY executed on first container creation**
- If the database volume already exists, SQL files are ignored
- Use `-v` flag with `docker-compose down` to delete the volume
- All team members get the same data when they first run the project

## For Team Members

1. Clone the repository
2. Navigate to `backend/` folder
3. Run: `docker-compose up -d`
4. Database will be automatically created with all data
5. No manual SQL execution needed!

## Checking If It Worked

```bash
# Connect to database
docker exec -it assignment-2-webcrafters-db-1 mysql -uroot -proot12345 cmsdb

# Check tables
SHOW TABLES;

# Check sample data
SELECT * FROM user;
SELECT * FROM conference;
```

## Adding New Data

To add permanent data that all team members get:
1. Add SQL statements to `02-cms_data.sql` or create a new numbered file
2. Commit and push to GitHub
3. Team members must run `docker-compose down -v` then `docker-compose up -d` to get the new data

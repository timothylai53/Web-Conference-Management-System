[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/j93TiVAg)
## Requirements for Assignment-3
[Read the instruction](https://github.com/STIWK2124-A251/class-activity-stiwk2124/blob/main/Assignment-3.md)

## Group Info:
1. Matric Number & Name & Photo & Phone Number
1. Mention who the leader is.
1. Mention your group name for Assignment-1 and Assignment-2
1. Other related info (if any)

| Photo | Name                            | Matric Number | Phone Number | Previous Group |
| --------------- |---------------------------------|--------|------------| ------------- |
| <img src="https://github.com/user-attachments/assets/46dbd9db-a50f-4769-86a0-e2fab9831dbf" width="120" height="170"> | ADTHIE CHARD A/L BOON           | 299967 | 01131810149 | Webboys |
| <img src="https://github.com/user-attachments/assets/47ff88ef-b27e-4b84-9207-48331fa57235" width="120" height="170"/> | GEE SIN ROU                     | 298556 | 0142693893 | hi5 |
| <img src="https://github.com/user-attachments/assets/f8f4179d-f8bc-4b03-afd6-10a34f5ee133" width="120" height="170"/> | NUR ALIA NAJWA BINTI JASMI                 | 298689 | 0128700384        | syntaxerror |
| <img src="https://github.com/user-attachments/assets/379d4607-3ddc-4644-8efb-ec917c5d2e70" width="120" height="170"/> | TIMOTHY LAI JUN HONG (Leader)   | 297880 | 0136483581 | 405 |
| <img src="https://github.com/user-attachments/assets/16101139-c497-415f-9be2-7dfd4a26e045" width="120" height="170"> | FAYADH MUHAMMAD                 | 297032 | 0143287087 | BiscuIT |
| <img src="https://github.com/user-attachments/assets/c9d6aca2-20a3-480e-892b-c3634cae0b41" width="120" height="170"> | WAN NUR AZIMAH BINTI WAN RAHMAT | 297976   | 01160724726        | webcrafters |
| <img src="https://github.com/user-attachments/assets/ed9595c8-5f6a-44d6-a637-f7200b5ffe94" width="120" height="170"> | MUHD AFIF AFIFI BIN MUSA        | 299783   | 0163878419        | budaksurau |
| <img src="https://github.com/user-attachments/assets/6b063b37-8a92-4987-b879-157bc2f03a51" width="120" height="170"> | LOH JIA PING                    | 297839   | 01169503383        | g2 |


 <img width="876" height="864" alt="image" src="" />
 

## Title of your application (a unique title)

#### ScholarSync


## Abstract (in 300 words)
   1. Background
      
      Conference management platforms are widely used in academic and professional communities to handle submissions, review processes, event scheduling, and participant coordination. Systems like EasyChair and         ConfTool automate these tasks, simplifying administrative workloads. This project adopts the core ideas of such platforms and implements a simplified yet functional version using modern software engineering       tools.
      
   2. Problem Statement (from article)
      
      Managing academic conferences manually leads to inefficiencies such as lost submissions, poor tracking of participant data, and delays in review processes. Existing platforms are often complex and require         high learning curves. Therefore, there is a need for a simplified, modern, web-based conference management system with an intuitive interface that supports essential functions such as user registration,           submission handling, and event management.
      
   3. Main objective
      
      To develop a cloud-deployed, full-stack Conference Management System integrating Angular, Spring Boot, MySQL, and Docker, with complete CRUD functionality and component-based architecture.
      
   4. Methodology
      
      Requirement Analysis – Identified core features (user management, conference events, paper submission).

      Database Design – MySQL schema created with normalized tables.

      Backend Development – Developed Spring Boot REST APIs for CRUD operations using JPA and MySQL.

      Frontend Development – Built Angular components, services, routing, and Bootstrap styling.

      Integration – Connected frontend to backend APIs using Angular HttpClient.

      Containerization – Created Dockerfiles for frontend and backend; used Docker Compose for a multi-container environment.

      Cloud Deployment – Uploaded Docker images to Docker Hub and deployed via a cloud provider.

      Documentation – Created README, references, and video demonstration.
      
   5. Result
       
      Successful CRUD operations for all conference-related entities.
      Angular interface fully connected to backend APIs.
      Functional routing across components (Dashboard, Users, Conferences, Submissions).
      All applications containerized and deployed on cloud.
      Repository available on GitHub with complete source code and documentation.
      
   6. Conclusion
       
      This project demonstrates the successful development and deployment of a full-stack Conference Management System using Angular, Spring Boot, and MySQL, packaged with Docker and hosted on cloud                     infrastructure. It highlights the importance of component-based design, API integration, and containerization for modern scalable applications.

## Link for Docker Image

The latest verified Docker images for this project are hosted on Docker Hub:

Frontend Image:
https://hub.docker.com/r/sinrou0410/confease-frontend

Backend Image:
https://hub.docker.com/r/sinrou0410/confease-backend

**To run a specific version (e.g., v1.0.0):**
```bash
docker pull sinrou0410/confease-frontend:v1.0.0
docker pull sinrou0410/confease-backend:v1.0.0
```
   
## Instructions on how to run Docker.
Step 1: Pull Docker Images
```bash
docker pull sinrou0410/confease-frontend
docker pull sinrou0410/confease-backend
```

Step 2: Run Backend Container
```bash
docker run -d \
  --name confease-backend \
  -p 8080:8080 \
  sinrou0410/confease-backend
```
Backend will be available at:
http://localhost:8080

Step 3: Run Frontend Container
```bash
docker run -d \
  --name confease-frontend \
  -p 4200:80 \
  sinrou0410/confease-frontend
  ```
Frontend will be available at:
http://localhost:4200

Step 4: Verify Running Containers
```bash
docker ps
```
Stop Containers
```bash
docker stop confease-backend confease-frontend
```
Remove Containers
Stop Containers
```bash
docker rm confease-backend confease-frontend
```

## List of all the endpoints

### 1. Auth

| Operation        | HTTP Method | Endpoint        | URL                               | Description                           |
|-----------------|------------|----------------|----------------------------------|---------------------------------------|
| Login           | POST       | /auth/login    | http://localhost:8080/auth/login  | Authenticate a user and return user info (without password) |
| Register        | POST       | /auth/register | http://localhost:8080/auth/register | Register a new user and assign default "Author" role         |

### 2. Author

| Operation                  | HTTP Method | Endpoint            | URL                                   | Description                                  |
|----------------------------|------------|-------------------|--------------------------------------|----------------------------------------------|
| Get All Authors            | GET        | /author           | http://localhost:8080/author         | Retrieve all authors                          |
| Get Author by ID           | GET        | /author/{id}      | http://localhost:8080/author/{id}    | Retrieve a single author by ID               |
| Get Authors by Paper ID    | GET        | /author/paper/{paperId} | http://localhost:8080/author/paper/{paperId} | Retrieve all authors for a specific paper    |
| Create Author              | POST       | /author           | http://localhost:8080/author         | Add a new author                              |
| Update Author              | PUT        | /author/{id}      | http://localhost:8080/author/{id}    | Update an existing author                     |
| Delete Author              | DELETE     | /author/{id}      | http://localhost:8080/author/{id}    | Delete an author by ID                        |

### 3. User
| Operation       | HTTP Method | Endpoint     | URL                                | Description            |
|-----------------|------------|-------------|------------------------------------|------------------------|
| Create User     | POST       | /users      | http://localhost:8080/users        | Add a new user         |
| Get All Users   | GET        | /users      | http://localhost:8080/users        | Retrieve all users     |
| Get User by ID  | GET        | /users/{id} | http://localhost:8080/users/{id}   | Retrieve a user by ID  |
| Update User     | PUT        | /users/{id} | http://localhost:8080/users/{id}   | Update a user          |
| Delete User     | DELETE     | /users/{id} | http://localhost:8080/users/{id}   | Delete a user          |

### 4. User Roles
| Operation           | HTTP Method | Endpoint           | URL                                    | Description                    |
|--------------------|------------|------------------|----------------------------------------|--------------------------------|
| Create User Role    | POST       | /user-roles      | http://localhost:8080/user-roles       | Add a new user role            |
| Get All User Roles  | GET        | /user-roles      | http://localhost:8080/user-roles       | Retrieve all user roles        |
| Get User Role by ID | GET        | /user-roles/{id} | http://localhost:8080/user-roles/{id}  | Retrieve a user role by ID     |
| Update User Role    | PUT        | /user-roles/{id} | http://localhost:8080/user-roles/{id}  | Update an existing user role   |
| Delete User Role    | DELETE     | /user-roles/{id} | http://localhost:8080/user-roles/{id}  | Delete a user role by ID       |

###  5. Conference
| Operation            | HTTP Method | Endpoint           | URL                                 | Description                            |
|---------------------|------------|------------------|------------------------------------|----------------------------------------|
| Create Conference    | POST       | /conferences     | http://localhost:8080/conferences   | Add a new conference                   |
| Get All Conferences  | GET        | /conferences     | http://localhost:8080/conferences   | Retrieve all conferences               |
| Get Conference by ID | GET        | /conferences/{id}| http://localhost:8080/conferences/{id}| Retrieve a single conference by its ID |
| Update Conference    | PUT        | /conferences/{id}| http://localhost:8080/conferences/{id}| Update an existing conference          |
| Delete Conference    | DELETE     | /conferences/{id}| http://localhost:8080/conferences/{id}| Delete a conference by its ID          |


### 6. Paper
| Operation        | HTTP Method | Endpoint        | URL                               | Description                     |
|-----------------|------------|----------------|----------------------------------|---------------------------------|
| Create Paper     | POST       | /papers        | http://localhost:8080/papers      | Add a new paper                 |
| Get All Paper    | GET        | /papers        | http://localhost:8080/papers      | Retrieve all paper              |
| Get Paper by ID  | GET        | /papers/{id}   | http://localhost:8080/papers/{id} | Retrieve a single paper by ID   |
| Update Paper     | PUT        | /papers/{id}   | http://localhost:8080/papers/{id} | Update existing paper           |
| Delete Paper     | DELETE     | /papers/{id}   | http://localhost:8080/papers/{id} | Delete a paper by ID            |


### 7. Notification
| Operation               | HTTP Method | Endpoint           | URL                                 | Description                          |
|-------------------------|------------|------------------|------------------------------------|--------------------------------------|
| Create Notification     | POST       | /notifications   | http://localhost:8080/notifications | Add a new notification               |
| Get All Notification    | GET        | /notifications   | http://localhost:8080/notifications | Retrieve all notification            |
| Get Notification by ID  | GET        | /notifications/{id} | http://localhost:8080/notifications/{id} | Retrieve a single notification by ID |
| Update Notification     | PUT        | /notifications/{id} | http://localhost:8080/notifications/{id} | Update existing notification         |
| Delete Notification     | DELETE     | /notifications/{id} | http://localhost:8080/notifications/{id} | Delete a notification by ID          |


### 8. Schedule
| Operation           | HTTP Method | Endpoint          | URL                                | Description                     |
|--------------------|------------|-----------------|-----------------------------------|---------------------------------|
| Create Schedule     | POST       | /schedule       | http://localhost:8080/schedule    | Add a new schedule              |
| Get All Schedules   | GET        | /schedule       | http://localhost:8080/schedule    | Retrieve all schedules          |
| Get Schedule by ID  | GET        | /schedule/{id}  | http://localhost:8080/schedule/{id}| Retrieve a single schedule by ID|
| Update Schedule     | PUT        | /schedule/{id}  | http://localhost:8080/schedule/{id}| Update existing schedule        |
| Delete Schedule     | DELETE     | /schedule/{id}  | http://localhost:8080/schedule/{id}| Delete a schedule by ID         |

### 9. Session
| Operation         | HTTP Method | Endpoint         | URL                               | Description                 |
|------------------|------------|----------------|----------------------------------|-----------------------------|
| Create Session    | POST       | /sessions      | http://localhost:8080/sessions    | Add a new session           |
| Get All Sessions  | GET        | /sessions      | http://localhost:8080/sessions    | Retrieve all sessions       |
| Get Session by ID | GET        | /sessions/{id} | http://localhost:8080/sessions/{id}| Retrieve a session by ID    |
| Update Session    | PUT        | /sessions/{id} | http://localhost:8080/sessions/{id}| Update a session            |
| Delete Session    | DELETE     | /sessions/{id} | http://localhost:8080/sessions/{id}| Delete a session            |

### 10. Discussion
| Operation           | HTTP Method | Endpoint         | URL                              | Description                   |
|--------------------|------------|----------------|---------------------------------|-------------------------------|
| Create Discussion   | POST       | /discussion    | http://localhost:8080/discussion | Add a new discussion          |
| Get All Discussions | GET        | /discussion    | http://localhost:8080/discussion | Retrieve all discussions      |
| Get Discussion by ID| GET        | /discussion/{id}| http://localhost:8080/discussion/{id} | Retrieve a discussion by ID  |
| Update Discussion   | PUT        | /discussion/{id}| http://localhost:8080/discussion/{id} | Update an existing discussion |
| Delete Discussion   | DELETE     | /discussion/{id}| http://localhost:8080/discussion/{id} | Delete a discussion by ID     |

### 11. Room
| Operation       | HTTP Method | Endpoint     | URL                                | Description                    |
|-----------------|------------|-------------|------------------------------------|--------------------------------|
| Create Room     | POST       | /rooms      | http://localhost:8080/rooms        | Add a new room                 |
| Get All Rooms   | GET        | /rooms      | http://localhost:8080/rooms        | Retrieve all rooms             |
| Get Room by ID  | GET        | /rooms/{id} | http://localhost:8080/rooms/{id}   | Retrieve a room by ID          |
| Update Room     | PUT        | /rooms/{id} | http://localhost:8080/rooms/{id}   | Update an existing room        |
| Delete Room     | DELETE     | /rooms/{id} | http://localhost:8080/rooms/{id}   | Delete a room by ID            |

### 12. Email Logs

| Operation            | HTTP Method | Endpoint          | URL                                  | Description                     |
|---------------------|------------|-----------------|-------------------------------------|---------------------------------|
| Create Email Log     | POST       | /email-logs     | http://localhost:8080/email-logs    | Add a new email log             |
| Get All Email Logs   | GET        | /email-logs     | http://localhost:8080/email-logs    | Retrieve all email logs         |
| Get Email Log by ID  | GET        | /email-logs/{id}| http://localhost:8080/email-logs/{id}| Retrieve a single email log by ID |
| Update Email Log     | PUT        | /email-logs/{id}| http://localhost:8080/email-logs/{id}| Update an existing email log    |
| Delete Email Log     | DELETE     | /email-logs/{id}| http://localhost:8080/email-logs/{id}| Delete an email log by ID       |

### 13. Events

| Operation        | HTTP Method | Endpoint       | URL                                  | Description                     |
|-----------------|------------|---------------|-------------------------------------|---------------------------------|
| Create Event     | POST       | /events       | http://localhost:8080/events        | Add a new event                 |
| Get All Events   | GET        | /events       | http://localhost:8080/events        | Retrieve all events             |
| Get Event by ID  | GET        | /events/{id}  | http://localhost:8080/events/{id}   | Retrieve a single event by ID   |
| Update Event     | PUT        | /events/{id}  | http://localhost:8080/events/{id}   | Update an existing event        |
| Delete Event     | DELETE     | /events/{id}  | http://localhost:8080/events/{id}   | Delete an event by ID           |

### 14. Files

| Operation      | HTTP Method | Endpoint         | URL                                 | Description                     |
|---------------|------------|-----------------|------------------------------------|---------------------------------|
| Upload File    | POST       | /files/upload   | http://localhost:8080/files/upload | Upload a file (PDF, DOC, etc.) |
| Serve File     | GET        | /files/{filename}| http://localhost:8080/files/{filename} | Serve/download a file by filename |
| Delete File    | DELETE     | /files/{filename}| http://localhost:8080/files/{filename} | Delete a file                  |

### 15. Keywords

| Operation         | HTTP Method | Endpoint       | URL                                  | Description                  |
|------------------|------------|---------------|-------------------------------------|------------------------------|
| Create Keyword    | POST       | /keywords     | http://localhost:8080/keywords      | Add a new keyword            |
| Get All Keywords  | GET        | /keywords     | http://localhost:8080/keywords      | Retrieve all keywords        |
| Get Keyword by ID | GET        | /keywords/{id}| http://localhost:8080/keywords/{id} | Retrieve a single keyword by ID |
| Update Keyword    | PUT        | /keywords/{id}| http://localhost:8080/keywords/{id} | Update an existing keyword   |
| Delete Keyword    | DELETE     | /keywords/{id}| http://localhost:8080/keywords/{id} | Delete a keyword by ID       |

### 16. Log Activities

| Operation             | HTTP Method | Endpoint            | URL                                         | Description                     |
|----------------------|------------|-------------------|--------------------------------------------|---------------------------------|
| Create Log Activity   | POST       | /log-activities    | http://localhost:8080/log-activities       | Add a new log activity          |
| Get All Log Activities| GET        | /log-activities    | http://localhost:8080/log-activities       | Retrieve all log activities     |
| Get Log Activity by ID| GET        | /log-activities/{id}| http://localhost:8080/log-activities/{id} | Retrieve a single log activity by ID |
| Update Log Activity   | PUT        | /log-activities/{id}| http://localhost:8080/log-activities/{id} | Update an existing log activity |
| Delete Log Activity   | DELETE     | /log-activities/{id}| http://localhost:8080/log-activities/{id} | Delete a log activity by ID     |

### 17. Paper Keywords

| Operation               | HTTP Method | Endpoint                   | URL                                            | Description                           |
|------------------------|------------|----------------------------|------------------------------------------------|---------------------------------------|
| Create Paper Keyword    | POST       | /paper-keywords            | http://localhost:8080/paper-keywords          | Add a new paper keyword               |
| Get All Paper Keywords  | GET        | /paper-keywords            | http://localhost:8080/paper-keywords          | Retrieve all paper keywords           |
| Get Paper Keyword by ID | GET        | /paper-keywords/{paperId}/{keywordId} | http://localhost:8080/paper-keywords/{paperId}/{keywordId} | Retrieve a single paper keyword by composite ID |
| Update Paper Keyword    | PUT        | /paper-keywords/{paperId}/{keywordId} | http://localhost:8080/paper-keywords/{paperId}/{keywordId} | Update an existing paper keyword      |
| Delete Paper Keyword    | DELETE     | /paper-keywords/{paperId}/{keywordId} | http://localhost:8080/paper-keywords/{paperId}/{keywordId} | Delete a paper keyword by composite ID |

### 18. Payments

| Operation           | HTTP Method | Endpoint            | URL                                      | Description                     |
|--------------------|------------|-------------------|-----------------------------------------|---------------------------------|
| Create Payment      | POST       | /payments         | http://localhost:8080/payments          | Add a new payment               |
| Get All Payments    | GET        | /payments         | http://localhost:8080/payments          | Retrieve all payments           |
| Get Payment by ID   | GET        | /payments/{id}    | http://localhost:8080/payments/{id}     | Retrieve a single payment by ID |
| Update Payment      | PUT        | /payments/{id}    | http://localhost:8080/payments/{id}     | Update an existing payment      |
| Delete Payment      | DELETE     | /payments/{id}    | http://localhost:8080/payments/{id}     | Delete a payment by ID          |

### 19. Rebuttals

| Operation           | HTTP Method | Endpoint          | URL                                      | Description                     |
|--------------------|------------|-----------------|-----------------------------------------|---------------------------------|
| Create Rebuttal     | POST       | /rebuttals      | http://localhost:8080/rebuttals         | Add a new rebuttal              |
| Get All Rebuttals   | GET        | /rebuttals      | http://localhost:8080/rebuttals         | Retrieve all rebuttals          |
| Get Rebuttal by ID  | GET        | /rebuttals/{id} | http://localhost:8080/rebuttals/{id}    | Retrieve a single rebuttal by ID|
| Update Rebuttal     | PUT        | /rebuttals/{id} | http://localhost:8080/rebuttals/{id}    | Update an existing rebuttal     |
| Delete Rebuttal     | DELETE     | /rebuttals/{id} | http://localhost:8080/rebuttals/{id}    | Delete a rebuttal by ID         |

### 20. Registrations

| Operation                     | HTTP Method | Endpoint              | URL                                              | Description                          |
|-------------------------------|------------|----------------------|-------------------------------------------------|--------------------------------------|
| Create Registration           | POST       | /registrations       | http://localhost:8080/registrations            | Add a new registration               |
| Get All Registrations         | GET        | /registrations       | http://localhost:8080/registrations            | Retrieve all registrations           |
| Get Registration by ID        | GET        | /registrations/{id}  | http://localhost:8080/registrations/{id}       | Retrieve a single registration by ID|
| Get Registrations by User ID  | GET        | /registrations/user/{userId} | http://localhost:8080/registrations/user/{userId} | Retrieve registrations for a specific user |
| Update Registration           | PUT        | /registrations/{id}  | http://localhost:8080/registrations/{id}       | Update an existing registration      |
| Delete Registration           | DELETE     | /registrations/{id}  | http://localhost:8080/registrations/{id}       | Delete a registration by ID          |


### 21. Reviews

| Operation       | HTTP Method | Endpoint     | URL                                | Description                                                                 |
|-----------------|------------|-------------|------------------------------------|-----------------------------------------------------------------------------|
| Create Review   | POST       | /reviews    | http://localhost:8080/reviews      | Add a new review; updates assignment and paper status if applicable        |
| Get All Reviews | GET        | /reviews    | http://localhost:8080/reviews      | Retrieve all reviews                                                        |
| Get Review by ID| GET        | /reviews/{id}| http://localhost:8080/reviews/{id} | Retrieve a review by ID                                                     |
| Update Review   | PUT        | /reviews/{id}| http://localhost:8080/reviews/{id} | Update an existing review                                                   |
| Delete Review   | DELETE     | /reviews/{id}| http://localhost:8080/reviews/{id} | Delete a review by ID                                                       |

### 22. Review Assignments

| Operation                        | HTTP Method | Endpoint                     | URL                                              | Description                                 |
|----------------------------------|------------|------------------------------|-------------------------------------------------|---------------------------------------------|
| Create Review Assignment          | POST       | /review-assignments          | http://localhost:8080/review-assignments       | Add a new review assignment                |
| Get All Review Assignments        | GET        | /review-assignments          | http://localhost:8080/review-assignments       | Retrieve all review assignments            |
| Get Review Assignment by ID       | GET        | /review-assignments/{id}     | http://localhost:8080/review-assignments/{id}  | Retrieve a review assignment by ID         |
| Get Review Assignments by Reviewer| GET        | /review-assignments/reviewer/{reviewerId} | http://localhost:8080/review-assignments/reviewer/{reviewerId} | Retrieve assignments for a specific reviewer |
| Update Review Assignment          | PUT        | /review-assignments/{id}     | http://localhost:8080/review-assignments/{id}  | Update an existing review assignment       |
| Delete Review Assignment          | DELETE     | /review-assignments/{id}     | http://localhost:8080/review-assignments/{id}  | Delete a review assignment by ID           |

### 23. Reviewer Applications

| Operation                        | HTTP Method | Endpoint                         | URL                                              | Description                                 |
|----------------------------------|------------|----------------------------------|-------------------------------------------------|---------------------------------------------|
| Submit Reviewer Application      | POST       | /reviewer-applications/submit    | http://localhost:8080/reviewer-applications/submit | Submit a new reviewer application          |
| Get All Pending Applications     | GET        | /reviewer-applications/pending   | http://localhost:8080/reviewer-applications/pending | Retrieve all pending applications (Admin only) |
| Get All Applications             | GET        | /reviewer-applications/all       | http://localhost:8080/reviewer-applications/all | Retrieve all applications (Admin only)     |
| Approve Application              | POST       | /reviewer-applications/approve/{id} | http://localhost:8080/reviewer-applications/approve/{id}?adminUserId={adminUserId} | Approve a pending application and create user account |
| Reject Application               | POST       | /reviewer-applications/reject/{id} | http://localhost:8080/reviewer-applications/reject/{id}?adminUserId={adminUserId} | Reject a pending application with reason   |

### 24. Review Scores

| Operation                        | HTTP Method | Endpoint                     | URL                                              | Description                                 |
|----------------------------------|------------|------------------------------|-------------------------------------------------|---------------------------------------------|
| Get All Review Scores            | GET        | /review-scores               | http://localhost:8080/review-scores            | Retrieve all review scores                  |
| Get Review Score by ID           | GET        | /review-scores/{reviewId}/{criterionId} | http://localhost:8080/review-scores/{reviewId}/{criterionId} | Retrieve a review score by composite ID (review and criterion) |
| Create Review Score              | POST       | /review-scores               | http://localhost:8080/review-scores            | Create a new review score                   |
| Update Review Score              | PUT        | /review-scores/{reviewId}/{criterionId} | http://localhost:8080/review-scores/{reviewId}/{criterionId} | Update an existing review score            |
| Delete Review Score              | DELETE     | /review-scores/{reviewId}/{criterionId} | http://localhost:8080/review-scores/{reviewId}/{criterionId} | Delete a review score by composite ID      |

### 25. Score Criteria

| Operation                   | HTTP Method | Endpoint            | URL                                      | Description                          |
|------------------------------|------------|-------------------|-----------------------------------------|--------------------------------------|
| Get All Score Criteria       | GET        | /score-criteria   | http://localhost:8080/score-criteria    | Retrieve all score criteria          |
| Get Score Criterion by ID    | GET        | /score-criteria/{id} | http://localhost:8080/score-criteria/{id} | Retrieve a score criterion by ID     |
| Create Score Criterion       | POST       | /score-criteria   | http://localhost:8080/score-criteria    | Create a new score criterion         |
| Update Score Criterion       | PUT        | /score-criteria/{id} | http://localhost:8080/score-criteria/{id} | Update an existing score criterion   |
| Delete Score Criterion       | DELETE     | /score-criteria/{id} | http://localhost:8080/score-criteria/{id} | Delete a score criterion by ID       |

### 26. Roles

| Operation        | HTTP Method | Endpoint   | URL                           | Description                  |
|-----------------|------------|------------|-------------------------------|------------------------------|
| Get All Roles    | GET        | /roles     | http://localhost:8080/roles   | Retrieve all roles           |
| Get Role by ID   | GET        | /roles/{id} | http://localhost:8080/roles/{id} | Retrieve a role by ID       |
| Create Role      | POST       | /roles     | http://localhost:8080/roles   | Create a new role            |
| Update Role      | PUT        | /roles/{id} | http://localhost:8080/roles/{id} | Update an existing role     |
| Delete Role      | DELETE     | /roles/{id} | http://localhost:8080/roles/{id} | Delete a role by ID         |

### 27. Tracks

| Operation           | HTTP Method | Endpoint      | URL                              | Description                       |
|--------------------|------------|---------------|---------------------------------|-----------------------------------|
| Get All Tracks      | GET        | /tracks       | http://localhost:8080/tracks    | Retrieve all tracks               |
| Get Track by ID     | GET        | /tracks/{id}  | http://localhost:8080/tracks/{id} | Retrieve a track by ID            |
| Create Track        | POST       | /tracks       | http://localhost:8080/tracks    | Create a new track                |
| Update Track        | PUT        | /tracks/{id}  | http://localhost:8080/tracks/{id} | Update an existing track          |
| Delete Track        | DELETE     | /tracks/{id}  | http://localhost:8080/tracks/{id} | Delete a track by ID              |



## Result/Output (Screenshot of the output)


| No | Screenshot | Description |
|----|------------|-------------|
| 1  | <img width="700" src="https://github.com/user-attachments/assets/bdf753ec-7b6f-477d-81ce-3db0749d0258" /> |  Auth-user-profile(sign in)        |
| 2  | <img width="700" src="https://github.com/user-attachments/assets/fa105c47-d569-4886-bdb6-9761b35a4e12" /> |  Auth-user-profile(create account) |
| 3  | <img width="700" src="https://github.com/user-attachments/assets/28c0f7f0-bde3-4d84-b088-66890dcd4720"/>  |  Auth-user-profile(view & update profile)        |
| 4  | <img width="700" src="https://github.com/user-attachments/assets/7358feeb-dc30-4124-9e85-8573ad054e11" /> |  Admin-dashboard                       |
| 5  | <img width="700" src="https://github.com/user-attachments/assets/c8d456bf-df1e-431e-bca5-bbfdd6235967" /> |  Conference(Main Page)                 |
| 6  | <img width="700" src="https://github.com/user-attachments/assets/9647b863-e7cb-4519-80e1-9a869a53e9fc" /> |  Conference(Explore events)        |
| 7  | <img width="700" src="https://github.com/user-attachments/assets/e027db1f-60d2-4e62-b6de-895c2fa84f69" /> |  Conference Information (Explore events)  |
| 8  | <img width="700" src="https://github.com/user-attachments/assets/619bc99b-eaff-48ae-b86c-61b45ad27b75" /> |  Conference(Edit Conference)  |
| 9  | <img width="700"  src="https://github.com/user-attachments/assets/53d7a3e3-df61-4746-9745-0d49ac4f6fa6"/> |   Conference(Create New Conference)   | 
| 10 | <img width="700" src="https://github.com/user-attachments/assets/e23cfbc9-ca3a-4230-b523-84d567a412e2"/>  |  Conference(View Tracks)           |
| 11 | <img width="700" src="https://github.com/user-attachments/assets/241181ba-3423-4bf2-b10c-d285d73566f6"/>  |  Conference(Edit Tracks)           |
| 12 | <img width="700" src="https://github.com/user-attachments/assets/d680f77e-e2b1-400e-a39a-5e200aa2ab3e"/>  |  Conference (Create New Tracks)    |    
| 13 | <img width="700" src="https://github.com/user-attachments/assets/ae8ae058-54f2-4234-b8da-db23beba841e"/>  |  Conference Registration           |
| 14 | <img width="700" src="https://github.com/user-attachments/assets/a35ede1a-2624-45ac-8fdc-ad19b2ff688a"/>  |  Conference Registration (Payment) |
| 15 | <img width="700" src="https://github.com/user-attachments/assets/dbc4e898-071f-428c-a713-06895671cec6" /> |  Paper-submission (main page)          |
| 16 | <img width="700" src="https://github.com/user-attachments/assets/553694c4-d8fb-4bc4-a462-1d4306a42291" /> |  Paper-submission (view paper details) |
| 17 | <img width="700" src="https://github.com/user-attachments/assets/508f8738-a8cc-46ca-be3e-74a4c6703295" /> |  Paper-submission (submit new paper)   |
| 18 | <img width="700" src="https://github.com/user-attachments/assets/967d474b-001d-40ad-bbb9-d0893a476427" /> |  Paper-submission (submit new paper-Author details)   |
| 19 | <img width="700" src="https://github.com/user-attachments/assets/3a9c73d7-9350-4b95-8a93-ea601acc49db" /> |  Paper-submission (submit new paper-upload paper)   |
| 20 | <img width="700" src="https://github.com/user-attachments/assets/fca7855a-397a-4b86-9c8d-b9d233597fae" /> |  Paper-submission (submit new paper-add keywords)   |
| 21 | <img width="700" src="https://github.com/user-attachments/assets/0b1d27de-03d3-42da-a745-dff78f2b5899" /> |  Conference Schedule  |
| 22 | <img width="700" src="https://github.com/user-attachments/assets/7bfae0e6-f9a3-4a22-8620-fe82e6b3d0b7" /> |   Session Management  |
| 23 | <img width="700" src="https://github.com/user-attachments/assets/325337d1-893c-4818-ad56-deab0f706fd3" /> |     Room Management                      |
| 24 | <img width="700" src="https://github.com/user-attachments/assets/cd855bce-5a24-458a-b350-d886b37e3cde" /> |   Reviewer Dashboard(Before accepting) |
| 25 | <img width="700" src="https://github.com/user-attachments/assets/640b159f-efa0-44e3-bb99-6d3ef6f22a8e" /> |   Review Paper |
| 26 | <img width="700" src="https://github.com/user-attachments/assets/a903ad1d-cfd7-4598-9bff-4a8e101fe1eb" /> |    Reviewer Dashboard(Accepted)    |
| 27 | <img width="700" src="https://github.com/user-attachments/assets/9c8a67d0-ec05-40e7-a4bd-04ec33130975" /> |  Review setup                          |


Capture full size screenshot

## CI/CD Infrastructure (Jenkins)
Our project implements a professional **CI/CD Pipeline** that automates the software lifecycle from the moment code is pushed to the `testing` branch.

### 1. Pipeline Stages (Build #93)


<img width="1858" height="944" alt="image" src="https://github.com/user-attachments/assets/6f827891-8de3-49fb-bc6f-b4e5d187fc85" />


| Stage              | Action                                            | Result    |
| ------------------ | ------------------------------------------------- | --------- |
| Checkout SCM       | Pulls the latest code from GitHub.                | ✅ Success |
| Cleanup            | Wipes old Docker containers and volumes.          | ✅ Success |
| Build Backend      | Packages Spring Boot into a production-ready JAR. | ✅ Success |
| Deploy             | Spins up the full stack for testing.              | ✅ Success |
| Test               | Runs Playwright E2E automation scripts.           | ✅ Success |
| Push to Docker Hub | Tags and pushes images (v1.0.0 & build-93).       | ✅ Success |

### 2. Automated E2E Testing (Playwright + Allure)

We prioritize system-level reliability. Instead of basic unit tests, we use Playwright to simulate real user journeys.

| Metric           | Details                                           |
|-----------------|--------------------------------------------------|
| Total Test Cases | 4 (Authentication & Paper Submission flow)      |
| Pass Rate        | 100% ✅                                           |
| Tooling          | Integrated with Allure Report for visual trend analysis and debugging |


<img width="1872" height="946" alt="image" src="https://github.com/user-attachments/assets/2bcc6418-814f-4b76-b54d-4af521ba81dc" />


### 3. Docker Registry & Versioning
After passing all tests, the images are pushed to our Docker Hub repository with semantic versioning and build-specific tags for full traceability.

**Repository:** 
- Frontend: `sinrou0410/confease-frontend`  
- Backend: `sinrou0410/confease-backend`

  
<img width="1896" height="740" alt="image" src="https://github.com/user-attachments/assets/ddea21a7-f3ad-4845-8c11-b16899fe46c8" />


**Strategy:** Dual-tagging with `latest` and `v1.0.0` (or `build-93`) for full traceability.


**How to pull our latest verified build:**

```bash
docker pull sinrou0410/confease-frontend:v1.0.0
docker pull sinrou0410/confease-backend:v1.0.0
```

## References (Not less than 20)
Assad, M., Meiklejohn, C. S., Miller, H., & Krusche, S. (2024). Can my microservice tolerate an unreliable database? Resilience testing with fault injection and visualization. Proceedings of the 2024 IEEE/ACM 46th International Conference on Software Engineering, 54–58. https://doi.org/10.1145/3639478.3640030

Biel, B., Book, M., Gruhn, V., Peters, D., & Schäfer, C. (2004). Handling backtracking in web applications: A case study of a conference management system. Proceedings of the 30th EUROMICRO Conference, 388–395. https://doi.org/10.1109/EURMIC.2004.1333394

Camilli, M., Guerriero, A., Janes, A., Russo, B., & Russo, S. (2022). Microservices integrated performance and reliability testing. Proceedings of the 3rd ACM/IEEE International Conference on Automation of Software Test, 29–39. https://doi.org/10.1145/3524481.3527233

Di Francesco, P., Lago, P., & Malavolta, I. (2019). Architecting microservices: A systematic mapping study. Journal of Systems and Software, 150, 77–97. https://doi.org/10.1016/j.jss.2019.01.001

Dragoni, N., et al. (2017). Microservices: Yesterday, today, and tomorrow. Present and Ulterior Software Engineering, 195–216. https://doi.org/10.1007/978-3-319-67425-4_12

Eismann, S., et al. (2020). A review of serverless and microservices research. Proceedings of the ACM/SPEC International Conference on Performance Engineering, 165–176. https://doi.org/10.1145/3358960.3379123

Grohmann, J., et al. (2021). Suanming: Explainable prediction of performance degradations in microservice applications. Proceedings of the ACM/SPEC International Conference on Performance Engineering, 165–176. https://doi.org/10.1145/3427921.3450239 

Hatma Suryotrisongko, Jayanto, D. P., & Tjahyanto, A. (2017). Design and development of backend application for public complaint systems using microservice Spring Boot. Procedia Computer Science, 124, 736–743. https://doi.org/10.1016/j.procs.2017.12.212 

Kozak, M. (2023). Analysis of the Spring Boot and Spring Cloud in developing Java cloud applications. Journal of Computer Sciences Institute, 27, 112–120. https://doi.org/10.35784/jcsi.3130

Lafuente, A. L. (2016). Multi-party choreography propagation in RESTful services. Proceedings of the 31st Annual ACM Symposium on Applied Computing, 763–770. https://doi.org/10.1145/2851613.2851619

Memon, K. A., & Xiaoling, X. (2019). Deciphering and analyzing software requirements employing the techniques of natural language processing. ACM International Conference Proceeding Series, 153–156. https://doi.org/10.1145/3325730.3325757

Pahl, C. (2015). Containerization and the PaaS Cloud. IEEE Cloud Computing, 2(3), 24–31. https://doi.org/10.1109/MCC.2015.51

Puranik, B. S., et al. (2025). Containerization in web development: Docker and Kubernetes. Proceedings of the 3rd International Conference on Futuristic Technology, 163–173. https://doi.org/10.5220/0013588700004664

Qu, C., Calheiros, R. N., & Buyya, R. (2018). Auto-scaling web applications in clouds: A taxonomy and survey. ACM Computing Surveys, 51(4), 1–33. https://doi.org/10.1145/3148149

Rodzik, P. (2023). Comparative analysis of selected programming frameworks in terms of their suitability for user authentication and authorization. Journal of Computer Sciences Institute, 27, 104–111. https://doi.org/10.35784/jcsi.3129

Salas-Zárate, M. P., et al. (2015). A systematic mapping study of ontologies for conference management. Journal of Systems and Software, 111, 235–248. https://doi.org/10.1016/j.jss.2015.08.051

Shon, S., et al. (2017). A microservice-based system architecture for a mobile conference application. International Conference on Information Networking (ICOIN), 615–617. https://doi.org/10.1109/ICOIN.2017.7899580 

Swacha, J., & Kulpa, A. (2023). Evolution of popularity and multiaspectual comparison of widely used web development frameworks. Electronics, 12(17), 3563. https://doi.org/10.3390/electronics12173563

Vögler, M., et al. (2016). DIANE: Dynamic IoT application deployment in edge and cloud environments. Proceedings of the 2016 IEEE International Conference on Mobile Service, 113–120. https://doi.org/10.1109/MobServ.2016.27

Wettinger, J., et al. (2014). Integrating cloud application topologies with cloud machine images. Proceedings of the 2014 IEEE/ACM 7th International Conference on Utility and Cloud Computing, 148–155. https://doi.org/10.1109/UCC.2014.23 

## Link for the YouTube Presentation
https://youtu.be/WaxjA6QSSKw


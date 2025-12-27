
## Link for Docker Image

: https://hub.docker.com/r/nrsyzlna/confeasebackend

## Instructions on how to run the Docker.

1. Clone the repo
2. Start project
   - docker-compose up --build
3. Access services
   Backend: http://localhost:8080
   MySQL: port 3307
4. Stop project
   - docker-compose down
5. Pull backend image from Docker Hub
   - docker pull nrsyzlna/confeasebackend:latest
   - docker run -p 8080:8080 nrsyzlna/confeasebackend:latest

## List of all the endpoints

### 1. ReviewScore

**GET / POST**
```bash
http://localhost:8080/api/review-scores
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/review-scores/{reviewId}/{criterionId}
```

### 2. Role

**GET / POST**
```bash
http://localhost:8080/api/roles
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/roles/{id}
```

### 3. Room

**GET / POST**
```bash
http://localhost:8080/api/rooms
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/rooms/{id}
```

### 4. Schedule

**GET / POST**
```bash
http://localhost:8080/api/schedules
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/schedules/{id}
```

### 5. ScoreCriterion

**GET / POST**
```bash
http://localhost:8080/api/score-criteria
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/score-criteria/{id}
```

### 6. Session

**GET / POST**
```bash
http://localhost:8080/api/sessions
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/sessions/{id}
```

### 7. Track

**GET / POST**
```bash
http://localhost:8080/api/tracks
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/tracks/{id}
```

### 8. User

**GET / POST**
```bash
http://localhost:8080/api/users
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/users/{id}
```

### 9. UserRole

**GET / POST**
```bash
http://localhost:8080/api/user-roles
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/user-roles/{userId}/{roleId}
```

### 10. Conference

**GET / POST**
```bash
http://localhost:8080/api/conferences
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/conferences/{id}
```

### 11. Discussion

**GET / POST**
```bash
http://localhost:8080/api/discussions
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/discussions/{id}
```

### 12. EmailLog

**GET / POST**
```bash
http://localhost:8080/api/email-logs
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/review-scores/email-logs/{id}
```

### 13. Event

**GET / POST**
```bash
http://localhost:8080/api/events
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/events/{id}
```

### 14. Keyword

**GET / POST**
```bash
http://localhost:8080/api/keywords
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/keywords/{id}
```

### 15. LogActivity

**GET / POST**
```bash
http://localhost:8080/api/log-activities
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/log-activities/{id}
```

### 16. Notification

**GET / POST**
```bash
http://localhost:8080/api/notifications
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/notifications/{id}
```

### 17. Paper

**GET / POST**
```bash
http://localhost:8080/api/papers
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/papers/{id}
```

### 18. PaperKeyword

**GET / POST**
```bash
http://localhost:8080/api/paper-keywords
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/paper-keywords/{paperId}/{keywordId}
```

### 19. Payment

**GET / POST**
```bash
http://localhost:8080/api/payments
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/payments/{id}
```

### 20. Rebuttal

**GET / POST**
```bash
http://localhost:8080/api/rebuttals
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/rebuttals/{id}
```

### 21. Registration

**GET / POST**
```bash
http://localhost:8080/api/registrations
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/registrations/{id}
```
### 22. Review

**GET / POST**
```bash
http://localhost:8080/api/reviews
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/reviews/{id}
```

### 23. Reviewer

**GET / POST**
```bash
http://localhost:8080/api/reviewers
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/reviewers/{id}
```

### 24. Author

**GET / POST**
```bash
http://localhost:8080/api/author
```

**GET / PUT / DELETE**
```bash
http://localhost:8080/api/review-scores/author/{id}
```

## Result/Output (Screenshot of the output)

Output API using POSTMAN

**Reviewer Controller**

1. GET all reviewers

   <img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/7ea8130d-4dec-4768-aac1-b53b93f9ed4e" />

2. GET reviewer by ID

   <img width="1918" height="1019" alt="image" src="https://github.com/user-attachments/assets/5d6f39cf-8498-48d6-b17b-507f7a987e29" />

3. POST create reviewer

   <img width="1919" height="1023" alt="image" src="https://github.com/user-attachments/assets/1e20cafe-8aa6-4fe2-ba3a-4836ae140d4e" />

4. PUT update reviewer

   <img width="1917" height="1023" alt="image" src="https://github.com/user-attachments/assets/90d2386b-bc4f-45a2-acf9-04760d0063f0" />


**Review Score Controller**

1. GET all review scores

   <img width="1913" height="1020" alt="image" src="https://github.com/user-attachments/assets/cf05617d-4d50-435d-812d-6bf4576e4353" />

2. GET review score by composite ID

   <img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/ef9b0d42-9f08-485c-b174-4be04fac24b0" />

3. POST create review score

   <img width="1919" height="1023" alt="image" src="https://github.com/user-attachments/assets/84a14c09-f2f2-483f-aba3-d1dc9e25d2ac" />

4. PUT update review score

   <img width="1919" height="1022" alt="image" src="https://github.com/user-attachments/assets/097d05ee-d291-47b0-8e90-6d73b21e34fc" />

5. DELETE review score

   <img width="1919" height="1021" alt="image" src="https://github.com/user-attachments/assets/1918f886-92fd-4301-86e0-6a500baf963d" />

After Deletion

   <img width="1919" height="1026" alt="image" src="https://github.com/user-attachments/assets/29b0cb15-9cc5-4b37-b832-aea79b730e9d" />


**Session Controller**

1. GET all sessions

   <img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/e8dcccd2-eced-44cb-9d0c-3912ca01b5c0" />

2. GET session by ID

   <img width="1919" height="1022" alt="image" src="https://github.com/user-attachments/assets/ff8e8feb-d143-44ad-a310-eb10b591ba7b" />

3. POST create session

   <img width="1919" height="1021" alt="image" src="https://github.com/user-attachments/assets/f552fe45-c524-48b9-92f4-1cce25147a50" />

4. PUT update session

   <img width="1919" height="1023" alt="image" src="https://github.com/user-attachments/assets/b45c8329-3f05-4106-886b-6c46002f8573" />


**Track Controller**

1. GET all tracks

   <img width="1919" height="1018" alt="image" src="https://github.com/user-attachments/assets/3c999707-8699-4f61-a4ea-6a8f96d7f579" />

2. GET track by ID

   <img width="1919" height="1023" alt="image" src="https://github.com/user-attachments/assets/5a763ae4-fd34-4aa0-a05d-ce001140ca34" />

3. POST create track

   <img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/71159131-89b7-469c-8306-305cfc253c7d" />

4. PUT update track

   <img width="1919" height="1022" alt="image" src="https://github.com/user-attachments/assets/003d1ad0-d713-4b7a-95ec-c6b24af9fbdf" />

**Role Controller**

1. GET all roles
   
   <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/bffb3a75-453b-44a7-9a43-3ce4dc2c0f08" />

2. GET role by ID
   
   <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/541e498e-64d2-4853-80b5-a06213917fb7" />

3. POST create role
   
   <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2a79c978-5910-41ad-aef3-3604cdf0936b" />

4. POST update role
   
   <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d7aee948-ed0e-4cb0-94e3-c5d7ce43efa1" />





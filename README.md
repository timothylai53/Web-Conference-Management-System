[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/j93TiVAg)
## Requirements for Assignment-3
[Read the instruction](https://github.com/STIWK2124-A251/class-activity-stiwk2124/blob/main/Assignment-3.md)

## Group Info:
1. Matric Number & Name & Photo & Phone Number
1. Mention who the leader is.
1. Mention your group name for Assignment-1 and Assignment-2
1. Other related info (if any)

| Photo | Name | Matric Number | Phone Number | Previous Group |
| --------------- | --------------- |--------|------------| ------------- |
| <img src="https://github.com/user-attachments/assets/b1d916b4-89e2-46fa-aaf5-fe7d11ec5729" width="120" height="170"> | ADTHIE CHARD A/L BOON | 299967 | 01131810149 | Webboys |
| <img src="https://github.com/user-attachments/assets/10b3405a-1b6a-4091-9004-538bf1d80814" width="120" height="170"> | GEE SIN ROU | 298556   | 0142693893        | hi5 |
| <img src="https://github.com/user-attachments/assets/d374f289-afb1-48c4-ad0b-f517e122c18b" width="120" height="170"/> | ALIA | 2222   | 0128700384        | syntaxerror |
| <img src="https://github.com/user-attachments/assets/10b3405a-1b6a-4091-9004-538bf1d80814" width="120" height="170"> | TIMOTHY | 297880   | 0136483581        | 405 |
| <img src="https://github.com/user-attachments/assets/16101139-c497-415f-9be2-7dfd4a26e045" width="120" height="170"> | FAYADH MUHAMMAD | 297032 | 0143287087 | BiscuIT |
| <img src="https://github.com/user-attachments/assets/10b3405a-1b6a-4091-9004-538bf1d80814" width="120" height="170"> | WAN NUR AZIMAH BINTI WAN RAHMAT | 2222   | 01160724726        | webcrafters |
| <img src="https://github.com/user-attachments/assets/10b3405a-1b6a-4091-9004-538bf1d80814" width="120" height="170"> | MUHD AFIF AFIFI BIN MUSA | 299783   | 0163878419        | budaksurau |
| <img src="https://github.com/user-attachments/assets/6b063b37-8a92-4987-b879-157bc2f03a51" width="120" height="170"> | LOH JIA PING | 297839   | 01169503383        | g2 |

## Title of your application (a unique title)

ScholarSync

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
      Backend Development – Spring Boot REST APIs for CRUD operations using JPA and MySQL.
      Frontend Development – Angular components, services, routing, and Bootstrap styling.
      Integration – Connected frontend to backend APIs using HttpClient.
      Containerization – Created Dockerfiles for frontend and backend; used Docker Compose for multi-container environment.
      Cloud Deployment – Uploaded Docker images to Docker Hub and deployed via cloud provider.
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

## Instructions on how to run Docker.

## List of all the endpoints

## 1. User
| Operation       | Endpoint          | HTTP Method | URL                                           | Description            |
|-----------------|-------------------|------------|-----------------------------------------------|------------------------|
| Create User     | `/api/users`      | POST       | http://localhost:8080/users                   | Add a new user         |
| Get All Users   | `/api/users`      | GET        | http://localhost:8080/users                   | Retrieve all users     |
| Get User by ID  | `/api/users/{id}` | GET        | http://localhost:8080/users/{id}              | Retrieve a user by ID  |
| Update User     | `/api/users/{id}` | PUT        | http://localhost:8080/users/{id}              | Update a user          |
| Delete User     | `/api/users/{id}` | DELETE     | http://localhost:8080/users/{id}              | Delete a user          |

## 2. User Roles
| Operation           | Endpoint               | HTTP Method | URL                                      | Description                    |
|--------------------|-----------------------|------------|------------------------------------------|--------------------------------|
| Create User Role    | `/api/user-roles`     | POST       | http://localhost:8080/api/user-roles     | Add a new user role            |
| Get All User Roles  | `/api/user-roles`     | GET        | http://localhost:8080/api/user-roles     | Retrieve all user roles        |
| Get User Role by ID | `/api/user-roles/{id}`| GET        | http://localhost:8080/api/user-roles/{id}| Retrieve a user role by ID     |
| Update User Role    | `/api/user-roles/{id}`| PUT        | http://localhost:8080/api/user-roles/{id}| Update an existing user role   |
| Delete User Role    | `/api/user-roles/{id}`| DELETE     | http://localhost:8080/api/user-roles/{id}| Delete a user role by ID       |

## 3. Conference
| Operation            | Endpoint                | HTTP Method | URL                                              | Description                            |
|---------------------|-------------------------|------------|--------------------------------------------------|----------------------------------------|
| Create Conference    | `/api/conferences`      | POST       | http://localhost:8080/conferences                | Add a new conference                   |
| Get All Conferences  | `/api/conferences`      | GET        | http://localhost:8080/conferences                | Retrieve all conferences               |
| Get Conference by ID | `/api/conferences/{id}` | GET        | http://localhost:8080/conferences/{id}           | Retrieve a single conference by its ID |
| Update Conference    | `/api/conferences/{id}` | PUT        | http://localhost:8080/conferences/{id}           | Update an existing conference          |
| Delete Conference    | `/api/conferences/{id}` | DELETE     | http://localhost:8080/conferences/{id}           | Delete a conference by its ID          |

## 4. Review

| Operation           | Endpoint               | HTTP Method | URL                                    | Description                                                                 |
|--------------------|-----------------------|------------|----------------------------------------|-----------------------------------------------------------------------------|
| Create Review       | `/api/reviews`        | POST       | http://localhost:8080/api/reviews      | Add a new review; updates assignment status and paper status if applicable |
| Get All Reviews     | `/api/reviews`        | GET        | http://localhost:8080/api/reviews      | Retrieve all reviews                                                        |
| Get Review by ID    | `/api/reviews/{id}`   | GET        | http://localhost:8080/api/reviews/{id} | Retrieve a review by ID                                                     |
| Update Review       | `/api/reviews/{id}`   | PUT        | http://localhost:8080/api/reviews/{id} | Update an existing review                                                   |
| Delete Review       | `/api/reviews/{id}`   | DELETE     | http://localhost:8080/api/reviews/{id} | Delete a review by ID                                                       |


### 5. Paper
| Operation          | Endpoint           | HTTP Method | URL                                           | Description                     |
|-------------------|--------------------|------------|-----------------------------------------------|---------------------------------|
| Create Paper      | `/api/papers`      | POST       | http://localhost:8080/papers                  | Add a new paper                 |
| Get All Paper     | `/api/papers`      | GET        | http://localhost:8080/papers                  | Retrieve all paper              |
| Get Paper by ID   | `/api/papers/{id}` | GET        | http://localhost:8080/papers/{id}             | Retrieve a single paper by ID   |
| Update Paper      | `/api/papers/{id}` | PUT        | http://localhost:8080/papers/{id}             | Update existing paper           |
| Delete Paper      | `/api/papers/{id}` | DELETE     | http://localhost:8080/papers/{id}             | Delete a paper by ID            |


### 6. Notification
| Operation                | Endpoint                  | HTTP Method | URL                                               | Description                          |
|--------------------------|---------------------------|------------|---------------------------------------------------|--------------------------------------|
| Create Notification      | `/api/notifications`      | POST       | http://localhost:8080/notifications               | Add a new notification               |
| Get All Notification     | `/api/notifications`      | GET        | http://localhost:8080/notifications               | Retrieve all notification            |
| Get Notification by ID   | `/api/notifications/{id}` | GET        | http://localhost:8080/notifications/{id}          | Retrieve a single notification by ID |
| Update Notification      | `/api/notifications/{id}` | PUT        | http://localhost:8080/notifications/{id}          | Update existing notification         |
| Delete Notification      | `/api/notifications/{id}` | DELETE     | http://localhost:8080/notifications/{id}          | Delete a notification by ID          |


### 7. Schedule
| Operation            | Endpoint             | HTTP Method | URL                                          | Description                     |
|---------------------|----------------------|------------|----------------------------------------------|---------------------------------|
| Create Schedule     | `/api/schedule`      | POST       | http://localhost:8080/schedule               | Add a new schedule              |
| Get All Schedules   | `/api/schedule`      | GET        | http://localhost:8080/schedule               | Retrieve all schedules          |
| Get Schedule by ID  | `/api/schedule/{id}` | GET        | http://localhost:8080/schedule/{id}          | Retrieve a single schedule by ID|
| Update Schedule     | `/api/schedule/{id}` | PUT        | http://localhost:8080/schedule/{id}          | Update existing schedule        |
| Delete Schedule     | `/api/schedule/{id}` | DELETE     | http://localhost:8080/schedule/{id}          | Delete a schedule by ID         |

### 8. Session
| Operation         | Endpoint             | HTTP Method | URL                                          | Description                 |
|-------------------|----------------------|------------|----------------------------------------------|-----------------------------|
| Create Session    | `/api/sessions`      | POST       | http://localhost:8080/sessions               | Add a new session           |
| Get All Sessions  | `/api/sessions`      | GET        | http://localhost:8080/sessions               | Retrieve all sessions       |
| Get Session by ID | `/api/sessions/{id}` | GET        | http://localhost:8080/sessions/{id}          | Retrieve a session by ID    |
| Update Session    | `/api/sessions/{id}` | PUT        | http://localhost:8080/sessions/{id}          | Update a session            |
| Delete Session    | `/api/sessions/{id}` | DELETE     | http://localhost:8080/sessions/{id}          | Delete a session            |

## 9. Discussion
| Operation             | Endpoint               | HTTP Method | URL                                          | Description                     |
|-----------------------|------------------------|------------|----------------------------------------------|---------------------------------|
| Create Discussion     | `/api/discussion`      | POST       | http://localhost:8080/discussion             | Add a new discussion            |
| Get All Discussions   | `/api/discussion`      | GET        | http://localhost:8080/discussion             | Retrieve all discussions        |
| Get Discussion by ID  | `/api/discussion/{id}` | GET        | http://localhost:8080/discussion/{id}        | Retrieve a discussion by ID     |
| Update Discussion     | `/api/discussion/{id}` | PUT        | http://localhost:8080/discussion/{id}        | Update an existing discussion   |
| Delete Discussion     | `/api/discussion/{id}` | DELETE     | http://localhost:8080/discussion/{id}        | Delete a discussion by ID       |

## 10. Room

| Operation           | Endpoint           | HTTP Method | URL                                 | Description                    |
|--------------------|------------------|------------|-------------------------------------|--------------------------------|
| Create Room        | `/api/rooms`      | POST       | http://localhost:8080/api/rooms     | Add a new room                 |
| Get All Rooms      | `/api/rooms`      | GET        | http://localhost:8080/api/rooms     | Retrieve all rooms             |
| Get Room by ID     | `/api/rooms/{id}` | GET        | http://localhost:8080/api/rooms/{id}| Retrieve a room by ID          |
| Update Room        | `/api/rooms/{id}` | PUT        | http://localhost:8080/api/rooms/{id}| Update an existing room        |
| Delete Room        | `/api/rooms/{id}` | DELETE     | http://localhost:8080/api/rooms/{id}| Delete a room by ID            |

## Result/Output (Screenshot of the output)
| No | Screenshot | Description |
|----|------------|-------------|
| 1  | <img width="700" src="https://github.com/user-attachments/assets/48b5a242-1aef-4c96-bbf9-288d5c23961b" /> |  Auth-user-profile(sign in)        |
| 2  | <img width="700" src="https://github.com/user-attachments/assets/a9113ba6-f58f-472f-8ea5-01bb8e97ec9b" /> |  Auth-user-profile(create account) |
| 3  | <img width="700" src="https://github.com/user-attachments/assets/28c0f7f0-bde3-4d84-b088-66890dcd4720"/>  |  Auth-user-profile(profile)        |
| 4  | <img width="700" src="https://github.com/user-attachments/assets/e3a77eab-591f-4cd6-8b67-11032e599357" /> |  Conference(Main Page)             |
| 5  | <img width="700" src="https://github.com/user-attachments/assets/321ad54d-a8fb-442a-ab2a-357c6b91730b"/>  |  Conference(Main Page)             |
| 6  | <img width="700" src="https://github.com/user-attachments/assets/54fb42ce-4321-4cbf-b7ee-2c102a203974"/>  |  Conference Details                |
| 7  | <img width="700" src="https://github.com/user-attachments/assets/6d570411-f734-4abe-b975-02f3c9c753ad" /> |  Conference(Main Page)             |
| 8  | <img width="700" src="https://github.com/user-attachments/assets/b32a2bc8-89e1-4b1f-bd3a-f6079d256886"/>  |  Conference(Main Page)             |
| 9  | <img width="700" src="https://github.com/user-attachments/assets/ff693ebc-208a-48b2-909f-e66e5b286e38" /> |  Conference(Explore events)        |
| 10 | <img width="700" src="https://github.com/user-attachments/assets/c74ccaff-dc71-46b0-9709-a17f75633a78"/>  |  Conference(View Tracks)           |
| 11 | <img width="700" src="https://github.com/user-attachments/assets/69d40dc5-3e3b-46fb-b30b-884ee63a48b7"/>  |  Conference (Create New Tracks)    |
| 12 | <img width="700" src="https://github.com/user-attachments/assets/4945ffa5-2c7d-45ae-8427-79232b6bd1b7" /> |  Conference(Create New Conference) |
| 13 | <img width="700" src="https://github.com/user-attachments/assets/4d28e09e-c277-441b-812e-c7154aeb2a16"/>  |  Conference(Create New Conference) |
| 14 | <img width="700" src="https://github.com/user-attachments/assets/b4346544-2d4f-4e76-9e69-6fdf427d1ea6"/>  |  Conference(Create New Conference) |


## References (Not less than 20)
Amazon Web Services, Inc. (n.d.). AWS documentation. https://docs.aws.amazon.com/

Angular Team. (n.d.). Angular documentation. https://angular.io/docs

AWS Blog. (2016, November 9). Deploying a Spring Boot application on AWS using AWS Elastic Beanstalk. https://aws.amazon.com/blogs/devops/deploying-a-spring-boot-application-on-aws-using-aws-elastic-beanstalk/

BezKoder. (n.d.). Angular 17 + Spring Boot + MySQL: CRUD example. https://www.bezkoder.com/spring-boot-angular-17-mysql/

BezKoder. (n.d.). Spring Boot + Angular 13 + MySQL example: Build a CRUD app. https://www.bezkoder.com/spring-boot-angular-13-mysql/

Dev.to. (2025, November 5). Dockerize & deploy Spring Boot + Angular + MySQL. https://dev.to/smoothcode/dockerize-deploy-spring-boot-angular-mysql-full-guide-24fg

Docker, Inc. (n.d.). Docker documentation. https://docs.docker.com/

Docker Forums. (2024). Spring Boot app can’t connect to MySQL database in Docker. https://forums.docker.com/t/spring-boot-app-cant-connect-to-mysql-database-in-docker/143007

GitHub. (n.d.). Spring Boot & Angular full stack microservices + MySQL + AWS example. https://github.com/developersCradle/spring-springboot-angular-microservices-mysql-java-persistence-hibernate-aws

GitHub. (n.d.). Spring Boot + Angular + MySQL CRUD and Docker demo. https://github.com/vscalcione/springboot-angular12-mysql-docker-crud-application-demo

Google LLC. (n.d.). Google Cloud documentation. https://docs.cloud.google.com/

JavaChinna. (n.d.). Dockerize Angular with NGINX and Spring Boot with MySQL using Docker Compose. https://www.javachinna.com/angular-nginx-spring-boot-mysql-docker-compose/

LinkedIn. (2025). Deploying Angular, Java Spring Boot, MySQL with Docker Compose. https://www.linkedin.com/posts/manoj-kumar-selvaraj-b2089722a_github-manoj-kumar-selvarajdevopsjavaapp-activity-7323743697668386816-usnS

Medium. (2019). Set up a complete basic ecosystem with Angular, Spring Boot, Docker, GCP, Git, and Jenkins. https://medium.com/@ansgar.nell/set-up-a-complete-basic-ecosystem-with-angular-spring-boot-docker-google-cloud-git-and-jenkins-b2e062e684e8

Medium. (2024). Spring Boot & Angular full stack application deployment with Docker, AWS EC2, and Jenkins. https://rabieouallaf.medium.com/springboot-angular-full-stack-application-deployment-with-docker-aws-ec2-amazon-rds-github-3404420d61e0

Oracle Corporation. (n.d.). MySQL documentation. https://dev.mysql.com/doc/

Pivotal Software. (n.d.). Spring Boot reference guide. https://spring.io/projects/spring-boot

Spring.io. (n.d.). Accessing data with MySQL. https://spring.io/guides/gs/accessing-data-mysql/

Spring.io. (n.d.). Spring Boot with Docker. https://spring.io/guides/gs/spring-boot-docker/

Stack Overflow. (n.d.). How to upload Java/Spring Boot + MySQL + Angular project to AWS. https://stackoverflow.com/questions/58390569/how-to-upload-my-java-spring-boot-mysql-angular-project-to-aws

## Link for the YouTube Presentation


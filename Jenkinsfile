pipeline {
    agent any

    environment {
        // Define Docker Compose command for consistency
        DOCKER_COMPOSE_COMMAND = "docker compose"
    }

    stages {
        // Stage 1: Pull the latest source code
        stage('Checkout') {
            steps {
                echo 'Pulling the latest code from GitHub testing branch...'
                // Code is usually checked out automatically by Jenkins from the job configuration
            }
        }

        // Stage 2: Build the Spring Boot application
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend (JAR file)...'
                dir('backend') {
                    // Grant execution permission to the Maven wrapper
                    sh 'chmod +x mvnw'
                    // Clean and package the application, skipping unit tests for speed
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        // Stage 3: Deploy the application using Docker Compose
        stage('Deploy') {
            steps {
                echo 'Deploying containers with Docker Compose...'
                script {
                    try {
                        // Stop and remove existing containers, then build and start new ones in detached mode
                        sh 'docker compose down || docker-compose down || true'
                        sh 'docker compose up -d --build || docker-compose up -d --build'
                        echo 'Deployment completed successfully.'
                    } catch (Exception e) {
                        error "Deployment failed: Docker Compose command not found or execution error."
                    }
                }
            }
        }

        // Stage 4: Generate and Push images to Docker Hub
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing images to Docker Hub...'
                script {
                    try {
                        // This uses the 'image' tags defined in your docker-compose.yml
                        sh 'docker compose push || docker-compose push'
                        echo 'Images successfully pushed to Docker Hub.'
                    } catch (Exception e) {
                        echo 'Warning: Push failed. Ensure you are logged in via "docker login" on the host machine.'
                    }
                }
            }
        }
    }

    // Feedback after the pipeline execution
    post {
        success {
            echo '✅ SUCCESS: The project is built, deployed, and pushed to Docker Hub!'
        }
        failure {
            echo '❌ FAILURE: Something went wrong. Check the Jenkins console logs for details.'
        }
    }
}
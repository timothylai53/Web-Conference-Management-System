pipeline {
    agent any

    environment {
        // REPLACE 'your_dockerhub_username' with your actual Docker Hub ID
        DOCKER_HUB_USER = 'sinrou0410'
        APP_NAME = 'confease-backend'
    }

    stages {
        // Stage 1: Build the JAR file
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend (JAR file)...'
                dir('backend') {
                    sh 'chmod +x mvnw'
                    // We run clean package but skip tests here to build the artifact first
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        // Stage 2: Run Playwright Tests & Generate Allure Results
        stage('Test') {
            steps {
                echo 'Running Playwright Tests...'
                dir('backend') {
                    // Running actual tests to generate 'target/allure-results'
                    sh './mvnw test'
                }
            }
        }

        // Stage 3: Deploy locally using Docker Compose
        stage('Deploy') {
            steps {
                echo 'Deploying containers locally...'
                script {
                    try {
                        // Use permission fix if needed: docker exec -u root my-jenkins chmod 666 /var/run/docker.sock
                        sh 'docker compose down || true'
                        sh 'docker compose up -d --build'
                        echo 'Deployment successful.'
                    } catch (Exception e) {
                        error "Deployment failed. Check docker.sock permissions."
                    }
                }
            }
        }

        // Stage 4: Tag and Push images to Docker Hub
        stage('Push to Docker Hub') {
            steps {
                echo 'Tagging and Pushing images...'
                script {
                    // Tagging the local image to match Docker Hub format: username/repository:tag
                    sh "docker tag ${APP_NAME}:latest ${DOCKER_HUB_USER}/${APP_NAME}:latest"
                    sh "docker push ${DOCKER_HUB_USER}/${APP_NAME}:latest"
                }
            }
        }
    }

    post {
        always {
            // Generate Allure Report in Jenkins UI
            // Ensure Allure Plugin is installed in Jenkins
            allure includeProperties: false, results: [[path: 'backend/target/allure-results']]
        }
        success {
            echo '✅ SUCCESS: Built, Tested, Deployed, and Pushed to Docker Hub!'
        }
        failure {
            echo '❌ FAILURE: Pipeline failed. Check console logs and Allure report.'
        }
    }
}
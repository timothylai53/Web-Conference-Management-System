pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'sinrou0410'
        // 根据你刚才 docker images 的结果修改：
        BACKEND_IMAGE = 'assignment-3-pipeline-backend'
        FRONTEND_IMAGE = 'assignment-3-pipeline-frontend'
    }

    stages {
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend...'
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying containers locally...'
                script {
                    sh 'docker compose down || true'
                    sh 'docker compose up -d --build'
                    echo 'Waiting 45s for stability...'
                    sh 'sleep 45'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running Playwright Tests...'
                dir('backend') {
                    // 运行测试产生 allure-results
                    sh './mvnw test'
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Tagging and Pushing Backend & Frontend...'
                script {
                    // 后端 Push
                    sh "docker tag ${BACKEND_IMAGE}:latest ${DOCKER_HUB_USER}/confease-backend:latest"
                    sh "docker push ${DOCKER_HUB_USER}/confease-backend:latest"

                    // 前端 Push (既然老师要求 Image of your frontend)
                    sh "docker tag ${FRONTEND_IMAGE}:latest ${DOCKER_HUB_USER}/confease-frontend:latest"
                    sh "docker push ${DOCKER_HUB_USER}/confease-frontend:latest"
                }
            }
        }
    }

    post {
        always {
            // 修正路径：由于是在 dir('backend') 下运行的测试，结果在 backend/target
            allure includeProperties: false, results: [[path: 'backend/target/allure-results']]
        }
        success {
            echo '✅ MISSION ACCOMPLISHED: Everything is on Docker Hub!'
        }
    }
}
pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'sinrou0410'
        APP_NAME = 'confease-backend'
    }

    stages {
        // Stage 1: 编译 JAR 包（跳过测试以加快速度）
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend (JAR file)...'
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        // Stage 2: 部署到 Docker（让系统跑起来，测试才能访问）
        stage('Deploy') {
            steps {
                echo 'Deploying containers locally...'
                script {
                    try {
                        sh 'docker compose down || true'
                        sh 'docker compose up -d --build'
                        echo 'Waiting 15 seconds for backend to stabilize...'
                        sh 'sleep 15' // 给数据库和后端一点启动时间
                    } catch (Exception e) {
                        error "Deployment failed. Check docker.sock permissions."
                    }
                }
            }
        }

        // Stage 3: 运行 Playwright 测试（此时网页已在线）
        stage('Test') {
            steps {
                echo 'Running Playwright Tests...'
                dir('backend') {
                    // 运行测试并生成 allure-results
                    sh './mvnw test'
                }
            }
        }

        // Stage 4: 推送到 Docker Hub
        stage('Push to Docker Hub') {
            steps {
                echo 'Tagging and Pushing images...'
                script {
                    sh "docker tag ${APP_NAME}:latest ${DOCKER_HUB_USER}/${APP_NAME}:latest"
                    sh "docker push ${DOCKER_HUB_USER}/${APP_NAME}:latest"
                }
            }
        }
    }

    post {
        always {
            // 无论测试成功还是失败，都生成 Allure 报告
            allure includeProperties: false, results: [[path: 'backend/target/allure-results']]
        }
        success {
            echo '✅ SUCCESS: Built, Deployed, Tested, and Pushed to Docker Hub!'
        }
        failure {
            echo '❌ FAILURE: Pipeline failed. Check console logs and Allure report.'
        }
    }
}
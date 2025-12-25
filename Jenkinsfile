pipeline {
    agent any

    // 定义环境变量（可选）
    environment {
        DOCKER_COMPOSE_COMMAND = "docker compose" // 新版本 Docker 推荐用法
    }

    stages {
        // 步骤 1: 拉取代码
        stage('Checkout') {
            steps {
                echo 'Pulling the latest code from GitHub testing branch...'
            }
        }

        // 步骤 2: 后端打包 (Spring Boot)
        stage('Build Backend') {
            steps {
                echo 'Step 2: Building Spring Boot Backend...'
                dir('backend') {
                    // 赋予 mvnw 执行权限，防止 Permission Denied
                    sh 'chmod +x mvnw'
                    // 执行打包，跳过测试以加快速度
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        // 步骤 3: 部署 (Docker)
        stage('Deploy') {
            steps {
                echo 'Step 3: Starting Docker Containers...'
                script {
                    // 这是一个兼容性写法：如果 'docker compose' 报错，就尝试 'docker-compose'
                    try {
                        sh 'docker compose down || docker-compose down || true'
                        sh 'docker compose up -d --build || docker-compose up -d --build'
                        echo 'Deployment completed using Docker Compose.'
                    } catch (Exception e) {
                        error "Failed to deploy: Docker Compose command not found in Jenkins container."
                    }
                }
            }
        }
    }

    // 构建结束后的反馈
    post {
        success {
            echo '✅ SUCCESS: Confease Project is now running!'
        }
        failure {
            echo '❌ FAILURE: Build or Deployment failed. Please check the logs above.'
        }
    }
}
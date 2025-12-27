pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'sinrou0410'
        // 确保这里的名字与你的 docker-compose.yml 中的服务名保持逻辑一致
        BACKEND_IMAGE = 'assignment-3-pipeline-backend'
        FRONTEND_IMAGE = 'assignment-3-pipeline-frontend'
    }

    stages {
        stage('Cleanup') {
            steps {
                echo 'Cleaning up old images and containers...'
                // 强制停止旧容器并移除，防止端口占用或旧镜像残留
                sh 'docker compose down --volumes --remove-orphans || true'
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend (JAR)...'
                dir('backend') {
                    sh 'chmod +x mvnw'
                    // 跳过测试进行打包，测试留到部署后的 Test 阶段跑 E2E
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying containers with fresh build...'
                script {
                    // 使用 --build 和 --force-recreate 解决你看到的“一小时前旧镜像”问题
                    sh 'docker compose up -d --build --force-recreate'

                    echo 'Waiting 60s for Database and Services to be fully ready...'
                    sh 'sleep 60'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running Playwright E2E Tests...'
                dir('backend') {
                    // 执行测试。注意：如果测试失败，Jenkins 默认会在这里停止，除非 pom.xml 忽略了错误
                    sh './mvnw test'
                }
            }
        }

        stage('Push to Docker Hub') {
            // 只有当 Test 阶段成功时，才会执行 Push
            steps {
                echo 'Tagging and Pushing Backend & Frontend...'
                script {
                    // 后端 Push
                    sh "docker tag ${BACKEND_IMAGE}:latest ${DOCKER_HUB_USER}/confease-backend:latest"
                    sh "docker push ${DOCKER_HUB_USER}/confease-backend:latest"

                    // 前端 Push
                    sh "docker tag ${FRONTEND_IMAGE}:latest ${DOCKER_HUB_USER}/confease-frontend:latest"
                    sh "docker push ${DOCKER_HUB_USER}/confease-frontend:latest"
                }
            }
        }
    }

    post {
        always {
            // 确保生成 Allure 报告，即使测试失败也能看到截图
            echo 'Generating Allure Report...'
            allure includeProperties: false, results: [[path: 'backend/target/allure-results']]

            // 停止容器以节省服务器资源
            //sh 'docker compose down || true'
        }
        success {
            echo '✅ MISSION ACCOMPLISHED: All tests passed and images are on Docker Hub!'
        }
        failure {
            echo '❌ PIPELINE FAILED: Check the Allure report and screenshots.'
        }
    }
}
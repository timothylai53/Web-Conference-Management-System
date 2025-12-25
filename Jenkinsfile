pipeline {
    agent any

    stages {
        // 步骤 1: 拉取代码 (Jenkins 会自动处理这一步，所以我们写个 echo 确认一下)
        stage('Checkout') {
            steps {
                echo 'Checking out code from GitHub testing branch...'
            }
        }

        // 步骤 2: 编译后端 (生成 JAR 包)
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot Backend...'
                dir('backend') {
                    // 使用 Maven 包装器进行编译
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        // 步骤 3: 使用 Docker 部署
        stage('Deploy') {
            steps {
                echo 'Starting Docker Containers...'
                // 运行你之前写的 docker-compose 命令
                // --build 会确保前端和后端的镜像都会被重新构建
                sh 'docker-compose down'
                sh 'docker-compose up -d --build'
            }
        }
    }

    // 构建结束后的动作
    post {
        success {
            echo 'SUCCESS: Your project is now live!'
        }
        failure {
            echo 'FAILURE: Something went wrong. Check the logs above.'
        }
    }
}
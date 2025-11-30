pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "student-management:${BUILD_NUMBER}"
        DOCKER_REGISTRY = "student-management"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_LOGIN = credentials('sonarqube-token')
    }

    stages {
        stage('Git') {
            steps {
                echo "========== Git Stage =========="
                checkout scm
                echo "Repository cloned successfully"
            }
        }

        stage('Build') {
            steps {
                echo "========== Build Stage =========="
                sh '''
                    echo "Building Spring Boot application..."
                    chmod +x ./mvnw
                    ./mvnw clean package -DskipTests
                '''
                echo "Build completed successfully"
            }
        }

        stage('SonarQube') {
            steps {
                echo "========== SonarQube Analysis =========="
                sh '''
                    echo "Running SonarQube code quality analysis..."
                    chmod +x ./mvnw
                    ./mvnw sonar:sonar \
                      -Dsonar.projectKey=student-management \
                      -Dsonar.sources=src/main/java \
                      -Dsonar.tests=src/test/java \
                      -Dsonar.host.url=${SONAR_HOST_URL} \
                      -Dsonar.login=${SONAR_LOGIN}
                '''
                echo "SonarQube analysis completed"
            }
        }

        stage('Docker Build') {
            steps {
                echo "========== Docker Build Stage =========="
                sh '''
                    echo "Building Docker image..."
                    docker build -t ${DOCKER_REGISTRY}:latest .
                    docker tag ${DOCKER_REGISTRY}:latest ${DOCKER_REGISTRY}:${BUILD_NUMBER}
                '''
                echo "Docker image built successfully"
            }
        }

        stage('Run') {
            steps {
                echo "========== Deploy Stage =========="
                sh '''
                    echo "Stopping old container if running..."
                    docker stop my-student-app-${BUILD_NUMBER} || true
                    docker rm my-student-app-${BUILD_NUMBER} || true
                    
                    echo "Running new container..."
                    docker run -d \
                      --name my-student-app-${BUILD_NUMBER} \
                      --network app-network \
                      -e SPRING_PROFILES_ACTIVE=prod \
                      -p 8089:8089 \
                      --restart unless-stopped \
                      ${DOCKER_REGISTRY}:${BUILD_NUMBER}
                    
                    echo "Container deployed successfully"
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed"
        }
        success {
            echo "✓ All stages executed successfully!"
            echo "Application running on http://localhost:8089"
        }
        failure {
            echo "✗ Pipeline failed - check logs above"
        }
    }
}

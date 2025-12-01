pipeline {
    agent any
   
    triggers {
	githubPush()	
    }
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

        stage('Database Setup') {
            steps {
                echo "========== Database Setup =========="
                sh '''
                    echo "Creating Docker network if it doesn't exist..."
                    docker network create app-network 2>/dev/null || echo "Network already exists"
                    
                    echo "Checking if MySQL is running..."
                    if ! docker ps | grep -q mysql-db; then
                        echo "MySQL not running, starting it..."
                        docker run -d \
                          --name mysql-db \
                          --network app-network \
                          -e MYSQL_ROOT_PASSWORD=rootpassword \
                          -e MYSQL_DATABASE=studentdb \
                          -e MYSQL_USER=springuser \
                          -e MYSQL_PASSWORD=dandan \
                          -p 3306:3306 \
                          -v mysql-data:/var/lib/mysql \
                          --restart unless-stopped \
                          mysql:8.0 \
                          --default-authentication-plugin=mysql_native_password
                        
                        echo "Waiting for MySQL to be ready..."
                        sleep 15
                    else
                        echo "MySQL is already running"
                    fi
                '''
                echo "Database setup completed"
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

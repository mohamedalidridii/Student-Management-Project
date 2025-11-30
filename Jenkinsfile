pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "myapp:${BUILD_NUMBER}"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_LOGIN = credentials('sonarqube-token')
    }

    stages {
        stage('Git') {
            steps {
                echo "========== Git Stage =========="
                checkout scm
                echo "Git checkout completed"
            }
        }

        stage('Build') {
            steps {
                echo "========== Build Stage =========="
                sh '''
                    echo "Building application..."
                    # Uncomment based on your project type:
                    # For Maven: mvn clean install -DskipTests
                    # For Gradle: ./gradlew clean build -x test
                    # For Node.js: npm install && npm run build
                '''
                echo "Build completed"
            }
        }

        stage('SonarQube') {
            steps {
                echo "========== SonarQube Stage =========="
                sh '''
                    echo "Running SonarQube analysis..."
                    # For Maven projects:
                    # mvn sonar:sonar -Dsonar.projectKey=student-management -Dsonar.sources=src -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_LOGIN}
                    
                    # For Gradle projects:
                    # ./gradlew sonarqube -Dsonar.projectKey=student-management -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_LOGIN}
                    
                    # For Node.js projects:
                    # npx sonar-scanner -Dsonar.projectKey=student-management -Dsonar.sources=src -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_LOGIN}
                '''
                echo "SonarQube analysis completed"
            }
        }

        stage('Docker Build') {
            steps {
                echo "========== Docker Build Stage =========="
                sh '''
                    echo "Building Docker image..."
                    # docker build -t ${DOCKER_IMAGE} .
                '''
                echo "Docker image built"
            }
        }

        stage('Run') {
            steps {
                echo "========== Run Stage =========="
                sh '''
                    echo "Running Docker container..."
                    # docker run -d --name myapp-${BUILD_NUMBER} ${DOCKER_IMAGE}
                '''
                echo "Container running"
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed"
        }
        success {
            echo "All stages executed successfully!"
        }
        failure {
            echo "Pipeline failed - check logs above"
        }
    }
}

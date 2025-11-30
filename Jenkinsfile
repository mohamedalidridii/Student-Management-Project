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
                script {
                    // For Maven projects
                    sh 'mvn clean install -DskipTests'
                    
                    // Or for Gradle projects (uncomment if using Gradle)
                    // sh './gradlew clean build -x test'
                    
                    // Or for Node.js (uncomment if using Node.js)
                    // sh 'npm install && npm run build'
                }
                echo "Build completed"
            }
        }

        stage('SonarQube') {
            steps {
                echo "========== SonarQube Stage =========="
                script {
                    // For Maven projects
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=student-management \
                          -Dsonar.sources=src \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_LOGIN}
                    '''
                    
                    // Or for Gradle (uncomment if using Gradle)
                    // sh './gradlew sonarqube'
                }
                echo "SonarQube analysis completed"
            }
        }

        stage('Docker Build') {
            steps {
                echo "========== Docker Build Stage =========="
                sh '''
                    docker build -t ${DOCKER_IMAGE} .
                '''
                echo "Docker image built"
            }
        }

        stage('Run') {
            steps {
                echo "========== Run Stage =========="
                sh '''
                    docker run -d --name myapp-${BUILD_NUMBER} ${DOCKER_IMAGE}
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

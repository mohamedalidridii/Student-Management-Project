pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "myapp:${BUILD_NUMBER}"
        SONAR_TOKEN = credentials('sonarqube-token')
        REGISTRY = "your-docker-registry"
    }

    stages {
        stage('Git') {
            steps {
                echo "Cloning repository..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building application..."
                sh './mvn clean install' // or 'mvn clean install' for Maven, 'npm install' for Node.js, etc.
            }
        }

        stage('SonarQube') {
            steps {
                echo "Running SonarQube analysis..."
                sh '''
                    ./gradlew sonarqube \
                      -Dsonar.projectKey=my-project \
                      -Dsonar.sources=src \
                      -Dsonar.host.url=http://sonarqube-server:9000 \
                      -Dsonar.login=$SONAR_TOKEN
                '''
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image..."
                sh '''
                    docker build -t ${DOCKER_IMAGE} .
                    docker tag ${DOCKER_IMAGE} ${REGISTRY}/${DOCKER_IMAGE}
                '''
            }
        }

        stage('Run') {
            steps {
                echo "Pushing and running Docker image..."
                sh '''
                    docker push ${REGISTRY}/${DOCKER_IMAGE}
                    docker run -d --name myapp-${BUILD_NUMBER} ${REGISTRY}/${DOCKER_IMAGE}
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed"
        }
        success {
            echo "Pipeline succeeded!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}

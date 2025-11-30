pipeline {
    agent any

    stages {
        stage('Git') {
            steps {
                echo "========== Git Stage =========="
                echo "Checking out code from repository"
                checkout scm
                echo "Git checkout completed"
            }
        }

        stage('Build') {
            steps {
                echo "========== Build Stage =========="
                echo "Building application"
                sh 'echo "Build process here"'
                echo "Build completed"
            }
        }

        stage('SonarQube') {
            steps {
                echo "========== SonarQube Stage =========="
                echo "Running code quality analysis"
                sh 'echo "SonarQube analysis here"'
                echo "SonarQube analysis completed"
            }
        }

        stage('Docker Build') {
            steps {
                echo "========== Docker Build Stage =========="
                echo "Building Docker image"
                sh 'echo "Docker build here"'
                echo "Docker image built"
            }
        }

        stage('Run') {
            steps {
                echo "========== Run Stage =========="
                echo "Running Docker container"
                sh 'echo "Docker run here"'
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

pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.0' // Configure this in Jenkins Global Tool Configuration
        jdk 'JDK-17' // Configure this in Jenkins Global Tool Configuration
    }
    
    environment {
        SPRING_PROFILES_ACTIVE = 'dev'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from GitHub...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // Add your deployment steps here
                // Examples:
                // sh 'scp target/*.jar user@server:/path/to/deploy/'
                // sh 'docker build -t myapp:latest .'
                // sh 'kubectl apply -f k8s/'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            // Optional: Send notifications
            // emailext body: 'Build succeeded!', subject: 'Jenkins Build Success', to: 'team@example.com'
        }
        failure {
            echo 'Pipeline failed!'
            // Optional: Send notifications
            // emailext body: 'Build failed!', subject: 'Jenkins Build Failure', to: 'team@example.com'
        }
        always {
            cleanWs()
        }
    }
}

pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verificar entorno') {
            steps {
                sh 'pwd'
                sh 'ls -la'
                sh 'java -version || true'
                sh 'mvn -version || true'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}

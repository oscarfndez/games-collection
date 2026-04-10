pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Publish to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_PASSWORD'
                )]) {
                    sh '''
                      mvn -B jib:build \
                        -Djib.to.auth.username=$DOCKERHUB_USERNAME \
                        -Djib.to.auth.password=$DOCKERHUB_PASSWORD
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
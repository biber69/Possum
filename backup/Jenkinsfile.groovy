pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = 'vaultdemo'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://your-git-server.com/your/repo.git', branch: 'main'
            }
        }

        stage('Build and Run Containers') {
            steps {
                script {
                    sh 'docker-compose down || true'
                    sh 'docker-compose up --build -d'
                }
            }
        }

        stage('Check Flask App') {
            steps {
                script {
                    // Пробуем 10 раз с паузой
                    def success = false
                    for (int i = 0; i < 10; i++) {
                        def result = sh(
                            script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:5000',
                            returnStdout: true
                        ).trim()
                        if (result == '200') {
                            echo "Flask app is up!"
                            success = true
                            break
                        } else {
                            sleep 3
                        }
                    }

                    if (!success) {
                        error "Flask app did not start correctly"
                    }
                }
            }
        }

        stage('Check Vault is Running') {
            steps {
                script {
                    def result = sh(
                        script: 'curl -s http://localhost:8200/v1/sys/health',
                        returnStatus: true
                    )
                    if (result != 0) {
                        error "Vault is not running or unhealthy"
                    }
                }
            }
        }

        stage('Show Running Containers') {
            steps {
                sh 'docker ps --format "table {{.Names}}\t{{.Status}}"'
            }
        }

        // Если хочешь автоудаление — включи этот блок:
        /*
        stage('Cleanup') {
            steps {
                sh 'docker-compose down'
            }
        }
        */
    }
}

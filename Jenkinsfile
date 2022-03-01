pipeline {
    agent any

    parameters {
        string(name: 'PROHECT_NAME', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
    }

    environment {
      hello = "guoch"
      WS = "${WORKSPACE}"
    }

    stages {
        stage('环境检查') {
            steps {
                sh 'printenv'
                echo "${hello}"
                echo "正在检查基本信息"
                sh 'docker version'
                sh 'java -version'
                sh 'git --version'
                echo "${PROHECT_NAME}"
            }
        }
        stage('代码编译') {
            agent {
                docker {
                     image 'maven:3-alpine'  //用完就会杀掉
                     args '-v maven-repo:/root/.m2'
                 }
            }
            steps {
                echo "编译"
                sh 'pwd && ls -alh'
                sh 'mvn -v'
                sh "echo 默认的工作目录: ${WS}"
                sh 'cd ${WS} && mvn clean package -s "/var/jenkins_home/appliconfig/maven/settings.xml"  -Dmaven.test.skip=true'
            }
        }
        stage('代码测试') {
            steps {
                echo('测试')
                sh 'pwd && ls -alh'
            }
        }
        stage('生成镜像') {
            steps {
                echo('打包')
                sh 'pwd && ls -alh'
                echo "正在构建 ${PROHECT_NAME}"
                sh 'cd ${PROHECT_NAME} && docker build -t ${PROHECT_NAME} .'
            }
        }
        stage('代码部署') {
            steps {
                echo('部署')
                echo '${PROHECT_NAME}'
                //sh 'docker rm -f devops-dev'
                sh 'docker run -d -p 9005:9005 --name ${PROHECT_NAME} ${PROHECT_NAME}'
            }
        }
    }
}
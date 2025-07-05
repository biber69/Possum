FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y curl

# Устанавливаем нужные плагины
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Добавляем seed job
COPY jobs /usr/share/jenkins/ref/jobs
COPY init.groovy.d /usr/share/jenkins/ref/init.groovy.d

USER jenkins

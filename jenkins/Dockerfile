FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y curl docker.io docker-compose

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY jobs/ /usr/share/jenkins/ref/jobs/
COPY docker-compose.yml /var/jenkins_home/docker-compose.yml

USER root

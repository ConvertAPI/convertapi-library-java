FROM openjdk:8-jdk-alpine

ENV VERSION=
ENV NEXT_VERSION=
ENV GPG_PASSPHRASE=
ARG GIT_EMAIL=
ARG FULL_NAME=

ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DIR="/root"
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

VOLUME /gpg
VOLUME /maven
VOLUME /ssh

COPY maven-release.sh /maven-release.sh

# Install Maven, Git and gpg
RUN apk add --no-cache curl tar bash git gnupg openssh
RUN mkdir -p /usr/share/maven && \
	curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
RUN git config --global user.email ${GIT_EMAIL} && \
 	git config --global user.name ${FULL_NAME}

# Configure gpg
RUN export GPG_TTY=/dev/console
RUN export GPG_OPTS='--pinentry-mode loopback'

RUN ["chmod", "+x", "/maven-release.sh"]
ENTRYPOINT /maven-release.sh

FROM openjdk:8

ARG BUILD_ENV=debug

# SBT
RUN apt-get -yqq update && apt-get -yqq install \
    apt-transport-https \
    ca-certificates
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get update && apt-get install -y \
    sbt

# NODEJS
RUN curl -sL https://deb.nodesource.com/setup_7.x | bash -
RUN apt-get install -y nodejs \
    build-essential
ENV SBT_OPTS "${SBT_OPTS} -Dsbt.jse.engineType=Node"
ENV JAVA_OPTS "${JAVA_OPTS} -Xmx256m -XX:-UseConcMarkSweepGC -XX:+UseG1GC -XX:+UseStringDeduplication"

RUN mkdir -p /code
COPY . /code
WORKDIR /code

# If we're in production mode, build the app
RUN if [ "$BUILD_ENV" = "production" ]; then sbt clean stage; fi
# RUN if [ "$BUILD_ENV" = "production" ]; then bash -c "sbt clean universal:packageZipTarball -v && tar xzf ./target/universal/painting-1.0-SNAPSHOT.tgz -C ./target/universal/"; fi

EXPOSE 9000
EXPOSE 9999

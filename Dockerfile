FROM openjdk:8

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
ENV JAVA_OPTS "${JAVA_OPTS} -Xmx128m -XX:-UseConcMarkSweepGC -XX:+UseG1GC -XX:+UseStringDeduplication"
RUN mkdir -p /code

COPY . /code
WORKDIR /code

EXPOSE 9000
EXPOSE 9999

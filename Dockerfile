FROM openjdk:8

ARG BUILD_ENV=production

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

ENV PAINTING_APP_VERSION "0.1.1"

RUN mkdir -p /code
COPY . /code
WORKDIR /code

# If we're in production mode, build the app
# RUN if [ "$BUILD_ENV" = "production" ]; then sbt clean stage; fi
RUN if [ "$BUILD_ENV" = "production" ]; then bash -c "sbt universal:packageZipTarball -v && tar xzf /code/target/universal/painting-$PAINTING_APP_VERSION.tgz -C /code/target/universal/ &&  ln -sf /code/target/universal/painting-$PAINTING_APP_VERSION/bin/painting /code/target/universal/painting"; fi

EXPOSE 9000

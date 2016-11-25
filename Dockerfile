FROM openjdk:8

RUN apt-get -yqq update && apt-get -yqq install apt-transport-https ca-certificates
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get update -qq && apt-get install -y build-essential sbt

RUN mkdir -p /code

COPY . /code
WORKDIR /code
EXPOSE 9000
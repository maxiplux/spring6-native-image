FROM ghcr.io/graalvm/graalvm-ce:22.3.1 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# For SDKMAN to work we need unzip & zip

RUN microdnf  install -y unzip zip wget
RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-*.zip
RUN export GRADLE_HOME=/opt/gradle/gradle-7.6
RUN export PATH=${GRADLE_HOME}/bin:${PATH}
#RUN gu install gradle
RUN gu install  native-image
RUN native-image --version
RUN /opt/gradle/gradle-7.6/bin/gradle --version
RUN /opt/gradle/gradle-7.6/bin/gradle nativeCompile


FROM ubuntu:latest
EXPOSE 8080
RUN rm -rf /workdir
RUN mkdir /workdir
WORKDIR /workdir


COPY --from=build /home/gradle/src/build/native/nativeCompile/*  /workdir/

#CMD [ "sh", "-c", "./spring-boot-graal -Dserver.port=$PORT" ]
#ENTRYPOINT ["./workdir/auth-final-2023"]
ENTRYPOINT ["/workdir/auth-final-2023"]
#CMD [ "/workdir/auth-final-2023" ]


#If you are here reading this, I would like to thank you for your time and effort. I hope this article was helpful to you. If you have any questions or suggestions, please feel free to leave a comment below. I will try to answer them as soon as possible.
#However, yes this is a native image for spring boot 3, don't delete the .txt inside this container
#docker build -t maxiplux/native.quantum.app .

#docker build -t maxiplux/native.quantum.app . && docker run -it maxiplux/native.quantum.app  tail -f /dev/null

#docker run  -p 8080:8080 maxiplux/native.quantum.app:1.0.0
#docker tag  711e5f177926 maxiplux/native.quantum.app:1.0.1
#docker tag  39d440f82330 maxiplux/livemarket.business.b2bcart:kuerbernetes
#docker push maxiplux/native.quantum.app:1.0.1
#docker push maxiplux/io.core.app:1.0.0
#docker push maxiplux/io.api.base:master .
#docker buildx build --platform linux/amd64,linux/arm64 maxiplux/io.api.base:1.0.0 --push -t maxiplux/io.api.base:1.0.0
#aws lightsail create-container-service --service-name api-server-demo --power micro --scale 1
#aws lightsail push-container-image --region us-east-1 --service-name api-server  --label  api-server   --image maxiplux/io.api.base:2022-04-03--40-22


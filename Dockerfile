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


FROM ghcr.io/graalvm/graalvm-ce:22.3.1

EXPOSE 8080

#RUN rm -rf /app
#RUN mkdir /app
WORKDIR /workdir
COPY --from=build /home/gradle/build/native/nativeCompile/auth-final-2023  /workdir/spring-boot-application.jar

#CMD [ "sh", "-c", "./spring-boot-graal -Dserver.port=$PORT" ]

ENTRYPOINT ["sh","/app/spring-boot-application.jar"]



#docker build -t maxiplux/native.quantum.app .

#docker build -t maxiplux/native.quantum.app . && docker docker run -it maxiplux/native.quantum.app  tail -f /dev/null

#docker run  -p 8080:8080 maxiplux/native.quantum.app:1.0.0
#docker tag  e0e2e41e5245 maxiplux/native.quantum.app:1.0.0
#docker tag  39d440f82330 maxiplux/livemarket.business.b2bcart:kuerbernetes
#docker push maxiplux/native.quantum.app:1.0.0
#docker push maxiplux/io.core.app:1.0.0
#docker push maxiplux/io.api.base:master .
#docker buildx build --platform linux/amd64,linux/arm64 maxiplux/io.api.base:1.0.0 --push -t maxiplux/io.api.base:1.0.0
#aws lightsail create-container-service --service-name api-server-demo --power micro --scale 1
#aws lightsail push-container-image --region us-east-1 --service-name api-server  --label  api-server   --image maxiplux/io.api.base:2022-04-03--40-22


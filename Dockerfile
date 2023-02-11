FROM fedora:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# For SDKMAN to work we need unzip & zip
RUN yum install -y unzip zip
RUN yum install curl bash unzip zip -y
RUN curl -s "https://get.sdkman.io" | bash





RUN source "$HOME/.sdkman/bin/sdkman-init.sh"

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" sdk install gradle
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" gradle --version
RUN  source "$HOME/.sdkman/bin/sdkman-init.sh" gu install native-image;
#RUN native-image --version
RUN source "$HOME/.sdkman/bin/sdkman-init.sh"   gradle nativeCompile --exclude-task test  --exclude-task test

FROM oraclelinux:7-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/native/nativeCompile/auth-final-2023  /app/spring-boot-application.jar

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


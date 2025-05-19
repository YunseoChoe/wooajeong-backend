# Build stage

FROM bellsoft/liberica-openjdk-alpine:17 AS builder

ARG MYSQL_DATABASE_URL
ARG MYSQL_DATABASE_USERNAME
ARG MYSQL_DATABASE_PASSWORD
ARG MONGODB_URI

WORKDIR /app

COPY . .


#RUN DECODED_MONGO_URI=$(echo "$MONGO_URI" | sed -e 's/%3A/:/g' -e 's/%2F/\//g' -e 's/%40/@/g' -e 's/%3F/?/g' -e 's/%3D/=/g' -e 's/%26/\&/g')

ENV MYSQL_DATABASE_URL $MYSQL_DATABASE_URL
ENV MYSQL_DATABASE_USERNAME $MYSQL_DATABASE_USERNAME
ENV MYSQL_DATABASE_PASSWORD $MYSQL_DATABASE_PASSWORD
ENV MONGODB_URI $MONGODB_URI

RUN ./gradlew build

# Run stage

FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
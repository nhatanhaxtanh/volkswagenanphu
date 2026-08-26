# Dùng image có sẵn Maven thay vì `apk add maven`: apk phải tải từ mirror
# Alpine, chỗ này từng treo 30+ phút ở 0% CPU trên VPS.
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Tải dependency trước, tách khỏi source. Chừng nào pom.xml không đổi thì
# layer này được tái dùng, sửa code chỉ build lại phần compile.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/vw-saigon-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# ═══════════════════════════════════════════════════
# المرحلة 1: البناء (Build Stage)
# ═══════════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# ننسخ الـ pom.xml الأول عشان الـ Docker يعمل Cache للـ dependencies
# (يعني لو عدلت الكود بس، مش هيحمل الـ dependencies تاني)
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests


# صورة خفيفة جداً (JRE بس + Alpine Linux ~5MB)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# ✅ أمان: نشغل التطبيق بيوزر عادي مش root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Xmx256m", \
    "-Xms128m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:+UseContainerSupport", \
    "-jar", \
    "app.jar"]
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем файлы Gradle
COPY gradle gradle
COPY app/build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

# Загружаем зависимости
RUN ./gradlew --no-daemon dependencies

# Копируем исходный код и конфиги
COPY app/src src
COPY app/config config

# Сборка проекта
RUN ./gradlew --no-daemon build
RUN ./gradlew --no-daemon installDist

# Настройка JVM параметров и профиля Spring
ENV JAVA_OPTS "-Xmx512M -Xms512M"
ENV SPRING_PROFILES_ACTIVE "production"

EXPOSE 7070

CMD app/build/install/app/bin/app --spring.profiles.active=$SPRING_PROFILES_ACTIVE
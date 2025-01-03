FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем все
COPY . .

# Загружаем зависимости
RUN ./gradlew --no-daemon dependencies

# Сборка проекта
RUN ./gradlew --no-daemon build
RUN ./gradlew --no-daemon installDist

# Настройка JVM параметров и профиля Spring
ENV JAVA_OPTS "-Xmx512M -Xms512M"
ENV SPRING_PROFILES_ACTIVE "production"

EXPOSE 7070

CMD ./app/build/install/app/bin/app --spring.profiles.active=${SPRING_PROFILES_ACTIVE}
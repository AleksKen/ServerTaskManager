FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем все
COPY . .

# Делаем gradlew исполняемым
RUN chmod +x ./gradlew

# Загружаем зависимости
RUN ./gradlew --no-daemon dependencies

# Сборка проекта
RUN ./gradlew --no-daemon build
RUN ./gradlew --no-daemon installDist

# Настройка JVM параметров и профиля Spring (исправляем ENV формат)
ENV JAVA_OPTS="-Xmx512M -Xms512M"
ENV SPRING_PROFILES_ACTIVE=production

EXPOSE 7070

# Запускаем в формате JSON (рекомендуется Docker'ом)
CMD ["./app/build/install/app/bin/app", "--spring.profiles.active=production"]

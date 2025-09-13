# Перезапуск одной командой контейера .\run-docker.ps1

# Останавливаем выполнение при ошибках
$ErrorActionPreference = "Stop"

Write-Host "`n=== Запуск скрипта Docker для account-service с проверкой сервисов ===`n"

# 1 Собираем jar
Write-Host "Собираем Spring Boot jar..."
mvn clean package -DskipTests
Write-Host "Jar успешно собран.`n"

# 2 Останавливаем старые контейнеры
Write-Host "Останавливаем старые контейнеры..."
docker-compose down
Write-Host "Старые контейнеры остановлены.`n"

# 3 Поднимаем Zookeeper, Kafka и Postgres
Write-Host "Поднимаем Zookeeper, Kafka и Postgres..."
docker-compose up -d zookeeper kafka db

# 4 Проверка готовности Postgres через healthcheck
Write-Host "Ожидаем Postgres (healthcheck)..."
$maxRetries = 10
$retry = 0
while ($retry -lt $maxRetries) {
    try {
        $status = docker inspect --format='{{.State.Health.Status}}' db 2>$null
        if ($status -eq "healthy") {
            Write-Host "Postgres готов!"
            break
        }
    } catch {}
    $retry++
    Write-Host "Ожидание Postgres... ($retry/$maxRetries)"
    Start-Sleep -Seconds 3
}

if ($retry -ge $maxRetries) { throw "Postgres не доступен после $maxRetries попыток." }

# 5 Проверка готовности Kafka через healthcheck (или sleep)
Write-Host "Ожидаем Kafka..."
Start-Sleep -Seconds 10   # Можно добавить healthcheck в docker-compose и проверять как Postgres
Write-Host "Kafka готова!"

# 6 Поднимаем account-service
Write-Host "Поднимаем account-service..."
docker-compose up -d account-service

# 7 Показываем логи account-service
Write-Host "`nПоказываем логи account-service..."
docker-compose logs -f account-service

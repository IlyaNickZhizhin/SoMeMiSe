# SoMeMiSe
This is a microservice architecture application for subsequent use in creating the backend of the Relocation UZ social network. The following microservices have been implemented:

1. Authorization (supports JWT Token), there is logic for the impossibility of creating a login and password without filling out a profile.
2. Profile (subscription/unsubscription functionality is organized) access by jwt-token. (access rights are not configured at the moment)
3. Common - global exception handler
4. Eureka - for microservice registration
5. Swagger-Ui and OpenAPI are connected for convenient workability check
6. The database is connected by a separate Docker container with Postress SQL. In Help-md there are commands for deploying the database and tables.


Приложение на микросервисной архитектуре, для последующего использваония в создании backend социальной сети Relocation UZ.
Реализованы слудующие микросервисы:

1 Autorization (поддерживает JWT Token), имеется логика невозможности создания логина и пароля, без заполнения профиля.
2 Profile (функционал подписки/отписки организован) доступ по jwt-токену. (ограничение прав в данный момент не настроены)
3 Common - глобальный обработчик исключений
4 Eureka - для регистрации микросервисов
5 Подключен Swagger-Ui и OpenAPI для удобной проверки работоспособности работы
6 База данных подключена отдельным Docker контейнером с Postress SQL. В Help-md есть команды для развертывания базы данных и таблиц.

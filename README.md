# LMS Lite

REST API для платформы онлайн-обучения: инструкторы создают курсы и уроки, студенты записываются на курсы, проходят уроки и тесты, система считает прогресс и выдаёт сертификаты.

**Стек:** Java 21, Spring Boot 3.5, Spring Data JPA (Hibernate 6), PostgreSQL, Maven, Lombok. Docker / Docker Compose.

## Запуск через Docker

Нужен установленный Docker.

1. Создайте файл `.env` по образцу `.env.example` (в нём пароль от базы):
   ```
   DB_PASSWORD=любой_пароль
   ```
2. Выполните:
   ```
   docker compose up --build
   ```
3. Приложение доступно на `http://localhost:8080`. База создаётся пустой, таблицы создаёт Hibernate при старте.

Данные базы хранятся в volume `db-data` и сохраняются после `docker compose restart`. Полная очистка: `docker compose down -v`. Приложение в контейнере запускается не от root.

## Демо-данные

После запуска на пустой базе выполните запросы из файла `docs/demo-requests.http` по порядку (в IntelliJ IDEA они запускаются стрелкой слева от запроса). Файл создаёт инструктора, трёх студентов, три курса, уроки всех типов, записи с разным прогрессом и один выданный сертификат. Запускать нужно один раз на пустой базе, иначе id и email не сойдутся: для сброса `docker compose down -v`.

## Запуск локально

1. Java 21 и PostgreSQL. Создайте базу `lms`.
2. Задайте переменные окружения (в скобках значения по умолчанию):
   `DB_HOST` (localhost), `DB_PORT` (5432), `DB_NAME` (lms), `DB_USER` (postgres), `DB_PASSWORD` (обязательна).
3. Запуск: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).

## Что сделано

**Этап 1. Модели.** Enum'ы, абстрактный `User` с наследниками `Student` и `Instructor`, `Course`.

**Этап 2. Иерархия уроков.** Абстрактный `Lesson`, наследники `VideoLesson`, `TextLesson`, `QuizLesson`, `Question`, подсчёт `getTotalEstimatedMinutes()`.

**Этап 3. Репозитории и сервисы.** `CourseService`, `EnrollmentService`, кастомные исключения.

**Этап 4. REST API.** Инструкторы, студенты, курсы, уроки, DTO (`record`), `@RestControllerAdvice` с `ProblemDetail`.

**Этап 5. Записи, прогресс, тесты.** Запись на курс, прогресс, отправка и проверка тестов, валидация (`jakarta.validation`).

**Этап 6. Сертификаты.** Автоматическая выдача при завершении курса, проверка по коду, скачивание текстового сертификата.

**Дополнительно:** Docker.

## Эндпоинты

| Метод | Адрес | Описание |
|---|---|---|
| POST, GET | `/api/instructors`, `/api/instructors/{id}`, `/api/instructors/{id}/courses` | регистрация и профиль инструктора, его курсы |
| POST, GET | `/api/students`, `/api/students/{id}` | регистрация и профиль студента |
| POST, GET, PUT, DELETE | `/api/courses`, `/api/courses/{id}` | курсы |
| PATCH | `/api/courses/{id}/publish`, `/api/courses/{id}/archive` | публикация и архивация |
| POST, GET | `/api/courses/{courseId}/lessons` | добавить урок, список уроков курса |
| GET, PUT, DELETE | `/api/lessons/{id}` | урок |
| GET, POST | `/api/lessons/{quizId}/questions`, `/api/lessons/{quizId}/submit` | вопросы теста (без ответов), сдача теста |
| POST, GET, DELETE | `/api/enrollments`, `/api/enrollments/{id}`, `/api/students/{id}/enrollments` | записи на курсы, отписка |
| POST, GET | `/api/enrollments/{id}/lessons/{lessonId}/complete`, `/api/enrollments/{id}/progress` | отметка урока, прогресс |
| GET | `/api/students/{id}/certificates`, `/api/certificates/{code}`, `/api/certificates/{id}/download` | сертификаты |

## Принятые решения и допущения

- **Наследование.** `User` это `@MappedSuperclass` (таблицы `students` и `instructors`), а `Lesson` использует `SINGLE_TABLE` с дискриминатором (одна таблица `lessons`).
- **DTO везде.** Сущности наружу не отдаются. DTO собираются внутри `@Transactional`-методов сервисов, `open-in-view` выключен.
- **Номер урока** (`orderIndex`) назначает сервер: максимальный в курсе плюс один.
- **Публикация и архивация.** Опубликовать можно только `DRAFT` с хотя бы одним уроком, архивировать только `PUBLISHED`, удалить только `DRAFT`.
- **Тесты (`QuizLesson`).** В тело `submit` добавлено поле `enrollmentId`, чтобы знать, чья это запись. Тест засчитывается только через `submit` при достижении проходного балла, через `complete` его отметить нельзя. Неудачные попытки не сохраняются, в `LessonProgress` попадает только засчитанный тест (лучший балл при пересдаче).
- **Повторная отметка** уже пройденного урока возвращает текущий прогресс без ошибки.
- **Отписка** переводит запись в `DROPPED`, а не удаляет её. Повторно записаться на тот же курс после отписки нельзя.
- **Сертификат** выдаётся один раз на пару «студент и курс» в момент перехода записи в `COMPLETED`. Скачивание отдаёт текст в UTF-8.
- **Уникальность email** проверяется отдельно для студентов и инструкторов (общей таблицы пользователей нет).
- `ddl-auto: update` используется для удобства разработки. В продакшене схему вели бы миграциями.

## Что не сделано

Дополнительные задания: Swagger, PDF-сертификаты, экспорт данных в CSV/JSON, импорт вопросов из JSON, фоновые задачи, статистика курсов, рейтинг и отзывы, изменение порядка уроков, результаты всех тестов, тесты.

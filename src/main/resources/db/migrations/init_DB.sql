CREATE TABLE roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    phone    VARCHAR(255) NOT NULL UNIQUE,
    role_id  UUID         NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE projects
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL UNIQUE,
    description TEXT         NOT NULL
);

CREATE TABLE tasks
(
    id              UUID PRIMARY KEY,
    task_code       VARCHAR(50)  NOT NULL UNIQUE,
    title           VARCHAR(255) NOT NULL,
    description     TEXT         NOT NULL,
    status          VARCHAR(50)  NOT NULL DEFAULT 'todo' CHECK ( status IN ('todo', 'in_progress', 'done') ),
    expiration_date DATE         NOT NULL,
    project_id      UUID         NOT NULL,
    user_id         UUID         NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE user_projects
(
    user_id    UUID NOT NULL,
    project_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE images
(
    id          UUID PRIMARY KEY,
    path        VARCHAR(255) NOT NULL,
    entity_id   UUID         NOT NULL,
    entity_type VARCHAR(255) NOT NULL
)
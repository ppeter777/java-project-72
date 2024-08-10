DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP,
    checkedAt TIMESTAMP,
    responseCode bigint
);

DROP TABLE IF EXISTS urlChecks;

CREATE TABLE urlChecks (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    urlId bigint REFERENCES urls(id),
    statusCode bigint,
    title text,
    h1 text,
    description text,
    createdAt TIMESTAMP
);
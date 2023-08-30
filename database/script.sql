create table users
(
    id       bigint auto_increment
        primary key,
    mail     varchar(256) not null,
    role     bigint       not null,
    password varchar(255) not null
)
    charset = utf8mb3;


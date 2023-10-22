-- liquibase formatted sql

-- changeset mmakart:1
create table weather (
    id bigint primary key auto_increment,
    weather_type varchar(255) not null
);

-- changeset mmakart:2
create index if not exists idx_weather_by_id on weather(id);

-- changeset mmakart:3
create index if not exists idx_weather_by_id_and_type on weather(id, weather_type);

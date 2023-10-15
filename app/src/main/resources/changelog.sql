-- liquibase formatted sql

-- changeset mmakart:1
create table city (
    id int primary key auto_increment,
    name varchar(255) unique not null
);

-- changeset mmakart:2
create table weather_type (
    id int primary key auto_increment,
    name varchar(255) unique not null
);

-- changeset mmakart:3
create table weather (
    id bigint primary key auto_increment,
    city_id int not null,
    weather_type_id int not null,
    foreign key(city_id) references city(id),
    foreign key(weather_type_id) references weather_type(id)
);

-- changeset mmakart:4
insert into weather_type (name) values ('Sunny');
insert into weather_type (name) values ('Cloudy');
insert into weather_type (name) values ('Rainy');
insert into weather_type (name) values ('Snowy');

-- changeset mmakart:5
insert into city (name) values ('London');
insert into city (name) values ('Paris');
insert into city (name) values ('Moscow');
insert into city (name) values ('Rome');
insert into city (name) values ('Athens');

-- changeset mmakart:6
insert into weather (city_id, weather_type_id) values (1, 3);
insert into weather (city_id, weather_type_id) values (2, 2);
insert into weather (city_id, weather_type_id) values (3, 4);
insert into weather (city_id, weather_type_id) values (4, 1);
insert into weather (city_id, weather_type_id) values (5, 1);

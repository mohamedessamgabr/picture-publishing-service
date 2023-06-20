drop database if exists picture_publishing_service;
create database picture_publishing_service;

use picture_publishing_service;

drop table if exists app_user;
create table app_user(
	id int not null auto_increment,
    email varchar(255) not null,
    password varchar(68) not null,
    
    primary key(id),
    unique(email)
);

drop table if exists role;
create table role(
	id int not null auto_increment,
    name varchar(255) not null,
    primary key(id)
);

drop table if exists user_role;
create table user_role(
	user_id int,
    role_id int,
    
    primary key(user_id, role_id),
    constraint user_fk foreign key(user_id) references app_user(id),
    constraint role_fk foreign key(role_id) references role(id)
);



drop table if exists picture;
create table picture(
	id int not null auto_increment,
    description varchar(255) not null,
    category varchar(255) not null,
	attachment varchar(255) not null,
    status varchar(255),
    
    primary key(id)
);


CREATE USER 'pps_user'@'localhost' IDENTIFIED BY 'PASSWORD';
GRANT ALL PRIVILEGES ON picture_publishing_service.* TO 'pps_user'@'localhost';
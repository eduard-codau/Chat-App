--create database unihub;
--use unihub;
drop table friend;
drop table friend_request;
drop table block_user;
drop table user_profile;
drop table message;
drop table chat;
drop table user;

create table user_profile(
    user_id int primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    status varchar(50),
    faculty varchar(70) not null,
    gender varchar(10) not null,
    birthday Date,
    avatar_path varchar(100),
    phone_number varchar(12)
);

create table user(
    user_id int AUTO_INCREMENT primary key,
    username varchar(50) not null unique,
    email varchar(30) not null unique,
    password varchar(100) not null,
    role varchar(30) not null
);

alter table user_profile add foreign key(user_id) references user(user_id); 

create table friend(
    friendship_id int auto_increment primary key,
    user1 int not null,
    user2 int not null
);
alter table friend add foreign key(user1) references user(user_id);
alter table friend add foreign key(user2) references user(user_id);


create table friend_request(
    friend_request_id int auto_increment primary key,
    from_user int not null,
    to_user int not null
);
alter table friend_request add foreign key(from_user) references user(user_id);
alter table friend_request add foreign key(to_user) references user(user_id);


create table block_user(
    block_request_id int auto_increment primary key,
    by_user int not null,
    to_user int not null
);

alter table block_user add foreign key(by_user) references user(user_id);
alter table block_user add foreign key(to_user) references user(user_id);


create table chat(
    chat_id int auto_increment,
    user_id int not null,
    primary key(chat_id,user_id)
);
alter table chat add foreign key(user_id) references user(user_id);


create table message(
    message_id int auto_increment primary key,
    chat_id int not null,
    from_user int not null,
    text varchar(255) default '',
    file_path varchar(100),
    sent_at DateTime not null
);
alter table message add foreign key(chat_id) references chat(chat_id);
alter table message add foreign key(from_user) references user(user_id);


drop table if exists beer cascade ;
drop table if exists customer cascade ;
create table beer (beer_style tinyint not null check (beer_style between 0 and 9), price numeric(38,2) not null, quantity_on_hand integer, version integer, create_date timestamp(6), update_date timestamp(6), id varchar(36) not null, beer_name varchar(50) not null, upa varchar(255) not null, primary key (id));
create table customer (version integer, created_date timestamp(6), last_modified_date timestamp(6), id varchar(36) not null, customer_name varchar(255), primary key (id));
drop table if exists beer;
drop table if exists customer;
create table beer (beer_style tinyint not null, price decimal(38,2) not null, quantity_on_hand integer, version integer, create_date datetime(6), update_date datetime(6), id varchar(36) not null, beer_name varchar(50) not null, upa varchar(255) not null, primary key (id)) engine=InnoDB;
create table customer (version integer, created_date datetime(6), last_modified_date datetime(6), id varchar(36) not null, customer_name varchar(255), primary key (id)) engine=InnoDB;

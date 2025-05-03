create extension if not exists postgis;

create schema if not exists awa;

create table if not exists awa.track (
    id varchar(32) primary key,
    account_id varchar(32) not null,
    device_id varchar(16) not null,
    device_type varchar(16) not null,
    started_at timestamp with time zone not null,
    created_at timestamp with time zone not null
);

create table if not exists awa.track_segment (
    id varchar(32) primary key,
    track_id varchar(32) not null,
    segment geometry(linestringz, 4326) not null,
    started_at timestamp with time zone not null,
    created_at timestamp with time zone not null,
    ord smallint not null
);
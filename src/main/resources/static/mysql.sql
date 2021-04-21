create table sys_log
(
    id             int auto_increment
        primary key,
    ip             varchar(32)  null,
    method_name    varchar(100) null,
    method_remark  varchar(100) null,
    request_params varchar(500) null,
    cost_time      bigint       null,
    create_time    datetime     null
);

create table url_info
(
    id         int auto_increment
        primary key,
    short_url  varchar(16)  null comment '短连接',
    origin_url varchar(500) null comment '原始连接'
);

create index url_info_short_url_index
    on url_info (short_url);


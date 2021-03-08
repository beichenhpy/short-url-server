-- url-info
create table "short-url".url_info
(
    id         bigserial not null
        constraint "url-info_pk"
        primary key,
    short_url  varchar(16),
    origin_url varchar(500)
);

comment on column "short-url".url_info.short_url is '短url';

comment on column "short-url".url_info.origin_url is '原始url';

alter table "short-url".url_info
    owner to beichen;

create index url_info_origin_url_index
    on "short-url".url_info (origin_url);
CREATE INDEX url_info_short_url_idx 
    ON "short-url".url_info USING btree (short_url);

-- sys_log
create table "short-url".sys_log
(
    id             bigserial not null
        constraint sys_log_pk
            primary key,
    ip             varchar(32),
    method_name    varchar(100),
    method_remark  varchar(100),
    request_params varchar(500),
    cost_time      bigint,
    create_time    timestamp
);

comment on column "short-url".sys_log.ip is '访问者ip';

comment on column "short-url".sys_log.method_name is '方法名';

comment on column "short-url".sys_log.method_remark is '方法备注';

comment on column "short-url".sys_log.request_params is '请求参数';

comment on column "short-url".sys_log.cost_time is '执行时间';

comment on column "short-url".sys_log.create_time is '创建时间';

alter table "short-url".sys_log
    owner to beichen;



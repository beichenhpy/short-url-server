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

﻿-- PostgreSQL用メッセージマスタテーブル作成DDL
create table MT_MSG (
  MESSAGE_KIND character varying(1) not null
  , MESSAGE_ID integer not null
  , MESSAGE character varying(50)
  , constraint MT_MSG_PKC primary key (MESSAGE_KIND,MESSAGE_ID)
) ;

comment on table MT_MSG is 'メッセージマスタ';
comment on column MT_MSG.MESSAGE_KIND is 'メッセージ種類';
comment on column MT_MSG.MESSAGE_ID is 'メッセージID';
comment on column MT_MSG.MESSAGE is 'メッセージ';

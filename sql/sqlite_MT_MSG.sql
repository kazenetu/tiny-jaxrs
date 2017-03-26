-- SQLite用メッセージマスタテーブル作成DDL
create table MT_MSG (
  MESSAGE_ID character varying(5) not null
  , MESSAGE character varying(50)
  , constraint MT_MSG_PKC primary key (MESSAGE_ID)
) ;


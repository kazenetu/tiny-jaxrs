-- PostgreSQL用ユーザーテーブル作成DDL
create table MT_USER (
  USER_ID VARCHAR(30)
  , NAME VARCHAR(30)
  , PASSWORD VARCHAR(30)
  , DATE_DATA date
  , TIME_DATA time without time zone
  , TS_DATA timestamp without time zone
  , constraint MT_USER_PKC primary key (USER_ID)
) ;

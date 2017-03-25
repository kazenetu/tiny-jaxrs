-- SQLite用ユーザーテーブル作成DDL
create table MT_USER (
  USER_ID NVARCHAR
  , NAME NVARCHAR
  , PASSWORD NVARCHAR
  , DATE_DATA date
  , TIME_DATA time without time zone
  , TS_DATA timestamp without time zone
  , primary key (USER_ID)
);

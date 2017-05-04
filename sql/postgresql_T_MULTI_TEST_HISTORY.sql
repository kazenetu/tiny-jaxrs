-- 複数モデル登録テスト履歴
create table T_MULTI_TEST_HISTORY (
  ID character varying(10) not null
  , HISTORY_NO integer not null
  , NAME character varying(100)
  , constraint T_MULTI_TEST_HISTORY_PKC primary key (ID,HISTORY_NO)
) ;

comment on table T_MULTI_TEST_HISTORY is '複数モデル登録テスト履歴';
comment on column T_MULTI_TEST_HISTORY.ID is 'ID';
comment on column T_MULTI_TEST_HISTORY.HISTORY_NO is '履歴No';
comment on column T_MULTI_TEST_HISTORY.NAME is '名前';

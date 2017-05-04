-- 複数モデル登録テスト
create table T_MULTI_TEST (
  ID character varying(10) not null
  , NAME character varying(100)
  , constraint T_MULTI_TEST_PKC primary key (ID)
) ;

comment on table T_MULTI_TEST is '複数モデル登録テスト';
comment on column T_MULTI_TEST.ID is 'ID';
comment on column T_MULTI_TEST.NAME is '名前';

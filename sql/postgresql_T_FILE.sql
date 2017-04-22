-- ファイルテーブル
create table T_FILE (
  ID integer not null
  , IMAGE_DATA character varying(1000000)
  , FILE_NAME character varying(50)
  , TAG character varying(8)
  , constraint T_FILE_PKC primary key (ID)
) ;

comment on table T_FILE is 'ファイルテーブル';
comment on column T_FILE.ID is 'ID';
comment on column T_FILE.IMAGE_DATA is 'BASE64ファイルデータ';
comment on column T_FILE.FILE_NAME is 'ファイル名';
comment on column T_FILE.TAG is 'タグ情報';


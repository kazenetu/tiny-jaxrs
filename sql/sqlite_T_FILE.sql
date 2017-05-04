-- ファイルテーブル
create table T_FILE (
  ID integer not null
  , IMAGE_DATA character varying(1000000)
  , FILE_NAME character varying(50)
  , TAG character varying(8)
  , constraint T_FILE_PKC primary key (ID)
) ;


﻿-- ファイルテーブル
create table T_FILE (
  ID INTEGER not null
  , IMAGE_DATA CHARACTER VARYING
  , FILE_NAME CHARACTER VARYING
  , TAG CHARACTER VARYING
  , TIME_DATA CHARACTER VARYING
  , primary key (ID)
);
https://www.popit.kr/mysql-index-column-size-too-large-error/ 

기본 설정에서는 index 크기가 최대 767 bytes  까지만 가능합니다. utf8 charset의 경우 1글자가 3byte, utf8mb4의 경우 4byte로 계산되기 때문에 varchar(255),  utf8mb4를 사용할 경우 255 * 4 byte = 1020 byte가 필요하기 때문에 index의 최대 크기를 초과하여 위와 같은 에러 메시지가 나타납니다.
자체 개발하는 프로젝트의 경우라면 컬럼 길이를 줄이거나 하는 방법을 사용할 수 있지만 오픈 소스로 배포되는 솔루션을 설치할 경우 함부로 수정할 경우 오동작을 할 수 있기 때문에 길이를 조절하는 방법이 해결책이 될 수 없습니다.
위 mysql 문서에 보면 index  크기를 늘릴 수 있는 방법이 있는데 'innodb_large_prefix'  를 on 하면 가능하다고 되어 있습니다. 하지만 실제로 해보면 적용되지 않는데 다음과 같은 두가지 설정을 추가 해야 합니다.

 1. database level 에서 innodb_file_format=BARRACUDA
 2. table level 에서 ROW_FORMAT=DYNAMIC 또는 ROW_FORMAT=COMPRESSED 사용

set global innodb_large_prefix = ON;
set global innodb_file_format = BARRACUDA;
CREATE TABLE test2 (
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_test_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DELIMITER $$
create trigger trigger_members after insert
on members
for each row begin
	insert into members_authorities(members_idx, auth_name) values(NEW.idx, 'ROLE_MEMBER');
end$$
DELIMITER ;
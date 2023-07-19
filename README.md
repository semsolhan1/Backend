
# HandiFarm Backend
HandyFarm Backend Git Repository.

# DB 관련 설정
1. MySQL Workbench -> root 계정으로 접속.
2. 디스코드 DB 및 Key 채널에 sql 문 실행.
3. 워크벤치 홈에서 새로운 커넥션 생성.
    1. Connection Name : HandiFarm (변경 가능)
    2. Username : handi
    3. password -> Store in Valut... -> farm -> OK
    4. Default Schema : handifarm
    5. Test connection -> 연결 정상적인지 확인 후 OK -> OK
4. 생성된 커넥션(Connection Name에서 설정한 이름)으로 접속.
5. 각 테이블 생성문 실행.

# Key
 - DB 및 Key 채널에서 keys.yml 파일 다운 후 classpath:/ 경로(src/main/resources)에 넣기.
 - API 사용 시 쓰이는 키 값들은 해당 파일에 입력 후 최신화해서 조원들과 공유해주세요. gitignore에 등록되어 있습니다.

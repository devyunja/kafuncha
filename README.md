# kafuncha

### API Endpoints

Test (테스트용)

- GET https://programming.coffee/frontend-test

Fetch entire history (전체 채팅 내역, 오름차순)

- GET https://programming.coffee/history

Fetch pages history (페이지별 채팅 내역, 내림차순)

- GET https://programming.coffee/history-paged
- params: [page: Int, offset: Int]

Attendance (유저별 전채 채팅 횟수)

- GET https://programming.coffee/attendance

Daily chat Count (일별 채팅 횟수)

- GET https://programming.coffee/daily-chat-count

Daily champion (일별 가장 채팅횟수가 많았던 유저)

- GET https://programming.coffee/daily-champion

Links in chat (채팅방 링크모음)

- GET https://programming.coffee/links-in-chat

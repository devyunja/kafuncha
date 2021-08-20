# kafuncha

### 프론트엔드 프로젝트 노션

> https://busy-mandrill-84b.notion.site/Kafuncha-579e37e48abd4a9887fb0a78e7f518bb

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

척살리스트

- GET https://programming.coffee/prune

오늘의 키워드

- GET https://programming.coffee/today-keyword

날짜별 키워드

- GET https://programming.coffee/keyword-date
- params: [date: String] yyyy-MM-dd

날짜별 키워드 - 한번에 여러날

- GET https://programming.coffee/keyword-dates
- params: [date: List[String]] yyyy-MM-dd

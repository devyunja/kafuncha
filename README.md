# kafuncha

### 프론트엔드 프로젝트 노션

https://busy-mandrill-84b.notion.site/Kafuncha-579e37e48abd4a9887fb0a78e7f518bb

### API base URL: https://programming.coffee

> GET /frontend-test

Test (테스트용)

> GET /history

Fetch entire history (전체 채팅 내역, 오름차순)

> GET /history-paged

Fetch pages history (페이지별 채팅 내역, 내림차순)

- params: [page: Int, offset: Int]

> GET /attendance

Attendance (유저별 전채 채팅 횟수)

> GET /daily-chat-count

Daily chat Count (일별 채팅 횟수)

> GET /daily-champion/:filename

Daily champion (일별 가장 채팅횟수가 많았던 유저)

> GET /links-in-chat

Links in chat (채팅방 링크모음)

> GET /prune

Prune member list (척살리스트)

> GET /today-keyword

Today's keyword (오늘의 키워드)

> GET /keyword-date

Keywords by a date

- params: [date: String] yyyy-MM-dd

> GET /keyword-dates

Keywords by dates
  
- params: [date: List[String]] yyyy-MM-dd

> POST /upload

Uploading CSV file (채팅 파일 업로드)

- multipart-form data: chat_history

> GET /current-member

Get current chat member list

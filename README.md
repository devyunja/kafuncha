# kafuncha

### 프론트엔드 프로젝트 노션

https://busy-mandrill-84b.notion.site/Kafuncha-579e37e48abd4a9887fb0a78e7f518bb

### API base URL: https://programming.coffee

### GET /history/{filename}

- Fetches entire chat histories

- Params:

| Key   | Type    | Required   | default   | format |
| :---: | :-----: | :--------: | :-------: | :----: |
| desc  | boolean | false      | false     | N/A    |

### GET /attendance

- Total chat count by user

### GET /daily-chat-count/{filename}

- Total chat count by date

### GET /daily-champion/{filename}

- Top user by date

### GET /daily-champion-rank/{filename}

- Users with rank by date
- Params:

| Key           | Type    | Required   | default   | description             |
| :-----------: | :-----: | :--------: | :-------: | :---------------------: |
| rewindNumDays | int     | false      | N/A       | how many days to rewind |

- If you don't pass the **rewindNumDays**, the result will contain all data from the beginning of the history.

### GET /links-in-chat

- Links from the chat

### GET /prune/{filename}

- Users with data for pruning

### GET /today-keyword

- Today's keyword list

### GET /keyword-date

- Keyword list by date
- Params:

| Key   | Type    | Required   | default   | format     |
| :---: | :-----: | :--------: | :-------: | :--------: |
| date  | string  | true       | N/A       | yyyy-mm-dd |

### GET /keyword-dates

- Keyword list by date list
- Params:

| Key   | Type      | Required   | default   | format     |
| :---: | :-----:   | :--------: | :-------: | :--------: |
| date  | [string]  | true       | N/A       | yyyy-mm-dd |

### POST /upload

- Chat history CSV file uploading
- **multipart-form data**: chat_history

### GET /current-member/{filename}

- Current members

### GET /mention/{filename}

- Mentioned users by date

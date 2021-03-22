# Cron Job Email
A application using Quartz Spring Boot to schedule send mail

## Installation & Run
To configuration application let edit ***application.properties*** file in resources/application.properties
To creating table in database let go to the ***CreatTableIfNotExist.sql*** file copy schema and execute it in postgresql


#### API Endpoint
Start schedule
```bash
http://localhost:8081/api/timer/start-job
```
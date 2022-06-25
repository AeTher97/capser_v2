# Capser V2

Second version of caps games tracking service. With revamped interface and backend. New version allows hosting tournaments and improved statistics.
This version currently powers [Global Caps League official site](https://globalcapsleague.com).

## Startup

To lanuch applicaiton in local enviroment use maven to start CapserV2Application.java and node js to start frontend start package.json script.

### Environmental variables

For local configuration PostgreSQL database needs to be provided. Credentials should be set as correct environmental variables when starting backed service:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD

Also secret for generating jwt secrets and automatic acceptance cron need to be set:

- JWT_SECRET
- CRON_EXPRESSION

For email communication with users email env must be set for gmail account:

- EMAIL_USERNAME
- EMAIL_PASSWORD

## Eureka and gateway are legacy packages from kubernetes hosting.

# Lazybot

Lazybot is a bot manager for Minecraft

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them :

```
- Java 8
- Maven
```

Which ports need to be free :
```
- 8080 (webapp web server)
- 8090 (webapp socket server)
- 9090 (master socket server)
- 9091 (mission socket server)
```

### Installing

To install the project you just need to clone this repo.

```
git clone https://github.com/Ronflonflon/lazybot.git
```

End with an example of getting some data out of the system or using it for a little demo

## Deployment
The deploy the whole project, you need to deploy the three microservices. To do that go in "lazybot-microservices directory".
Inside it, run :
```
mvn package
```
### Deploy Master MS
From the root directory of the project, go in "lazybot-master/lazybot-master-api/target" directory, and run :
```
java -jar lazybot-master-api-1.7-RELEASE.jar
```
### Deploy Webapp MS
From the root directory of the project, go in "lazybot-webapp/target" directory, and run :
```
java -jar lazybot-webapp-api-1.7-RELEASE.jar
```
### Deploy Mission MS
From the root directory of the project, go in "lazybot-api/lazybot-api-api/target" directory, and run :
```
java -jar lazybot-mission-api-1.7-RELEASE.jar
```

## Documentations
[Documentation : "Lazybot - Servers communication"](https://github.com/Ronflonflon/lazybot/blob/master/doc/Lazybot%20-%20Servers%20communication.pdf)

## License
[MIT license](https://github.com/Ronflonflon/lazybot/blob/master/LICENSE.txt)
# ChatRoom
App for communicating via webSocket. You can send basic chat message or "@weather city" to get current weather from given city.

For local run please specify, in your application.yaml profile:
application:
  weather:
    url: http://localhost:8081/weather
Url is for location where your weather service is running.


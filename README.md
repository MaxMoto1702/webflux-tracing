# webflux-tracing

## Цель

при выполнении зпроса: 

```http request
POST localhost:8080/trips/generate
Content-Type: application/json
X-Request-ID: test-request-id-2

{
  "cityId": "new-york"
}
```

получить такой резульат в логе:

```log
2022-05-14 18:47:29.970 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 9
2022-05-14 18:47:29.980 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Build trip Trip(city=City(id=new-york, name=City #new-york), places=[Place(id=3, name=Place #3), Place(id=2, name=Place #2), Place(id=0, name=Place #0), Place(id=4, name=Place #4), Place(id=5, name=Place #5), Place(id=1, name=Place #1), Place(id=7, name=Place #7), Place(id=6, name=Place #6), Place(id=9, name=Place #9), Place(id=8, name=Place #8)], routes=[])
2022-05-14 18:47:30.000 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 0
2022-05-14 18:47:30.001 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 1
2022-05-14 18:47:30.001 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 2
2022-05-14 18:47:30.002 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 3
2022-05-14 18:47:30.003 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 4
2022-05-14 18:47:30.003 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 5
2022-05-14 18:47:30.004 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 6
2022-05-14 18:47:30.005 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 7
2022-05-14 18:47:30.007 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 8
2022-05-14 18:47:30.007 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get place 9
2022-05-14 18:47:30.010 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=0, name=Place #0) and Place(id=1, name=Place #1)
2022-05-14 18:47:30.012 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=1, name=Place #1) and Place(id=3, name=Place #3)
2022-05-14 18:47:30.013 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=3, name=Place #3) and Place(id=2, name=Place #2)
2022-05-14 18:47:30.014 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=2, name=Place #2) and Place(id=4, name=Place #4)
2022-05-14 18:47:30.016 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=4, name=Place #4) and Place(id=5, name=Place #5)
2022-05-14 18:47:30.018 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=5, name=Place #5) and Place(id=6, name=Place #6)
2022-05-14 18:47:30.019 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=6, name=Place #6) and Place(id=7, name=Place #7)
2022-05-14 18:47:30.020 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=7, name=Place #7) and Place(id=8, name=Place #8)
2022-05-14 18:47:30.021 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.TripController   : Get route between Place(id=8, name=Place #8) and Place(id=9, name=Place #9)
2022-05-14 18:47:30.040 [test-request-id-2]  INFO 23324 --- [ctor-http-nio-2] net.serebryansky.nology.RequestIdFilter  : POST http://localhost:8080/trips/generate
```

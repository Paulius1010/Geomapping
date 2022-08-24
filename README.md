To start this application it is necessary to have  postgres database and update application.properties:
e.g.
spring.datasource.url=jdbc:postgresql://localhost:5432/maps
spring.datasource.username=postgres
spring.datasource.password=psw

To import data from csv file, it is necessary to have a file in project folder, set a name, location and valid Google API KEY in application.properties:
e.g.
your.file.location=src/main/resources/500_europe_cities.csv
google.maps.apiKey=your_api_key_jdabadbchjsdvcjd

When application is tarted, answer to console questions.
It asks to delete old entries if some exists.
It is possible to import data using single or multi threading.

# Angular

Create Angular project + copy code
ng new parcel-management-frontend --routing --style=css
# IMPORTANT: when it asks "Use standalone components?" choose "No"
cd parcel-management-frontend

npm i bootstrap


Add Bootstrap to angular.json → styles:

"styles": [
  "src/styles.css",
  "node_modules/bootstrap/dist/css/bootstrap.min.css"
]


Now replace your project’s src/ with the downloaded src/ folder contents.

Run:

ng serve


Frontend: http://localhost:4200


Run backend
# Java 17+ and Maven required
cd parcel-management-backend
mvn spring-boot:run

H2 Console

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:parceldb

username: sa, password: (blank)

Default Officer account (seeded)

Email: officer@parcel.com

Password: officer123






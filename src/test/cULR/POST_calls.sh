#!/bin/bash

# 1) POST HTML Form Data
curl -X POST  -H "Content-Type: application/json" -d @user.json -i http://localhost:8080/api/user/add/m1
curl -X POST -H "Content-Type: application/json" -d '{"lastName":"Thach","firstName":"Billy","email":"billy.thach@zenika.com"}' -i http://localhost:8080/api/user/add/m1

# 2) Send a Request Body : Mapping par argument
curl -X POST -i "http://localhost:8080/api/user/add/m2?lastName=Thach&firstName=Billy&email=billy.thach@zenika.com"
curl -X POST -d "lastName=Thach&firstName=Billy&email=billy.thach@zenika.com" -i http://localhost:8080/api/user/add/m2

# 3) Send a Request Body using Enum : Mapping par argument
curl -X POST -d "lastName=Thach&firstName=Billy&email=billy.thach@zenika.com&userType=ADMIN" -i http://localhost:8080/api/user/add/m3

# 4) Send a Request Body : Mapping Objet
curl -X POST -d "lastName=Thach&firstName=Billy&email=billy.thach@zenika.com&userType=ADMIN" -i http://localhost:8080/api/user/add/m4
curl -X POST -d -i "http://localhost:8080/api/user/add/m4?lastName=Thach&firstName=Billy&email=billy.thach@zenika.com&userType=ADMIN"

# 5) POST HTML Multipart / File Forms
curl -X POST  -H "Content-Type: multipart/form-data"  -F "userFile=@user.json" -F "status=IMPORT" -i http://localhost:8080/api/user/add/m5

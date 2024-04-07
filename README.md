# weater_service
A web application to get weather details

# Technology
- Java 17
- MongoDB
- RestTemplate
- SpringBoot 3.2.4

# Steps to Authorize
- Create a new user with username and password.
- The JWT token will be received in the API response.
- Use this token, in the headers to fetch weather details
- If the token has expired, use the refresh token curl provided in the postman collection

# Postman Collection Link
https://drive.google.com/file/d/1ihsL9YQJYhaXLW_-XyJ4om_FaoWZJntw/view?usp=sharing

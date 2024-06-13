# car-rental
Application for managing car rental company. 
**Technologies in use**: Java 17, Spring Boot 3.0.6, PostgreSQL, Hibernate, JPA, React.js. 

1. **About**
   Monolithic architecture application used to manage a car rental company. Two types of user accounts: customer and employee. Authorisation is done by Spring Security and JWT.  Employee panel allows to add new cars, manage existing cars. A history of price changes is maintained in the price updates table. Employee is able to generate reports in pdf or excel format.

3. **API endpoints**
   Here we can see endpoints in swagger-ui.
![image](https://github.com/jakubZp/CarRental/assets/92670078/020deae7-7a38-43ea-aca2-d6cea88a96bc)
![image](https://github.com/jakubZp/CarRental/assets/92670078/2af2123e-e8d5-490a-90d9-60871726aea6)
![image](https://github.com/jakubZp/CarRental/assets/92670078/736d2248-e7d1-499b-9487-a1fc6dd6b330)

4. **Database diagram**
   ![image](https://github.com/jakubZp/CarRental/assets/92670078/96fb021e-9f0a-44c3-8c8d-5ccb88662c05)

5. **To do**
   - extend frontend design
   - add email recovery

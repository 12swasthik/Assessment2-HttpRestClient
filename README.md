# HttpRest!

An application built using java servlets to connect and fetch data from [Reqres.in](https://reqres.in/)

# Running the application

Download and setup tomcat to connect to application, Java version 1.8 or above and maven.
Download the dependencies.
Run the application

## URL endpoints

**/search?q=\$query&page=\$pageNumber** to search for the query in name or in email, pageNumber is used if there are many results

**/pages?page=\$pageNumber** to get a list of all users, pageNumber is used for getting a limited number of users on one page


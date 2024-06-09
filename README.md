
#EPIC Learning Tool Web App
##How to run
Backend and Front end are run separately. Backend is on Spring Boot, 
while front end is on ReactJS. To run the web app, Run the back end and front end 
then access the web app through the front end - at present, localhost:8081.

Before running the frontend, run the 'npm install' command in the terminal, this installs the 
frontend dependencies. You can then run 'npm start' to deploy the frontend of the project.

Before running the backend, either fill in the empty fields in the SSHConnection class in
com.team32.epicwebapp.services or delete/comment both SSHConnection and MyContextListener entirely.
If not, the application will not run.

##Connecting to the database
Before doing any of this, please fill in the SSHConnection class.
1. **Connecting to the database from within the University Campus.**  
Instructions to connect to the university database is within the application.properties file.
   Comment and un-comment the relevant lines.
     

2. **Connecting to the database from outside of campus**  
To connect to the database from outside of campus, there are a few steps.
   First, follow the instructions in the application.properties file and comment/uncomment 
   the relevant lines. Next, add the relevant fields in the SSHConnection class in the package
   com.team32.epicwebapp.services. 
    
 
3. **Connecting to a docker image**
In the event the first 2 options does not work, connecting to a docker container
   can also be done. In the parent folder of the entire project there is a database folder
   which contains the data.sql file and the dockerfile. Build the image, run the container and
   comment/uncomment the relevant lines in the application.properties file.

##Signing in
The only account available for use at the point of first run is an admin account,
accessible through the credentials [username: admin] and [password: password]. The password
can be changed later.

For future users (Staff and Student) added by the admin, their credentials will be provided
to them by the admin. Upon first log in, they will be required to change their password.
Passwords can also be changed later.


##ADMIN PRIVILEGES
1. Adding and removing users.
2. Changing password.

##STAFF PRIVILEGES
1. Adding announcements (General, Stage or Module announcements)
2. Viewing announcements that they post.
3. Adding modules
4. Adding students to modules.
5. Viewing all students registered to a module.
6. Adding Results for students.
7. Viewing student result graph (Module average across all students, or individual student results.)
8. Changing password.

##STUDENT PRIVILEGES
1. Viewing announcements for them
2. Viewing their results - by module, or their stage average.
3. Changing Password.
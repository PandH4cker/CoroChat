# CoroChat
---
## Authors: <br /> <br /> Raphael Dray, Thierry Khamphousone <br /> 
##### Alexandre De Sevin, Diane Martin, Eythan Dahan, Boris Houessou
---
> This program is a desktop application. <br />
> It integrates the client and the server which are communicating each other through HTTP Routes. <br />
> The client fetches and posts data from/to the server which returns JSON-Encoded responses.
> The client is a Whatsapp-Like application.

> It's written in __Java programming language including some libraries like so:__
+ [JDBC Oracle DB](https://www.oracle.com/fr/database/technologies/appdev/jdbc.html)
+ [Java FX 11](https://openjfx.io/)
+ [JUnit 5](https://junit.org/junit5/)
+ [Animate FX](https://typhon0.github.io/AnimateFX/)
+ [JBcrypt](https://www.mindrot.org/projects/jBCrypt/)
+ [Gson](https://github.com/google/gson)

---

## Note: 
> To be able to launch the LoginView, you must configure javaFX <br />
> Go to:  Run > Edit configuration > LoginView > VM option

> Inside the VM option text zone, add the location of the javaFX library as the example below:

    --module-path \
    /Path/To/javafx-sdk-11.0.2/lib \
    --add-modules \
    javafx.controls,javafx.fxml

---
## Last Release Version: 0.0.8
### Changelog:
> #### Version 0.0.8
> Refactored modules between client and server.
---
> #### Version 0.0.7
> Added Unit Tests. <br />
---
> #### Version 0.0.6
> Added Logger interface with its implementation for logging information as well as in console and in a file stored on the server/client.
---
> #### Version 0.0.5
> Added JavaDoc inside all the classes
---
> #### Version 0.0.4
> Added client commands for deleting messages. <br />
> Added popup for informing the user when error on the login view. <br />
> Added the Message model for storing messages in the database. <br />
> Added MessageRepository to the AbstractCorochatDatabase. <br />
> Added Dao and its helpers for communicating with the message table.
> Added the button over our own message for deleting them. <br />
> Added methods inside the chat controller for interacting with the other users in case of connecting, deleting, sending actions.
---
> #### Version 0.0.3
> Refactored design colors from linear gradient violet to darkblue. <br />
> Added support for OS validations. <br />
> Added second view ChatView and its ChatController for performing action on the Chat view. <br>
> Refactored LoginController and added connection between the server and the view. <br />
> Refactored MultiThreadedServer class. <br />
> Refactored classes in the data package. <br />
> Added new Index for handling pseudo duplication in the database. <br />
---
> #### Version 0.0.2
> Added UserDao interface containing abstract sql methods to interact with the DB. <br />
> Added DataUserName class containing the names of the user columns and the table name. <br />
> Added AbstractCorochatDatabase defining abstract dao method and connecting the server to the Oracle DB by the Singleton design pattern. <br />
> Added UserRepository in order to interact asynchronously with the Oracle DB for the user table. <br />
> Implemented UserDao and AbstractCorochatDatabase:
> * UserDaoImpl implements UserDao sql methods to interact with the Oracle DB user table.
> * CorochatDatabase extends AbstractCorochatDatabase with a Singleton design pattern and use himself as an implementation for instantiating a database.
---
> #### Version 0.0.1
> Added LoginView fxml created with SceneBuilder 2.0, the design of the Login view. <br />
> Added Utils to handle email validation and password complexity and helpers for JavaFX. <br />
> Added UserModel to instantiate it in further.
> Added LoginView JavaFX java class screen.
> Added LoginController to handle actions on the LoginView.
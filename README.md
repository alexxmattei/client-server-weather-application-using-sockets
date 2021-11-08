# client-server-chat-using-sockets
Client Server Application for Web Technologies class of Year 3.

Server accepts multiple clients in the socket connection and exposes PORT 1234.
Client connects to PORT 1234 and once he specifies a role that he authenticates with.
After the user authenticates, the client decides the flow that he has access to and the server checks their credentials with every request. 
A Cookie and a Session is simulated every time a Client is instanciated.

When the application is first started up, the Client has the role NONE.
Then the client is asked to authenticate and is given the role CLIENT or ADMIN upon request. 

A CLIENT can: 
1. Make requests regarding the weather conditions in a city by providing the coordinates. 
2. Logout. 
3. Close the application. 

An ADMIN can: 
1. Make a request to see the entire record of cities in JSON format. 
2. Upload a new JSON file with updated coordinates/temperatures. 
3. Log out. 
4. Close the application. 

The admin can change the JSON file by adding a new one (persistence layer is immutable) in one line. 
It has been tested by using an online JSON formatter.
The admin can quit the JSON file addition by typing 'quit' - this has been added as a backup feature.

The Client uses an AuthManager that is thread safe to log in and send stateful data to the Server. 
The Server allocates a new Thread for each client with keeping a record of each client's state and request. 

Dependencies: 

GSON for parsing json and saving data in the persistance layer 
>> pom.xml 

    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.9</version>
        </dependency>
    </dependencies>

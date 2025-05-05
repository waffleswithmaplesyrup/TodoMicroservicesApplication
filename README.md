## Microservice Example - Todo Microservice Application

There are 4 modules in this project:
1. **Service Registry:** serves as a centralized database or directory where information about available services and their locations is stored and maintained. All active microservices will be displayed in the registry: [http://localhost:8761](http://localhost:8761).
2. **API Gateway:** serves as a centralized entry point for managing and routing requests from clients to the appropriate microservices or backend services within a system.
3. **Auth Microservice:** manages all user authentication and authorisation requests. All other microservices need to "talk" to this microservice if they need any information on users.
4. **Todo Microservice:** manages all todo requests. This microservice communicates with the Auth Microservice often to check for user authorisation before manipulating data.

After cloning the repo:

Set up all 4 microservices run configuration like this.
<img src="https://raw.githubusercontent.com/waffleswithmaplesyrup/TodoMicroservicesApplication/refs/heads/main/assets/RunConfiguration.png">

For AuthMicroservice and TodoMicroservice, add secrets and passwords in the configuration environment like this.
<img src="https://raw.githubusercontent.com/waffleswithmaplesyrup/TodoMicroservicesApplication/refs/heads/main/assets/ENV.png">

Lastly, create a new compound configuration and add all 4 modules in the configuration. This allows you to run all 4 modules together at the same time without having to run each module individually.
<img src="https://raw.githubusercontent.com/waffleswithmaplesyrup/TodoMicroservicesApplication/refs/heads/main/assets/Compound.png">

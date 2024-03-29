# Interstellar Transport System

An interstellar transport system used by Earth inhabitants to find the shortest path to other planets

**How to run**
<br/>
The project is spring-boot and can be started by running maven command: "spring-boot:run" After starting it up on your web browser navigate to localhost:8080

Implemented by having three main 'components'

The database layer
The service implementation
The Controller layer

**Database layer**<br/>
DAO classes are utilised for the CRUD and they are accessed via an interface. This helps if in future there is need to pull this layer out and have it as a separate deployable module.
Future consideration: The DAO layer can be factoried to reduce the code and also make common functionality manageable. Though a decision was made to have DTOs, the DAo beans are 
accessible from the frontend for the CRUD functions

**Service implementation**<br/>
This is layered but essentially there are two main service groups; the CRUD services (DaoManagerServices) and the shortest path calculator (ShortestPathService). The 'data loader' 
(ExcelDataHandler) is also lumped up here though I prefer to classify it as a utility

**Controller layer**<br/>
Is reponsible for request mapping. Can be extended to add filtered service mappers


**Considerations**<br/>
1. The project can be chopped up into separate modules if need be (DB layer and the enabling service interfaces)
2. The ExcelDataHandler can be extended to cater for different file types.

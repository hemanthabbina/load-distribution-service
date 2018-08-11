# load-distribution-service
## Problem 

Client wants to distribute tasks/workload across multiple vendors. Solution needs to take input as 
    Total number of tasks to be distributed across multiple vendors
    Number of Vendors (EX: A, B, C…)
    % distribution of tasks across vendors ( Ex: 30,40,40…..)
And output will be task distribution across multiple vendors ( Ex: Vendor A got 300 tasks, B got 35 tasks…) based on the allocated percentages.

#### Constraints
    0<Number of tasks<n
    0<Number of Vendors<m
    0<percentage allocation for each vendor<100
    Total percentage of all vendors must be 100
    All the tasks need to be distributed across vendors.
    Task cannot be split. 
#### Note

    Application need to be developed using Spring boot/Spring MVC.
    Make sure the code written is as per the industry standards following Java Naming conventions, design patterns and best practices.
    Code should be, maintainable and readable.
    Expose a rest service which takes total  number of tasks to be distributed, number of vendors, percentage distribution for vendors and returns data as per percentage distribution.
    
## Solution
The solution tries to distribute the tasks as per the allocation percentages. In this process, there is a possibility
that some of the tasks might left unallocated (as we can't split the task). So, to distribute the remaining unallocated tasks,
the solution seeks a strategy. There are three strategies defined for this purpose (prefer big vendor, prefer small vendor, 
prefer vendor with highest remaining capacity), default being the prefer vendor with highest remaining capacity.

#### Technologies
    Spring Boot 2.0.4 (with embeded Tomcat)
    Maven
    Junit4 (Embeded with Spring-boot-test)
    
#### API
This webservice exposes a REST API POST /load-distribution-service/distribute to allocate the tasks among vendors. This API seeks the inputs in the body of the request and an optional query parameter 'strategy-type'.
Swagger documentation is available at http://localhost:8080/swagger-ui.html

##### strategy-type
    PREFER_BIG
    PREFER_SMALL
    PREFER_HIGHEST_REMAINING
    
##### Sample API Call

Post Url http://localhost:8080/load-distribution-service/distribute?strategy-type=PREFER_HIGHEST_REMAINING

Request Body
`{
  "tasks" : "11",
  "distribution" : [
	  {"vendor": "A", "distribution_percent":30},
	  {"vendor": "B", "distribution_percent":40},
	  {"vendor": "C", "distribution_percent":30}
  ]
}`

###### Output
`[{"vendor":"B","num_tasks":5},{"vendor":"A","num_tasks":3},{"vendor":"C","num_tasks":3}]`


##### Unit Tests
Tests can be run using 'mvn test' or from any IDE.

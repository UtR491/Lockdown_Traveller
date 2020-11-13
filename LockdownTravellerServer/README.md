# Index
* [Build and Run Commands](#build-and-run-commands)
* [List of tables](#list-of-tables);
* [Description of tables](#description-of-tables);
   * [Admin](#admin);
   * [Basic_Train_Info](#basic_train_info)
   * [Booking_Info](#booking_info)
   * [Notification](#notification)
   * [Route_Info](#route_info)
   * [User](#user)
   * [Vacancy_Info](#vacancy_info)

# Build and Run Commands.

    ~/checker-framework-3.7.0/checker/bin/javac -processor nullness *.java -d ./bin
    
    java -classpath ~/Lockdown_Traveller/LockdownTravellerServer/src/bin/:/usr/share/java/mysql-connector-java-8.0.21.jar Server

# List of tables.

| Tables          | Needed in minimal version?|
|:---------------:|:-------------------------:| 
|Admin            |                     Yes   | 
|Basic_Train_Info |                    Yes    | 
|Booking_Info     |                   Yes     | 
|Notification     |                  Yes      | 
|Platform_No      |                 No        | 
|Route_Info       |                Yes        | 
|User             |              Yes          | 
|Vacancy_Info     |               Yes         | 


# Description of tables.

#### Admin
    Stores information related to admin login.

| Field    | Type        | Null | Key | Default | Extra |
|----------|-------------|------|-----|---------|-------|
| Username | varchar(20) | NO   |     | NULL    |       |
| Password | varchar(64) | NO   |     | NULL    |       |
| Admin_ID | int         | NO   |     | NULL    |       |


#### Basic_Train_Info

    Stores basic information about the train, like its name, number of different
    types of coaches and number of seats in them, fare for each type of coach,
    days on which the trains run.

| Field            | Type        | Null | Key | Default | Needed in minimal version? |
|------------------|-------------|------|-----|---------|:-----:|
| Train_ID         | varchar(5)  | NO   | PRI | NULL    |       |
| Train_Name       | varchar(30) | NO   |     | NULL    |       |
| Days_Running     | varchar(7)  | NO   |     | NULL    |       |
| Cancelled_Till   | date        | YES  |     | NULL    |       No|
| FirstAC_Coaches  | int         | YES  |     | NULL    |       |
| SecondAC_Coaches | int         | YES  |     | NULL    |       |
| ThirdAC_Coaches  | int         | YES  |     | NULL    |       |
| Sleeper_Coaches  | int         | YES  |     | NULL    |       |
| FirstAC_Seats    | int         | YES  |     | NULL    |       |
| SecondAC_Seats   | int         | YES  |     | NULL    |       |
| ThirdAC_Seats    | int         | YES  |     | NULL    |       |
| Sleeper_Seats    | int         | YES  |     | NULL    |       |
| FirstAC_Fare     | int         | YES  |     | NULL    |       |
| SecondAC_Fare    | int         | YES  |     | NULL    |       |
| ThirdAC_Fare     | int         | YES  |     | NULL    |       |
| Sleeper_Fare     | int         | YES  |     | NULL    |       |
| Rerouted_Till    | date        | YES  |     | NULL    |       No|
| Added_Till       | date        | YES  |     | NULL    |       No|

#### Booking_Info

    Stores relevant information about each booking. Things liked which user booked
    a seat, who will travel on the seat, is the booking confirmed or in waiting.

| Field            | Type        | Null | Key | Default | Extra |
|------------------|-------------|------|-----|---------|-------|
| Booking_ID       | varchar(10) | NO   | PRI | NULL    |       |
| PNR              | varchar(5)  | NO   |     | NULL    |       |
| User_ID          | varchar(20) | NO   | MUL | NULL    |       |
| Passenger_Name   | varchar(40) | NO   |     | NULL    |       |
| Passenger_Age    | int         | NO   |     | NULL    |       |
| Passenger_Gender | varchar(10) | NO   |     | NULL    |       |
| Booking_Status   | varchar(10) | NO   |     | NULL    |       |

#### Notification

    Stores notification details. The notification itself, the user to give the
    notification to and whether or not the notification has already been seen once.

| Field          | Type         | Null | Key | Default | Extra |
|----------------|--------------|------|-----|---------|-------|
| User_ID        | varchar(20)  | NO   |     | NULL    |       |
| Message        | varchar(500) | NO   |     | NULL    |       |
| Pending_Status | int          | NO   |     | NULL    |       |

#### Route_Info

    Stores the stations each train will visit and various information like arrival
    departure times etc.

| Field            | Type        | Null | Key | Default | Extra |
|------------------|-------------|------|-----|---------|-------|
| Train_ID         | varchar(5)  | NO   | MUL | NULL    |       |
| Train_Name       | varchar(30) | NO   |     | NULL    |       |
| Station          | varchar(20) | NO   |     | NULL    |       |
| Station_No       | int         | NO   |     | NULL    |       |
| City_Code        | varchar(3)  | NO   |     | NULL    |       |
| Arrival          | varchar(4)  | YES  |     | NULL    |       |
| Departure        | varchar(4)  | YES  |     | NULL    |       |
| Day_No           | int         | NO   |     | NULL    |       |
| Distance_Covered | int         | NO   |     | NULL    |       |
| Rerouted         | int         | YES  |     | NULL    |       |
| inCurrentRoute   | int         | NO   |     | NULL    |       |

#### User

    Stores detail of the users of the application.

| Field       | Type        | Null | Key | Default | Extra |
|-------------|-------------|------|-----|---------|-------|
| User_ID     | varchar(20) | NO   | PRI | NULL    |       |
| First_Name  | varchar(20) | NO   |     | NULL    |       |
| Last_Name   | varchar(20) | NO   |     | NULL    |       |
| Gender      | varchar(10) | NO   |     | NULL    |       |
| Age         | int         | NO   |     | NULL    |       |
| Email_ID    | varchar(50) | NO   |     | NULL    |       |
| Phone_No    | varchar(15) | NO   |     | NULL    |       |
| Username    | varchar(20) | NO   |     | NULL    |       |
| Password    | varchar(64) | NO   |     | NULL    |       |
| Total_Spend | int         | NO   |     | 0       |       |

#### Vacancy_Info

    Stores seats which have been booked from a particular station to another.

| Field      | Type        | Null | Key | Default | Extra |
|------------|-------------|------|-----|---------|-------|
| Train_ID   | varchar(5)  | NO   |     | NULL    |       |
| Booking_ID | varchar(15) | NO   | MUL | NULL    |       |
| Date       | date        | NO   |     | NULL    |       |
| Station    | varchar(20) | NO   |     | NULL    |       |
| Station_No | int         | NO   |     | NULL    |       |
| Seat_No    | varchar(10) | NO   |     | NULL    |       |

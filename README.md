# CS166Project
## Group 9: Yuchen Zhu & Angelika Bermudez
Project Implementation:
For our phase 3, we decided to work using github to collaborate and code at the same time. We first wrote all the SQL queries in an SQL file then tested those with predetermined values, and finally implemented them in Hotel.java along with modifying them to take in user inputs. But note that there appears to be 4 contributors; Yuchen Zhu, Angelika Bermudez, aberm028, and yzhu133. For some reason, when we used github through vscode it made the commit authors Yuchen Zhu or Angelika Bermudez instead of the actual github profiles. Because of this, the insight tab doesn’t properly show the edits made by each person. Also the majority of the code on Hotel.java was committed by the Angelika Bermudez profile but the work was split between both members.

SQL Queries:
First, Yuchen wrote out all the SQL queries except for the last two regarding repair requests in a separate file called queries.sql. The queries that had fields that required user inputs were first left blank and to be filled in as we implemented them in the Hotel.java file. Angelika then took the queries in queries.sql and put them into test.sql with all the fields filled in with test values in order to troubleshoot and fix any errors in the queries. It was through here that we found most of our errors with the queries which allowed for an easier translation into the Hotel.java file.

Hotel.java Implementation:
For the functions in this file, Angelika implemented the viewHotel, viewRooms, bookRooms, and viewRecentBookings functions. Yuchen implemented the updateRoomInfo, viewRecentUpdates, viewBookingHistory, and viewRegularCustomers functions. The last two functions, placeRoomRepairRequests and RoomRepairsRequests, were implemented by both team members working together on and off. 

Modifications to Hotel.java:
We made some modifications to the provided code, mainly regarding the  “authorisedUser” variable. Yuchen moved the declaration of this variable to line 35 for later functions 5 through 10 to verify that the user is a manager. We faced an error when we made this change that resulted in the menus not closing properly but we added line 312 where we reset the variable “authorisedUser” to null.

Function #1: viewHotels() - Angelika
This function’s sql was straightforward with a regular select statement but we used the provided calculateDistance SQL function whose first two parameters were each of the hotel’s longitude and latitude, last two parameters were user provided inputs. The hotels that were less than 30 unit distance from the user provided coordinates. An issue faced here was figuring out how the different “executeQuery…” functions worked but after some tinkering we figured out how they worked.

Function #2: viewRooms() - Angelika
The implementation of this function was much like the first one and easier once we figured out the execute functions. We create the query string by patching together the tested SQL statements and the user inputs for the fields that needed it.

Function #3: bookRooms() - Angelika
For this function we implemented it much like the previous two patching together the tested query statement with the user inputs to find the particular room that they were trying to book then executing it but this time we executed the query and returned the results into List<List<String>> result. We then iterated through this list of lists to see if the booking that the user wanted to commit interfered with any other bookings and if it didn’t we would move on and construct a new query to update the roomBookings table with the user provided information.

Function #4: viewRecentBookingsfromCustomer() - Angelika
The implementation was much like 1 and 2 but in the SQL query we used “order by” in conjunction with “limit” in order to arrange the bookings for the customer into descending order so the most recent are on top and then using “LIMIT 5” we only select the top 5.

Function #5: updateRoomInfo() - Yuchen
Starting with this function we needed to first verify that the user was a manager. So with the modified “authorisedUser” we execute a query statement finding the user type of the authorisedUser ID and return it into a list of lists. We then iterate through the list of lists to see if the authorisedUser is a manager or not. If they are, then we move on to executing the main query which is updating the information of an existing room with user provided information. If they aren’t then we print out “Sorry this feature is for managers only.”

Function #6:viewRecentUpdates() - Yuchen
Much like function 4 we use “order by” and “limit” to print out the 5 most recent updates. We don’t take in an input from the user because we use the modified “authorisedUser” variable to directly search if using it as the userID input. We also use the same method described in 5 to check if the user is a manager or not.

Function #7: viewBookingHistoryofHotel() - Yuchen
We use the same method as above to verify that the user is a manager. For this SQL statement we did a nested select where we returned the roomBooking row values that we want where the hotel is in a list of hotels run by the authorisedUser which we found using the nested select statement.

Function #8: viewRegularCustomers() - Yuchen
We use the same method as above to verify that the user is a manager. For this query we counted the number of times the userID of an user appears in the roomBookings table for hotels that are managed by authorisedUser using “group by” and “count()”. We return these values but using “order by” and “limit” to return only the top 5.

Function #9: placeRoomRepairRequests() - Both Members
Here we check twice if the selection is valid. First we check if the user is a manager, then we take in the information to process a room repair request then we check if the hotel they are trying to repair is managed by the authorisedUser. If it is, we continue and process the query update as so.

Function #10: viewRoomRepairHistory() - Both Members
To view this we only check one thing, if the current user is a manager or not. Then like previous functions we select the fields we want to display and piece it together with the user input to run the query.

Indexes:
We made a create_indexes file to implement performance tuning by creating indexes for 
the primary and foreign keys of the relations. We used BTREE to quickly index the data.

queries.sql and test.sql:
	These files were used to test our queries before we implemented them into the java file.

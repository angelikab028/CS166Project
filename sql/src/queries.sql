--this file is for writing the sqls to be implemented in java

--public static void viewHotels(Hotel esql) {}
SELECT h.hotelName
FROM Hotel h
WHERE calculate_distance(h.latitude, h.longitude,
                         -- user inputs from java func)
                         < 30.0;

--public static void viewRooms(Hotel esql) {}
SELECT r.roomNumber, r.price
FROM Rooms r
WHERE NOT EXISTS(SELECT *
                 FROM RoomBookings rb
                 WHERE rb.hotelID = --user input
                       AND rb.bookingDate = --user input
                       AND rb.roomNumber = r.roomNumber)
                AND r.hotelID = --user input;

--public static void bookRooms(Hotel esql) {}
-- use execute and return func to return the previous query 
-- into the list o list then do if(result.get(0).contains(roomNum))
-- true then say invalid and if false continue
SELECT rb.roomNumber
FROM RoomBookings rb
WHERE rb.hotelID = --user input
    AND rb.roomNumber = --user input
    AND rb.bookingDate = --user input;

INSERT INTO RoomBookings (customerID, hotelID, roomNumber, bookingDate)
VALUES --(user inputs);

--public static void viewRecentBookingsfromCustomer(Hotel esql) {}
SELECT rb.hotelID, rb.roomNumber, r.price, rb.bookingDate 
FROM RoomBookings rb, Rooms r
WHERE rb.customerID = --customer id (user input)
      AND rb.hotelID = r.hotelID
      AND rb.roomNumber = r.roomNumber
ORDER BY rb.bookingDate DESC
LIMIT 5;

--public static void updateRoomInfo(Hotel esql) {}
-- verify if manager first 
UPDATE Rooms
SET price = --user input,
    imageURL = --user input
WHERE roomNumber = --user input
      AND hotelID = --user input;
--store update
INSERT INTO RoomUpdatesLog (managerID, hotelID, roomNUmber, updatedOn)
VALUES (--user inputs, CURRENT_TIMESTAMP);

--public static void viewRecentUpdates(Hotel esql) {}
-- verify if manager first
SELECT *
FROM RoomUpdatesLog ru
WHERE hotelID = --user input
      AND managerID = --managerID (user input)
ORDER BY updateNumber DESC
LIMIT 5;

--public static void viewBookingHistoryofHotel(Hotel esql) {}
-- verify if manager first
SELECT rb.bookingID, u.name, rb.hotelID, rb.roomNumber, rb.bookingDate
FROM RoomBookings rb, Users u
WHERE rb.hotelID IN (SELECT h.hotelID
                 FROM Hotel h
                 WHERE managerUserID = --managerUserID (user input))
      AND u.userID = rb.customerID
ORDER BY rb.hotelID;

--public static void viewRegularCustomers(Hotel esql) {}
-- verify if manager first
SELECT rb.customerID, u.name, COUNT(*) as numOfBookings
FROM RoomBookings rb, Hotel h, Users u
WHERE rb.hotelID = --user input
      AND h.hotelID = rb.hotelID
      AND h.managerUserID = --managerid user input
      AND u.userID = rb.customerID
GROUP BY rb.customerID
ORDER BY numOfBookings DESC
LIMIT 5

--public static void placeRoomRepairRequests(Hotel esql) {}
-- verify if manager first


--public static void viewRoomRepairHistory(Hotel esql) {}
-- verify if manager first


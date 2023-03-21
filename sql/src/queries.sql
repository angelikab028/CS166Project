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
-- into the list o list then do result.get(0).contains(roomNum) if
-- true then say invalid and if false continue
SELECT rb.roomNumber
FROM RoomBookings rb
WHERE rb.hotelID = --user input
    AND rb.roomNumber = --user input
    AND rb.bookingDate = --user input;

INSERT INTO RoomBookings (customerID, hotelID, roomNumber, bookingDate)
VALUES --(user inputs);

--public static void viewRecentBookingsfromCustomer(Hotel esql) {}


--public static void updateRoomInfo(Hotel esql) {}


--public static void viewRecentUpdates(Hotel esql) {}


--public static void viewBookingHistoryofHotel(Hotel esql) {}


--public static void viewRegularCustomers(Hotel esql) {}


--public static void placeRoomRepairRequests(Hotel esql) {}


--public static void viewRoomRepairHistory(Hotel esql) {}


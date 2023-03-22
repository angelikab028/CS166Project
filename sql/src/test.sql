SELECT h.hotelName
FROM Hotel h
WHERE calculate_distance(h.latitude, h.longitude,
                         94.77143,25.58018)
                         < 30.0;

SELECT r.roomNumber, r.price
FROM Rooms r
WHERE NOT EXISTS(SELECT *
                 FROM RoomBookings rb
                 WHERE rb.hotelID = 1
                       AND rb.bookingDate = '5/12/2015'
                       AND rb.roomNumber = r.roomNumber)
                AND r.hotelID = 1;

SELECT rb.roomNumber
FROM RoomBookings rb
WHERE rb.hotelID = 1
    AND rb.roomNumber = 5
    AND rb.bookingDate = '5/12/2015';

--INSERT INTO RoomBookings (customerID, hotelID, roomNumber, bookingDate)
--VALUES (1, 1, 6, '5/12/2015');

SELECT *
FROM RoomBookings rb
WHERE rb.customerID = 1 AND hotelID = 1 AND roomNumber = 6;

SELECT rb.hotelID, rb.roomNumber, r.price, rb.bookingDate
FROM RoomBookings rb, Rooms r
WHERE rb.customerID = 1
      AND rb.hotelID = r.hotelID
      AND rb.roomNumber = r.roomNumber
ORDER BY rb.bookingDate DESC
LIMIT 5;

UPDATE Rooms
SET price = 1690,
    imageURL = 'abcd'
WHERE roomNumber = 3
      AND hotelID = 12;
--store update
INSERT INTO RoomUpdatesLog (managerID, hotelID, roomNUmber, updatedOn)
VALUES (51, 12, 3, CURRENT_TIMESTAMP);

SELECT rb.hotelID, rb.roomNumber, r.price, rb.bookingDate
FROM RoomBookings rb, Rooms r
WHERE rb.customerID = 1
      AND rb.hotelID = r.hotelID
      AND rb.roomNumber = r.roomNumber
ORDER BY rb.bookingDate DESC
LIMIT 5;

SELECT *
FROM RoomUpdatesLog ru
WHERE hotelID = 12
      AND managerID = 51
ORDER BY updateNumber DESC
LIMIT 5;

SELECT rb.bookingID, u.name, rb.hotelID, rb.roomNumber, rb.bookingDate
FROM RoomBookings rb, Users u
WHERE rb.hotelID IN (SELECT h.hotelID
                 FROM Hotel h
                 WHERE h.managerUserID = 51)
      AND u.userID = rb.customerID
ORDER BY rb.hotelID;

SELECT rb.customerID, u.name, COUNT(*) as numOfBookings
FROM RoomBookings rb, Hotel h, Users u
WHERE rb.hotelID = 10
      AND h.hotelID = rb.hotelID
      AND h.managerUserID = 51
      AND u.userID = rb.customerID
GROUP BY rb.customerID, u.userID
ORDER BY numOfBookings DESC
LIMIT 5
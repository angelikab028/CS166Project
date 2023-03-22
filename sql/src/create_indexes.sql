DROP INDEX IF EXISTS latitude_index CASCADE;
DROP INDEX IF EXISTS longitude_index CASCADE;
DROP INDEX IF EXISTS hotelID_index CASCADE;
DROP INDEX IF EXISTS bookingDate_index CASCADE;
DROP INDEX IF EXISTS roomNumber_index CASCADE;
DROP INDEX IF EXISTS customerID_index CASCADE;
DROP INDEX IF EXISTS imageURL_index CASCADE;
DROP INDEX IF EXISTS price_index CASCADE;
DROP INDEX IF EXISTS managerID_index CASCADE;
DROP INDEX IF EXISTS updatedOn_index CASCADE;
DROP INDEX IF EXISTS userID_index CASCADE;
DROP INDEX IF EXISTS companyID_index CASCADE;
DROP INDEX IF EXISTS repairID_index CASCADE;

CREATE INDEX latitude_index
ON Hotel
USING BTREE
(latitude);

CREATE INDEX longitude_index
ON Hotel
USING BTREE
(longitude);

CREATE INDEX hotelID_index
ON Hotel
USING BTREE
(hotelID);

CREATE INDEX bookingDate_index
ON RoomBookings
USING BTREE 
(bookingDate);

CREATE INDEX roomNumber_index
ON RoomBookings
USING BTREE 
(roomNumber);

CREATE INDEX customerID_index
ON RoomBookings
USING BTREE
(customerID);

CREATE INDEX imageURL_index
ON Rooms
USING BTREE
(imageURL);

CREATE INDEX price_index
ON Rooms
USING BTREE
(price);

CREATE INDEX managerID_index
ON RoomRepairRequests
USING BTREE
(managerID);

CREATE INDEX updatedOn_index
ON RoomUpdatesLog
USING BTREE
(updatedOn);

CREATE INDEX userID_index
ON Users
USING BTREE
(userID);

CREATE INDEX companyID_index
ON MaintenanceCompany
USING BTREE
(companyID);

CREATE INDEX repairID_index
ON RoomRepairs
USING BTREE
(repairID);







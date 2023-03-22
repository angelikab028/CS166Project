/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Hotel {

   public static String authorisedUser = null;

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Hotel 
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Hotel(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Hotel

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
      Statement stmt = this._connection.createStatement ();

      ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }

   public int getNewUserID(String sql) throws SQLException {
      Statement stmt = this._connection.createStatement ();
      ResultSet rs = stmt.executeQuery (sql);
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }
   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Hotel.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Hotel esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Hotel object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Hotel (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Hotels within 30 units");
                System.out.println("2. View Rooms");
                System.out.println("3. Book a Room");
                System.out.println("4. View recent booking history");

                //the following functionalities basically used by managers
                System.out.println("5. Update Room Information");
                System.out.println("6. View 5 recent Room Updates Info");
                System.out.println("7. View booking history of the hotel");
                System.out.println("8. View 5 regular Customers");
                System.out.println("9. Place room repair Request to a company");
                System.out.println("10. View room repair Requests history");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewHotels(esql); break;
                   case 2: viewRooms(esql); break;
                   case 3: bookRooms(esql); break;
                   case 4: viewRecentBookingsfromCustomer(esql); break;
                   case 5: updateRoomInfo(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewBookingHistoryofHotel(esql); break;
                   case 8: viewRegularCustomers(esql); break;
                   case 9: placeRoomRepairRequests(esql); break;
                   case 10: viewRoomRepairHistory(esql); break;
                   case 20: usermenu = false; 
                            authorisedUser = null; 
                            break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Hotel esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine(); 
         String type="Customer";
			String query = String.format("INSERT INTO USERS (name, password, userType) VALUES ('%s','%s', '%s')", name, password, type);
         esql.executeUpdate(query);
         System.out.println ("User successfully created with userID = " + esql.getNewUserID("SELECT last_value FROM users_userID_seq"));
         
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Hotel esql){
      try{
         System.out.print("\tEnter userID: ");
         String userID = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE userID = '%s' AND password = '%s'", userID, password);
         int userNum = esql.executeQuery(query);

         /* --------------------------------------------------------
            here we need to implement a global bool that reflects if
            the user is a manger or not
            
            use the execute and return then .contains('manager') 
            -------------------------------------------------------- */

         if (userNum > 0)
            return userID;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void viewHotels(Hotel esql) //Allows the user to see the list of hotels within 30 units distance of the user’s given input location 
   {
      try{
         System.out.print("\tEnter longitude: ");
         String longitude = in.readLine();
         System.out.print("\tEnter latitude: ");
         String latitude = in.readLine();

         String query = "SELECT h.hotelName " +
                        "From Hotel h " +
                        "WHERE calculate_distance( h.latitude, h.longitude, " + longitude + ", " + latitude + ") < 30.0";

         esql.executeQueryAndPrintResult(query);           
         int rowCount = esql.executeQuery(query);
         System.out.println("total row(s):" + rowCount);
      }catch(Exception e){
         System.err.println(e.getMessage ());
         //return null;
      }  
   } //end viewHotels;

   public static void viewRooms(Hotel esql) 
   {
      try 
      {
         System.out.print("\tEnter hotel ID: ");
         String hotelID = in.readLine();
         System.out.print("\tEnter booking date: ");
         String bookingDate = in.readLine();
         
         String query = "SELECT r.roomNumber, r.price " +
                        "FROM Rooms r " + 
                        "WHERE NOT EXISTS(SELECT *" +
                                         "FROM RoomBookings rb " +
                                         "WHERE rb.hotelID = " + hotelID +
                                         " AND rb.bookingDate = '" + bookingDate + "'" +
                                         " AND rb.roomNumber = r.roomNumber) " +
                        "AND r.hotelID = " + hotelID;
         
         esql.executeQueryAndPrintResult(query);
         int rowCount = esql.executeQuery(query);
         System.out.println("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }

   }//end viewRooms

   public static void bookRooms(Hotel esql) 
   {
      try 
      {
         System.out.print("\tEnter hotel ID: ");
         String hotelID = in.readLine();
         System.out.print("\tEnter booking date: ");
         String bookingDate = in.readLine();
         System.out.print("\tEnter room number: ");
         String roomNumber = in.readLine();

         String query = "SELECT rb.roomNumber " +
                        "FROM RoomBookings rb " +
                        "WHERE rb.hotelID = " + hotelID +
                        " AND rb.bookingDate = '" + bookingDate + "'" +
                        " AND rb.roomNumber = " + roomNumber + ";";
      

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         
         boolean booked = false;

         for (List<String> innerResult : result){ 
            if (innerResult.contains(roomNumber)) {
               booked = true;
               break;
            }
         }

         if(booked){
            System.out.println("Cannot book room because room is already booked.");
         }else{
            
            query = "INSERT INTO RoomBookings(customerID, hotelID, roomNumber, bookingDate) " +
                           "VALUES (" + authorisedUser + ", " + hotelID + ", " + roomNumber + ", \'" + bookingDate + "\')";

            esql.executeUpdate(query);

            System.out.println("Room successfully booked.");
            //int rowCount = esql.executeQuery(query);
            //System.out.println("total row(s): " + rowCount);
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
   } //end bookRooms
   
   public static void viewRecentBookingsfromCustomer(Hotel esql) 
   {
      try
      {
          String query = "SELECT rb.hotelID, rb.roomNumber, r.price, rb.bookingDate " +
                         "FROM RoomBookings rb, Rooms r " +
                         "WHERE rb.customerID = " + authorisedUser +
                         " AND rb.hotelID = r.hotelID " +
                         "AND rb.roomNumber = r.roomNumber " +
                         "ORDER BY rb.bookingDate DESC " +
                         "LIMIT 5";

         esql.executeQueryAndPrintResult(query);
         int rowCount = esql.executeQuery(query);
         System.out.println("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
   }//end viewRecentBookingsfromCustomer
   
   public static void updateRoomInfo(Hotel esql) 
   {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            System.out.print("\tEnter hotel ID: ");
            String hotelID = in.readLine();
            System.out.print("\tEnter room number: ");
            String roomNumber = in.readLine();
            System.out.print("\tEnter updated price: ");
            String roomPrice = in.readLine();
             System.out.print("\tEnter image url: ");
            String imageUpdate = in.readLine();

            query = "UPDATE Rooms " +
                  "SET price = " + roomPrice + ", " +
                      "imageURL = \'" + imageUpdate + "\' " + 
                  "WHERE roomNumber = " + roomNumber + " " + 
                        "AND hotelID = " + hotelID + "; ";

            esql.executeUpdate(query);
            System.out.println("Room successfully updated.");

         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }

   }//end updateRoomInfo

   public static void viewRecentUpdates(Hotel esql) 
   {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            System.out.print("\tEnter hotel ID: ");
            String hotelID = in.readLine();

            query = "SELECT * " +
                    "FROM RoomUpdatesLog ru " +
                    "WHERE hotelID = " + hotelID +
                     " AND managerID = " + authorisedUser +
                     " ORDER BY updateNumber DESC " +
                     "LIMIT 5";
            List<List<String>> result1 = esql.executeQueryAndReturnResult(query);
            for (List<String> innerResult : result1){ 
               System.out.println(innerResult);
            }
         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
   }//end viewRecentUpdates

   public static void viewBookingHistoryofHotel(Hotel esql) {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            query = "SELECT rb.bookingID, u.name, rb.hotelID, rb.roomNumber, rb.bookingDate " +
                 "FROM RoomBookings rb, Users u " +
                 "WHERE rb.hotelID IN (SELECT h.hotelID " +
                                      "FROM Hotel h " +
                                      "WHERE managerUserID = " + authorisedUser + ") " +
                 "AND u.userID = rb.customerID " +
                 "ORDER BY rb.hotelID";
            List<List<String>> result1 = esql.executeQueryAndReturnResult(query);
            for (List<String> innerResult : result1){ 
               System.out.println(innerResult);
            }
         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
      
   }//end viewBookingHistoryOfHotel

   public static void viewRegularCustomers(Hotel esql) 
   {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            System.out.print("\tEnter hotel ID: ");
            String hotelID = in.readLine();
            query = "SELECT rb.customerID, u.name, COUNT(*) AS numOfBookings " +
                    "FROM RoomBookings rb, Hotel h, Users u " +
                    "WHERE rb.hotelID = " + hotelID + 
                    " AND h.hotelID = rb.hotelID " +
                    "AND h.managerUserID = " + authorisedUser +
                    " AND u.userID = rb.customerID " +
                    "GROUP BY rb.customerID, u.userID " +
                    "ORDER BY numOfBookings DESC " +
                    "LIMIT 5"; 
            List<List<String>> result1 = esql.executeQueryAndReturnResult(query);
            for (List<String> innerResult : result1){ 
               System.out.println(innerResult);
            }
         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
      
   }// end viewRegularCustomers

   public static void placeRoomRepairRequests(Hotel esql) 
   {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            System.out.print("\tEnter hotel ID: ");
            String hotelID = in.readLine();
            System.out.print("\tEnter room number: ");
            String roomNumber = in.readLine();
            System.out.print("\tEnter companyID: ");
            String compID = in.readLine();

            query = "SELECT hotelID " + 
                    "FROM Hotel " +
                    "WHERE Hotel.managerUserID = " + authorisedUser + ";";

            esql.executeQueryAndPrintResult(query); //remove
            result = esql.executeQueryAndReturnResult(query);
            boolean belongs = false;
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(hotelID)) {
                  belongs = true;
                  break;
               }
            }
            
            if(belongs){
               query = "INSERT INTO RoomRepairs(companyID, hotelID, roomNumber, repairDate) " + 
                    "VALUES (" + compID + ", " + hotelID + ", " + roomNumber + ", CURRENT_DATE);";
               esql.executeUpdate(query);

               query = "INSERT INTO RoomRepairRequests(repairID, managerID)" + 
                       "SELECT rr.repairID, " + authorisedUser + " " + 
                       "FROM RoomRepairs rr " + 
                       "WHERE rr.companyID = " + compID + " " +
                           "AND rr.hotelID = " + hotelID + " " +
                           "AND rr.roomNumber = " + roomNumber + ";";
               esql.executeUpdate(query);

               System.out.println("Request processed.");
            }else{
               System.out.println("Sorry you don't manage this hotel.");
            }

            
         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }//end placeRoomRepairRequests
}

   public static void viewRoomRepairHistory(Hotel esql) 
   {
      try{
         String query = "SELECT usertype " +
                        "FROM Users " +
                        "WHERE userID = " + authorisedUser + ";"; 

         esql.executeQueryAndPrintResult(query);

         List<List<String>> result = esql.executeQueryAndReturnResult(query);
            boolean man = false;
            String managerStr = "manager   ";
            for (List<String> innerResult : result){ 
               //System.out.println("Inner result: " + innerResult);
               if (innerResult.contains(managerStr)) {
                  man = true;
                  break;
               }
            }
         
         if(man){
            query = "SELECT r2.companyID, r2.hotelID, r2.roomNumber, r2.repairDate " + 
                    "FROM RoomRepairs r2, RoomRepairRequests r3 " + 
                    "WHERE r3.managerID = " + authorisedUser + " " + 
                          "AND r3.repairID = r2.repairID;";
            esql.executeQueryAndPrintResult(query);

            int rowCount = esql.executeQuery(query);
            System.out.println("total row(s): " + rowCount);

         }else{
            System.out.println("Sorry this feature is for managers only.");
         }
      }catch(Exception e){
         System.err.println(e.getMessage ());
      }
   }//end viewRoomRepairHistory
}//end Hotel*/



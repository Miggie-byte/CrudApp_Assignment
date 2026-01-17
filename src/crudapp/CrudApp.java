package crudapp;

import java.sql.*;
import java.util.Scanner;

public class CrudApp {

    public static final String URL = "jdbc:derby://localhost:1527/CrudApp;create=true"; // Checks if there is an exisiting DB called CrudApp, if there is connects normally, if none it creates is explicitly
    public static final String USERNAME = "app";
    public static final String PASSWORD = "app";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";
    public static int oper = 4;
    static Employee emp = new Employee();
    static Scanner scanner = new Scanner(System.in);

    // Import the methods for the prepared statement (Insert, Update, Delete)
    // -- INSERT --
    public static void addEmployee(Employee emp) throws SQLException {
        String insertString = "INSERT into employees" + "(employee_number, first_name, last_name) " + "VALUES(?,?,?)"; // INSERT DYNAMIC QUERY

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(insertString);
            // Filling in the placeholders
            ps.setInt(1, emp.getId()); // 0 is the index of the first placeholder ?
            ps.setString(2, emp.getFirst_name());
            ps.setString(3, emp.getLast_name());
            ps.executeUpdate();

            System.out.println("Inserted Succesfully: " + emp.getFirst_name());
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -- DELETE --
    public static void deleteEmployee(int id) throws SQLException {
        String deleteString = "DELETE from employees " + "WHERE employee_number = ?";

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(deleteString);

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Deleted Successfully: " + id);
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEmployee(Employee emp) throws SQLException {
        String updateString = "UPDATE employees " + "SET first_name = ?,last_name = ? " + "WHERE employee_number =?";

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(updateString);

            ps.setString(1, emp.getFirst_name());
            ps.setString(2, emp.getLast_name());
            ps.setInt(3, emp.getId());
            ps.executeUpdate();

            System.out.println("Updated Successfully!");
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTableIfNotExist(Connection conn) throws SQLException {
        String sql = "CREATE TABLE employees (" + "employee_number INT PRIMARY KEY, " + "first_name VARCHAR(50), " + "last_name VARCHAR(50))"; // Query for Table Creation

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.printf(YELLOW + "DB State: "+ RESET +"Database CrudApp created. %n");
            System.out.printf(YELLOW + "DB State: "+ RESET +"Table Employees created. %n");
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) { // In this line we are trying to check here if the table "employees" already exist
                System.out.printf(YELLOW + "DB State: "+ RESET +"Database CrudApp already exists. %n");
                System.out.printf(YELLOW + "DB State: "+ RESET +"Employees table already exists. %n");
            } else {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {

        // Establish the connection to the database 
        try {
            // Load the driver
            String driver = "org.apache.derby.jdbc.ClientDriver";
            Class.forName(driver);
            System.out.printf(GREEN + "Loaded Driver: " + RESET + driver + "%n");

            // Create the URL
            //String url = "jdbc:derby://localhost:1527/CrudApp";
            // Create a connection to the database with the URL and with the username and password "app"
            try {
                Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.printf(GREEN + "Connected: " + RESET + URL + "%n");
                // insert method for table creation
                createTableIfNotExist(con);

                // TERMINAL INTERFACE
                do {
                    System.out.println("\nPick Operation\n 1 - INSERT \n 2 - UPDATE \n 3 - DELETE\n 4 - TERMINATE");
                    oper = scanner.nextInt();

                    switch (oper) {
                        case 1:
                            System.out.println("INSERT");
                            System.out.print("Enter Employee Number: ");
                            emp.setID(scanner.nextInt());
                            System.out.print("Enter Firstname: ");
                            emp.setFirst_name(scanner.next());
                            System.out.print("Enter Lastname: ");
                            emp.setLast_name(scanner.next());
                            addEmployee(emp);
                            break;
                        case 2:
                            System.out.println("UPDATE");
                            System.out.print("Enter Employee Number: ");
                            emp.setID(scanner.nextInt());
                            System.out.print("Enter Firstname: ");
                            emp.setFirst_name(scanner.next());
                            System.out.print("Enter Lastname: ");
                            emp.setLast_name(scanner.next());
                            updateEmployee(emp);
                            break;
                        case 3:
                            System.out.println("DELETE");
                            System.out.print("Enter Employee Number: ");
                            deleteEmployee(scanner.nextInt());
                    }
                    
                    // this automatically shows the table every time operation is executed
                    Statement stmt = con.createStatement();
                    String qry = "SELECT * FROM employees";
                    ResultSet rs = stmt.executeQuery(qry);
                    System.out.println("EXECUTED QUERY ---> " + qry);

                    // Print the results, row by row
                    System.out.println("\nPROCESSING RESULTS:\n");
                    while (rs.next()) {
                        System.out.println("  Employee Number: "
                                + rs.getString("employee_number").trim());

                        System.out.println("  First Name: "
                                + rs.getString("first_name").trim());

                        System.out.println("  Last Name: "
                                + rs.getString("last_name").trim());

                        System.out.println("");
                    }

                } while (oper != 4);

                System.out.println("System Terminated");
                con.close();
                System.exit(0);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}

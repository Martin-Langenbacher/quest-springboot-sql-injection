package com.bankzecure.webapp.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bankzecure.webapp.entity.*;
import com.bankzecure.webapp.JdbcUtils;




public class CustomerRepository {
  private final static String DB_URL = "jdbc:mysql://localhost:3306/springboot_bankzecure?serverTimezone=GMT";
	private final static String DB_USERNAME = "bankzecure";
	private final static String DB_PASSWORD = "Ultr4B4nk@L0nd0n";

  public Customer findByIdentifierAndPassword(final String identifier, final String password) {
    Connection connection = null;
    // Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    
/* - 7 --> (last from CostomerControl) 
   Check out CustomerRepository's findByIdentifierAndPassword method: here's another case of a query built by
   including user input (the identifier and password parameters) as is.   
   
   As a would-be hacker, using this to your advantage may seem trickier at first glance, since the WHERE clause
   combines two terms with AND. Still, you just saw that some attack variations lead MySQL to ignore the end of
   the query. Try to reuse what worked for the credit card numbers!
   
   Indeed, one of them works. But you always end up on the same customer's profile. Try and think how you could
   refine the attack, in order to gain access to anyone's account.
*/ 

   
    
    try {
      connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//      preparedStatement = connection.createStatement();
//      final String query = "SELECT * FROM customer " +
//        "WHERE identifier = '" + identifier + "' AND password = '" + password + "'";
      	preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE identifier = ? AND passwortd = ?");      	
      	preparedStatement.setString(1, identifier);
      	preparedStatement.setString(2,  password);
      
//      resultSet = statement.executeQuery(query);
      resultSet = preparedStatement.executeQuery();

      Customer customer = null;
      
      if (resultSet.next()) {
        final int id = resultSet.getInt("id");
        final String identifierInDb = resultSet.getString("identifier");
        final String firstName = resultSet.getString("first_name");
        final String lastName = resultSet.getString("last_name");
        final String email = resultSet.getString("email");
        customer = new Customer(id, identifierInDb, firstName, lastName, email);
      }
      return customer;
    } catch (final SQLException e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeResultSet(resultSet);
      JdbcUtils.closeStatement(preparedStatement);
      JdbcUtils.closeConnection(connection);
    }
    return null;
  }

 
  
  public Customer update(String identifier, String newEmail, String newPassword) {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Customer customer = null;
    try {
        // Connection and statement
        connection = DriverManager.getConnection(
          DB_URL, DB_USERNAME, DB_PASSWORD
        );
//        statement = connection.createStatement();
           
        // Build the update query using a QueryBuilder
        StringBuilder queryBuilder = new StringBuilder();
//      queryBuilder.append("UPDATE customer SET email = '" + newEmail + "'");
        queryBuilder.append("UPDATE customer SET email = ?");
        
        
        
     
  
        
        // Don't set the password in the update query, if it's not provided
        if (newPassword != "") {
//          queryBuilder.append(",password = '" + newPassword + "'");
        queryBuilder.append(",password = ?");
        }
//        queryBuilder.append(" WHERE identifier = '" + identifier + "'");
        queryBuilder.append(" WHERE identifier = ?");
        String query = queryBuilder.toString();
        preparedStatement = connection.prepareStatement(query);  // --> check this one better... !!!!
//        statement.executeUpdate(query);
        preparedStatement.setString(1, newEmail);
        if (newPassword != "") {
        	preparedStatement.setString(2, newPassword);
        	preparedStatement.setString(3, identifier);
		} else {
			preparedStatement.setString(2, identifier);
		}
        
        if (preparedStatement.executeUpdate() != 1) {
        	throw new SQLException("failed to update data");
		}

        JdbcUtils.closeStatement(preparedStatement);
        JdbcUtils.closeConnection(connection);
        
        

        
        
        

        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//        statement = connection.createStatement();
//        query = "SELECT * FROM customer WHERE identifier = '" + identifier + "'";
//        resultSet = statement.executeQuery(query);
        preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE identifier = ?");
        preparedStatement.setString(1,  identifier);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
          final int id = resultSet.getInt("id");
          final String identifierInDb = resultSet.getString("identifier");
          final String firstName = resultSet.getString("first_name");
          final String lastName = resultSet.getString("last_name");
          final String email = resultSet.getString("email");
          customer = new Customer(id, identifierInDb, firstName, lastName, email);
        }
        return customer;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
   	    JdbcUtils.closeStatement(preparedStatement);
   	    JdbcUtils.closeConnection(connection);
    }
    return null;
}
}



/*

The trick is to replace 1=1 (which is always true) with something else, which will be true on a specific condition,
that you decide.

Inserting ' OR 1=1 -- ; in the query leads to getting all the customers in resultSet. Since the code that comes
after only takes the first record of this result set, you have to alter the query, so that the result set doesn't
include all the users.

Try inserting ' OR id > 1 -- ; in the identifier field: this eliminates the user with id 1 from the result set,
and you gain access to the next customer's account (id 2).

If you repeat this with other values than 1, you can get access to any account. It would even be feasible to run
a program that does it automatically!

As for the third SQLi vulnerability, it will be up to you to exploit it in the challenge.

Before that, discover how to fix these vulnerabilities!

*/





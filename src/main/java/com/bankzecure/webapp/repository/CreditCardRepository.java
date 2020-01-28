package com.bankzecure.webapp.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.bankzecure.webapp.entity.*;
import com.bankzecure.webapp.JdbcUtils;

public class CreditCardRepository {
	private final static String DB_URL = "jdbc:mysql://localhost:3306/springboot_bankzecure?serverTimezone=GMT";
	private final static String DB_USERNAME = "bankzecure";
	private final static String DB_PASSWORD = "Ultr4B4nk@L0nd0n";

	
  public List<CreditCard> findByCustomerIdentifier(final String identifier) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    // alt: Statement statement = null;
    ResultSet resultSet = null;
    
    
//    final String query = "SELECT cc.* FROM credit_card cc " +
//      "JOIN customer c ON cc.customer_id = c.id " +
//     "WHERE c.identifier = '" + identifier + "'";
   
    
/* -3-->    (coming from CreditCardController)
    CreditCardRepository.findByCustomerIdentifier begins by building an SQL query, from hardcoded SQL code
    that we concatenate with the identifier received as an argument. This query aims to get the credit cards
    belonging to a specific user, using the 6-digit identifier in a WHERE clause, to obtain only the relevant
    data.
    
    -4-->
    It establishes a connection to the database and stores it in connection, then creates a Statement instance
    via connection.createStatement(). This is a tad bit different from the JDBC&Spring quests, where we created
    a PreparedStatement instance instead. */    
    
    try {
      connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
      preparedStatement = connection.prepareStatement("SELECT cc.* FROM credit_card cc JOIN customer c ON cc.customer_id = c.id WHERE c.identifier = ?");
      // cc ==> alias zu credit_card
      preparedStatement.setString(1, identifier);
      //alt: statement = connection.createStatement(query);
      resultSet = preparedStatement.executeQuery();
      
/*      
    - 5 -->  
    It then calls the executeQuery method of this Statement instance, to actually fire the SQL query.
    
    Then, an ArrayList of CreditCard instances is built, by looping through resultSet. If everything goes well,
    this ArrayList instance is returned to the controller, which injects it into the customer_cards Thymeleaf
    template, via the creditCards attribute.
    
    ==> To summarize, the request landed on the Controller, which queried the Model, then injected its data into the View.
      
*/      

      final List<CreditCard> creditCards = new ArrayList<CreditCard>();

      
      while (resultSet.next()) {
        final int id = resultSet.getInt("id");
        final int customerId = resultSet.getInt("customer_id");
        final String type = resultSet.getString("type");
        final String number = resultSet.getString("number");
        final String cvv = resultSet.getString("cvv");
        final String expiry = resultSet.getString("expiry");
        creditCards.add(new CreditCard(id, customerId, type, number, cvv, expiry));
      }

      return creditCards;
    } catch (final SQLException e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeResultSet(resultSet);
      JdbcUtils.closeStatement(preparedStatement);
      JdbcUtils.closeConnection(connection);
    }
    return null;
  }
}




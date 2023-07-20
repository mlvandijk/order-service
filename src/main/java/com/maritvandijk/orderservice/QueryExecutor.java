package com.maritvandijk.orderservice;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public class QueryExecutor {
    public void query(Connection connection) {
        try {
            connection.prepareCall("SELECT * FROM CUSTOMER WHERE customer.ID LIKE 'J%'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
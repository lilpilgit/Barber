package model.dao.mySQLJDBCImpl;

import model.dao.OrdersDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrdersDAOMySQLJDBCImpl implements OrdersDAO {

    /*
    questa è la classe che implementa a tutti gli effetti i metodi presenti nella OrdersDao
    dunque è l'implementazione per solamente MySQL, se si ci fossero stati altri DB con cui dialogare
    si sarebbero dovuti creare altri package dal nome per esempio PostgreeSQLJDBCImpl e avremmo dovuto
    scrivere le implementazioni per ogni possibile db
     */

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public OrdersDAOMySQLJDBCImpl (Connection connection) { this.connection = connection; }


}

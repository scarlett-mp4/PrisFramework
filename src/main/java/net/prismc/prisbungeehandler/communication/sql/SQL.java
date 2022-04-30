package net.prismc.prisbungeehandler.communication.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQL {

    /**
     * Returns true if the selected object is equal
     * to the specified parameters.
     *
     * @param getColumn  - Column to check
     * @param equalsThis - What object is equal to
     * @param fromTable  - What table to look in
     */
    public boolean exists(final String getColumn, String equalsThis, String fromTable) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM " + fromTable + " WHERE " + getColumn + " = '" + equalsThis + "' LIMIT 1");
        if (rs.next()) {
            rs.close();
            stmt.close();
            con.close();
            return true;
        }
        rs.close();
        stmt.close();
        con.close();

        return false;
    }

    /**
     * Insert new data into a column.
     *
     * @param columns   - Specify the columns to insert the data to; ex: "id, name, nickname"
     * @param values    - Values to insert; ex: "8, xSkarless, YourGoddess"
     * @param intoTable - What table to insert the data into
     */
    public void insertData(String columns, String values, String intoTable) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "INSERT INTO " + intoTable + " (" + columns + ") VALUES (" + values + ");";
            stmt.execute(sql);
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        stmt.close();
        con.close();
    }

    /**
     * Delete data in a specified column.
     *
     * @param deleteWhere - What to delete; ex: "one=1, two=2"
     * @param fromTable   - What table to delete from
     */
    public void deleteData(String[] deleteWhere, String fromTable) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();
        String arguments = "";

        for (final String argument : deleteWhere) {
            arguments = arguments + argument + " AND ";
        }
        arguments = arguments.substring(0, arguments.length() - 5);

        String sql = "DELETE FROM " + fromTable + " WHERE " + arguments;
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    /**
     * Update a set value.
     *
     * @param getColumn   - The column to update
     * @param newData     - New data that will replace the old
     * @param fromTable   - What table to look in
     * @param whereColumn - Select a column to pinpoint the row
     * @param equalsThis  - What the whereColumn is equal to
     */
    public void set(String getColumn, String newData, String fromTable, String whereColumn, String equalsThis) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        String sql = "UPDATE " + fromTable + " SET " + getColumn + "='" + newData + "' WHERE " + whereColumn + "='" + equalsThis + "';";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    /**
     * Get a value from the database.
     *
     * @param getColumn   - The column the data will be extracted from
     * @param fromTable   - What table to look in
     * @param whereColumn - Select a column to pinpoint the row
     * @param equalsThis  - What the whereColumn is equal to
     */
    public String get(String getColumn, String fromTable, String whereColumn, String equalsThis) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM " + fromTable + " WHERE " + whereColumn + "='" + equalsThis + "'");
        if (rs.next()) {
            String result = rs.getString(getColumn);

            rs.close();
            stmt.close();
            con.close();
            return result;
        }
        rs.close();
        stmt.close();
        con.close();

        return null;
    }

    /**
     * Get a large amount of data with one query.
     *
     * @param getColumns  - The columns the data will be extracted from
     * @param fromTable   - What table to look in
     * @param whereColumn - Select a column to pinpoint the row
     * @param equalsThis  - What the whereColumn is equal to
     */
    public ArrayList<String> getBulk(String[] getColumns, String fromTable, String whereColumn, String equalsThis) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM " + fromTable + " WHERE " + whereColumn + "='" + equalsThis + "'");
        if (rs.next()) {
            ArrayList<String> resultList = new ArrayList<>();
            for (String s : getColumns) {
                resultList.add(rs.getString(s));
            }
            rs.close();
            stmt.close();
            con.close();
            return resultList;
        }
        rs.close();
        stmt.close();
        con.close();

        return null;
    }
}

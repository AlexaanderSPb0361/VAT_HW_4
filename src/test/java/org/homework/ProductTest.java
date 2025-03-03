package org.homework;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest extends AbstractTest {

    @Test
    public void getCountProductTest() throws SQLException {
        //given
        String sql = "select * from products";
        Statement stmt = getConnection().createStatement();
        int count = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            count++;
        }
        //then
        Assertions.assertEquals(10, count);
    }

    @ParameterizedTest
    @CsvSource({"GOJIRA ROLL", "VIVA LAS VEGAS ROLL"})
    void getProductsNamesTest(String menuName) throws SQLException {
        //given
        String sql = "SELECT * FROM products WHERE menu_name='" + menuName + "'";
        Statement stmt  = getConnection().createStatement();
        String nameString = "";
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            nameString = rs.getString(2);
        }
        //then
        Assertions.assertEquals(menuName, nameString);
    }
}

package org.homework;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.persistence.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest extends AbstractTest {

    @Test
    public void getCountCustomerTest() throws SQLException {
        //given
        String sql = "select * from customers";
        Statement stmt = getConnection().createStatement();
        int count = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            count++;
        }
        //then
        Assertions.assertEquals(15, count);
    }

    @Test
    void getCustomersDistrictTest() throws SQLException {
        //given
        String sql = "SELECT * FROM customers WHERE district='Восточный'";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM customers").addEntity(CustomersEntity.class);
        //then
        Assertions.assertEquals(5, countTableSize);
    }

    @ParameterizedTest
    @CsvSource({"Penny, Smith", "Randy, Brown", "Oliver, Thompson"})
    void getCustomerNamesTest(String name, String lastName) throws SQLException {
        //given
        String sql = "SELECT * FROM customers WHERE first_name='" + name + "'";
        Statement stmt  = getConnection().createStatement();
        String nameString = "";
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            nameString = rs.getString(3);
        }
        //then
        Assertions.assertEquals(lastName, nameString);
    }

    @Test
    void addCustomerValidSaveTest() {
        //given
        CustomersEntity entity = new CustomersEntity();
        entity.setCustomerId((short) 16);
        entity.setApartment("100");
        entity.setDistrict("Южный");
        entity.setFirstName("Федор");
        entity.setLastName("Федорович");
        entity.setHouse("5");
        entity.setPhoneNumber("+7 960 800 9000");
        entity.setStreet("Центральная");
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        final Query query = getSession()
                .createSQLQuery("SELECT * FROM customers WHERE customer_id="+16).addEntity(CustomersEntity.class);
        CustomersEntity creditEntity = (CustomersEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(creditEntity);
        Assertions.assertEquals("100", creditEntity.getApartment());
    }

    @Test
    void deleteCustomerTest() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM customers WHERE customer_id=" + 16).addEntity(CustomersEntity.class);
        Optional<CustomersEntity> customersEntity = (Optional<CustomersEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(customersEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(customersEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM customers WHERE customer_id=" + 16).addEntity(CustomersEntity.class);
        Optional<CustomersEntity> customersEntityAfterDelete = (Optional<CustomersEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(customersEntityAfterDelete.isPresent());
    }

    @Test
    void addCustomerNotValidTest() {
        //given
        CustomersEntity entity = new CustomersEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
        ;
    }
}

package org.homework;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractTest {

    private static Connection connection;
    private static SessionFactory ourSessionFactory;
    private static int counter = 0;

    @BeforeAll
    static void initTest(){
        try {
            //Регистрация драйвера
            Class.forName("org.sqlite.JDBC");
            //Создание подключения
            connection = DriverManager.getConnection("jdbc:sqlite:homework.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        counter = 0;
        System.out.println("Успешное открытие базы данных");
        System.out.println("Запуск тестов");

    }

    @BeforeEach
    public void before() {
        counter++;
        System.out.println("Запуск теста: " + counter);
    }

    @AfterEach
    public void after() {
//        counter--;
        System.out.println("Тест: " + counter + " пройден");
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        connection.close();
        getSession().close();
        counter = 0;
        System.out.println("Тестирование окончено");
        System.out.println("База данных закрылась успешно");
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static Connection getConnection() {
        return connection;
    }


}

package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.*;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `users` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  `lastName` VARCHAR(45) NULL,\n" +
            "  `age` INT NULL,\n" +
            "        PRIMARY KEY (`id`))";
    private final static String DROP = "DROP TABLE IF EXISTS users;";
    private final static String ADD = "INSERT INTO users (name, lastName, age) values (?, ?, ?)";
    private final static String REMOVE_USER = "DELETE FROM users WHERE id= ?";
    private final static String SELECT = "SELECT * FROM users";
    private final static String DELETE = "DELETE FROM users";

    public UserDaoJDBCImpl() {
    }

   @Override
    public void createUsersTable() {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)){
            connection.setAutoCommit(false);
            statement.execute();
            System.out.println("Таблица создана");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement statement = connection.prepareStatement(DROP)){
            connection.setAutoCommit(false);
            statement.execute();
            System.out.println("Таблица удалена");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection.prepareStatement(ADD)) {
            connection.setAutoCommit(false);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_USER)){
            connection.setAutoCommit(false);
            statement.setLong(1,id);
            statement.executeUpdate();
            System.out.println("Пользователь удален из БД");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT)){
            connection.setAutoCommit(false);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //users.add(new User(rs.getString("name"), rs.getString("lastName"), Byte.parseByte(rs.getString("age")));
                int userId = rs.getInt("id");
                String userName = rs.getString("name");
                String userLastName = rs.getString("lastName");
                byte userAge = Byte.parseByte(rs.getString("age"));
                System.out.println("--------------------");
                System.out.println("User ID:" + userId);
                System.out.println("User name:" + userName);
                System.out.println("User lastName:" + userLastName);
                System.out.println("User age:" + userAge);
                users.add(new User(userName, userLastName, userAge));
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
            return null;
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)){
            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка создания БД" + e.getMessage());
        }
    }
}

// транзакции - роллбеки; отключить автокоммит, ручное управление транзакциями
// свойства транзакции
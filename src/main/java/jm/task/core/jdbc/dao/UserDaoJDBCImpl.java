package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Util util = new Util();
    private final Connection connection = util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS USERS " +
                "(ID INT NOT NULL AUTO_INCREMENT, " +
                " NAME VARCHAR(255), " +
                " LAST_NAME VARCHAR(255), " +
                " AGE INTEGER, " +
                " PRIMARY KEY ( ID ))";

        try (Connection connection = util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE USERS";
        try (Connection connection = util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO USERS (NAME, LAST_NAME, AGE) VALUES (?, ?, ?)";
        try (Connection connection = util.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM USERS WHERE ID = " + id;

        try (Connection connection = util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT ID, NAME, LAST_NAME, AGE FROM USERS";

        try (Connection connection = util.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setAge(resultSet.getByte("AGE"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE USERS");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

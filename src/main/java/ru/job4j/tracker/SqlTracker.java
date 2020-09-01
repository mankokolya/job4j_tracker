package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {
    private Connection connection;

    SqlTracker(){}

    SqlTracker(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Item add(Item item) {
        String insert = "insert into items(name) values (?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    item.setId(resultSet.getString("id"));
                    return item;
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        throw new IllegalStateException("Could not create new user");
    }

    @Override
    public boolean replace(String id, Item item) {
        int i = 0;
        String replace = "update items set name = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(replace)) {
            statement.setString(1, item.getName());
            statement.setInt(2, Integer.parseInt(id));
            i = statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return i != 0;
    }

    @Override
    public boolean delete(String id) {
        int i = 0;
        String delete = "delete from items where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setInt(1, Integer.parseInt(id));
            i = statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return i != 0;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String findAll = "SELECT * FROM items";
        try (PreparedStatement statement = connection.prepareStatement(findAll)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                items = itemsToList(resultSet);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = new ArrayList<>();
        String findByName = "SELECT * FROM items WHERE name = (?)";
        try (PreparedStatement statement = connection.prepareStatement(findByName)) {
            statement.setString(1, key);
            try (ResultSet resultSet = statement.executeQuery()) {
                items = itemsToList(resultSet);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return items;
    }

    @Override
    public Item findById(String id) {
        Item item = null;
        String findById = "SELECT * FROM items WHERE id = (?)";
        try (PreparedStatement statement = connection.prepareStatement(findById)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                item = new Item(resultSet.getString("name"));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return item;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    private List<Item> itemsToList(ResultSet resultSet) throws SQLException {
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            Item item = new Item(resultSet.getString("name"));
            item.setId(resultSet.getString("id"));
            items.add(item);
        }
        return items;
    }
}

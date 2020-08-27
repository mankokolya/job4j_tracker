package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {
    private Connection connection;

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
    public boolean add(Item item) {
        boolean result = false;
        String insert = "insert into items(name) values (?) returning id";
        try (PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, item.getName());
            statement.execute();
            result = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean result = false;
        String replace = "update items set name = (?) where id = (?)";
        try (PreparedStatement statement = connection.prepareStatement(replace)) {
            statement.setString(1, item.getName());
            statement.setString(2, id);
            statement.executeUpdate();
            result = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(String key) {
        boolean result = false;
        String delete = "delete from items where items.name = ?";
        try(PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setString(1, key);
            statement.execute();
            result = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String findAll = "SELECT * FROM items";
        try (PreparedStatement statement = connection.prepareStatement(findAll)) {
            final ResultSet results =  statement.executeQuery();
            while (results.next()) {
                Item item = new Item(results.getString("name"));
                item.setId(results.getString("id"));
                items.add(item);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } ;
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = new ArrayList<>();
        String findByName = "SELECT * FROM items WHERE name = (?)";
        try (PreparedStatement statement = connection.prepareStatement(findByName)) {
            statement.setString(1, key);
            final ResultSet results =  statement.executeQuery();
            while (results.next()) {
                Item item = new Item(results.getString("name"));
                items.add(item);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } ;
        return items;
    }

    @Override
    public Item findById(String id) {
        Item item = null;
        String findById = "SELECT * FROM items WHERE id = (?)";
        try (PreparedStatement statement = connection.prepareStatement(findById)) {
            statement.setInt(1, Integer.parseInt(id));
            final ResultSet results =  statement.executeQuery();
            while (results.next()) {
                item = new Item(results.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } ;
        return item;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

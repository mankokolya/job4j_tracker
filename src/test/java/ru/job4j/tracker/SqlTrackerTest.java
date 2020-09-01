package ru.job4j.tracker;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SqlTrackerTest {

    public Connection init() {
        try(InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        } throw new IllegalStateException();
    }

    @Test
    public void createItem() throws Exception {
        try(SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            tracker.add(new Item("desc"));
            List<Item> list = tracker.findByName("desc");
            assertThat(tracker.findByName("desc").size(), is(1));
        }
    }
    @Test
    public void findById() throws Exception {
        try(SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            assertThat(tracker.findById("1").getName(), is("Learn Git"));
        }
    }

    @Test
    public void deleteItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            assertTrue(tracker.delete("1"));
        }
    }

    @Test
    public void replace() throws Exception {
        try (SqlTracker tracker = new SqlTracker(ConnectionRollback.create(this.init()))) {
            assertTrue(tracker.replace("1", new Item("Learn SQL")));
        }
    }
}
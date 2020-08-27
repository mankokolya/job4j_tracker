package ru.job4j.tracker;

import java.util.List;

public interface Store  extends AutoCloseable {
    void init();
    boolean add(Item item);
    boolean replace(String id, Item item);
    boolean delete(String key);
    List<Item> findAll();
    List<Item> findByName(String key);
    Item findById(String id);
}
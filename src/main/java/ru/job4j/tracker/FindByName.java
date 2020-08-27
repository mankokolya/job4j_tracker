package ru.job4j.tracker;

import java.util.List;

public class FindByName implements UserAction {
    @Override
    public String name() {
        return "=== Find items by Name ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String name = input.askStr("Enter a name: ");
        List<Item> sameNameItems = tracker.findByName(name);
        if (sameNameItems.size() > 0) {
            for (Item element : sameNameItems) {
                System.out.println(element);
            }
        } else {
            System.out.println("There is no item with name: \" " + name + "\" in the tracker");
        }
        return true;
    }
}

package ru.job4j.tracker;

public class FindById implements UserAction {
    @Override
    public String name() {
        return "=== Find an Item by ID ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String id = input.askStr("Enter id: ");
        Item item = tracker.findById(id);
        if (item != null) {
            System.out.println(item);
        } else {
            System.out.println("Item with id: " + id + " not found in the tracker.");
        }
        return true;
    }
}

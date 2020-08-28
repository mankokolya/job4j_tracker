package ru.job4j.tracker;

public class DeleteItem implements UserAction {
    @Override
    public String name() {
        return "=== Delete an Item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String id = input.askStr("Enter id: ");
        if (tracker.delete(id)) {
            System.out.println("Item with id: " + id + " was deleted from tracker");
        } else {
            System.out.println("Item with id: " + id + " wasn't found in the tracker.");
        }
        return true;
    }
}

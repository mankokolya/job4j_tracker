package ru.job4j.tracker;

public class ReplaceItem implements UserAction {
    @Override
    public String name() {
        return "=== Replace an Item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        String id = input.askStr("Enter id: ");
        String name = input.askStr("Enter a new name: ");
        Item item = new Item(name);
        if (tracker.replace(id, item)) {
            System.out.println("Item with id: " + id + " was replaced by the item " + item.getId());
        } else {
            System.out.println("Item with id: " + id + "wasn't found in the tracker.");
        }
        return true;
    }
}

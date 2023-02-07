package carsharing;

public class Customer {
    private int id;
    private final String name;

    Customer(String name) {
        this.name = name;
    }

    Customer(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.id + ". " + name;
    }
}

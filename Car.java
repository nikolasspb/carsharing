package carsharing;

public class Car {
    private final String name;
    private final int id;

    Car(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id + ". " + name;
    }
}

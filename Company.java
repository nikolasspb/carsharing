package carsharing;

public class Company {
    private int id;
    private final String name;

    Company(String name) {
        this.name = name;
    }

    Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.id + ". " + name;
    }
}

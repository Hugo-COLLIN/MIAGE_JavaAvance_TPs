package solid_counter;

public class Model {
    
    private int counter = 0;

    public void increment() {
        this.counter++;
    }

    public void reset() {
        this.counter = 0;
    }

    public int getCounter() {
        return this.counter;
    }

}

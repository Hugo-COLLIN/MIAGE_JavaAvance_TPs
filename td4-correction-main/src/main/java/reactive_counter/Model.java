package reactive_counter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Model {
    
    private IntegerProperty counter = new SimpleIntegerProperty(0);

    public void increment() {
        this.counter.set(this.counter.add(1).get());
    }

    public void reset() {
        this.counter.set(0);
    }

    public IntegerProperty getCounter() {
        return this.counter;
    }

}

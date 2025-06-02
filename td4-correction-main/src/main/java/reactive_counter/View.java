package reactive_counter;

import javafx.beans.property.IntegerProperty;

public interface View {
    void bind(IntegerProperty prop);
    void onIncrement(Runnable r);
    void onReset(Runnable r);
}

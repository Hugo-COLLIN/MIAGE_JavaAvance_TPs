package solid_counter;

public interface View {
    void setCounter(int value);
    void onIncrement(Runnable r);
    void onReset(Runnable r);
}

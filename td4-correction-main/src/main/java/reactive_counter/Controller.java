package reactive_counter;

public class Controller {
    public Controller(View view, Model model) {
        view.onIncrement(() -> model.increment());
        view.onReset(() -> model.reset());
        view.bind(model.getCounter());
    }
}

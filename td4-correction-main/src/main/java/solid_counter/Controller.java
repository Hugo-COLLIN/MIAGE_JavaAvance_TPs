package solid_counter;

public class Controller {
    public Controller(View view, Model model) {
        view.onIncrement(() -> {
            model.increment();
            view.setCounter(model.getCounter());
        });
        view.onReset(() -> { 
            model.reset();
            view.setCounter(model.getCounter());
        });
        // ici le controlleur met explicitement la valeur à jour
        // après chaque modification (/!\ à en pas en oublier)
        // on pourrait également définir un listener sur le modèle
        // pour pouvoir réagir à la modification de sa valeur
        // model.onValueChange(() -> view.setCounter(model.getCounter()));
        // l'avantage de cette méthode et qu'on n'a plus à se préoccuper
        // de savoir quand mettre à jour la vue ^^
    }
}

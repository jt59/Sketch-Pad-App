package Sketchy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class runs the Sketchy program.
 * 
 * @author npucel
 * @updated emagaver 11/13/16
 *
 */
public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Sketchy!");
		stage.setScene(new Scene(new PaneOrganizer().getRoot()));
		stage.show();
	}
}

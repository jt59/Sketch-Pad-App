package Sketchy;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * This class sets up the root of the scene graph. It creates the different
 * parts of the application and adds the necessary {@link javafx.scene.Node
 * Node}s to the root.
 *
 * @author npucel
 *
 */
public class PaneOrganizer {

	private BorderPane _root;

	public PaneOrganizer() {
		_root = new BorderPane();
		Sketchy sketchy = new Sketchy();
		Control control = new Control(sketchy);

		// Ensures the canvas is added to the root first so that it appears
		// "behind" the control Pane.
		_root.setCenter(sketchy.getCanvas());
		_root.setLeft(control.getNode());

	}

	public Parent getRoot() {
		return _root;
	}

}

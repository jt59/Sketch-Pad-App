package Sketchy;

/**
 * Models the action of creating a new {@link SketchyShape SketchyShape}.
 *
 * @author npucel
 *
 */
public class CreateShapeCommand implements Command {

	private SketchyShape _newShape;
	private Sketchy _sketchy;

	public CreateShapeCommand(Sketchy sketchy, SketchyShape shape) {
		_sketchy = sketchy;
		_newShape = shape;
	}

	@Override
	public void undo() {
		_sketchy.deleteShape(_newShape);
	}

	@Override
	public void redo() {
		_sketchy.addShape(_newShape);
	}

}

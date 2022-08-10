package Sketchy;

/**
 * Models the action of moving a {@link SketchyShape SketchyShape} forwards.
 *
 * @author npucel
 *
 */
public class RaiseCommand implements Command {

	private Sketchy _sketchy;
	private SketchyShape _raised;

	public RaiseCommand(Sketchy sketchy, SketchyShape shape) {
		_sketchy = sketchy;
		_raised = shape;
	}

	@Override
	public void undo() {
		_sketchy.lowerShape(_raised);
	}

	@Override
	public void redo() {
		_sketchy.raiseShape(_raised);
	}

}

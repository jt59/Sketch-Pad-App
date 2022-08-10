package Sketchy;

/**
 * Models the action of moving a {@link SketchyShape SketchyShape} to the back.
 *
 * @author npucel
 *
 */
public class LowerCommand implements Command {

	private Sketchy _sketchy;
	private SketchyShape _lowered;

	public LowerCommand(Sketchy sketchy, SketchyShape shape) {
		_sketchy = sketchy;
		_lowered = shape;
	}

	@Override
	public void undo() {
		_sketchy.raiseShape(_lowered);
	}

	@Override
	public void redo() {
		_sketchy.lowerShape(_lowered);
	}

}

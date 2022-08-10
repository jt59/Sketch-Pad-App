package Sketchy;

/**
 * Models the action of deleting a {@link SketchyShape SketchyShape}.
 *
 * @author npucel
 *
 */
public class DeleteCommand implements Command {

	private SketchyShape _deleted;
	private Sketchy _sketchy;
	private Layer _shapeLayer;

	public DeleteCommand(Sketchy sketchy, SketchyShape deleted, Layer shapeLayer) {
		_sketchy = sketchy;
		_deleted = deleted;
		_shapeLayer = shapeLayer;
	}

	@Override
	public void undo() {
		_sketchy.addShape(_deleted, _shapeLayer);
	}

	@Override
	public void redo() {
		_sketchy.deleteShape(_deleted);
	}

}

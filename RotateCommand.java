package Sketchy;

/**
 * Models the action of rotating a {@link SketchyShape SketchyShape}.
 *
 * @author npucel
 *
 */
public class RotateCommand implements Command {

	private double _initRot, _endRot;
	private SketchyShape _shape;

	public RotateCommand(SketchyShape s) {
		_shape = s;
		_initRot = s.getRotate();
	}

	@Override
	public void undo() {
		_endRot = _shape.getRotate();
		_shape.setRotate(_initRot);
	}

	@Override
	public void redo() {
		_shape.setRotate(_endRot);
	}

}

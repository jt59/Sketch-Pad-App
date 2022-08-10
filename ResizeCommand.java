package Sketchy;

import javafx.geometry.Point2D;

/**
 * Models the action of resizing a {@link SketchyShape SketchyShape}.
 *
 * @author npucel
 *
 */
public class ResizeCommand implements Command {

	private SketchyShape _shape;
	private Point2D _startLoc, _endLoc;
	private double _startW, _startH, _endW, _endH;

	public ResizeCommand(SketchyShape shape) {
		_shape = shape;
		_startLoc = new Point2D(shape.getX(), shape.getY());
		_startW = _shape.getWidth();
		_startH = _shape.getHeight();
	}

	@Override
	public void undo() {
		_endLoc = new Point2D(_shape.getX(), _shape.getY());
		_endW = _shape.getWidth();
		_endH = _shape.getHeight();
		_shape.setLocation(_startLoc.getX(), _startLoc.getY());
		_shape.setSize(_startW, _startH);
	}

	@Override
	public void redo() {
		_shape.setLocation(_endLoc.getX(), _endLoc.getY());
		_shape.setSize(_endW, _endH);
	}

}

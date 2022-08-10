package Sketchy;

import java.awt.Point;

/**
 * Models the action of moving a {@link SketchyShape SketchyShape}.
 *
 * @author npucel
 *
 */
public class MoveCommand implements Command {

	private SketchyShape _shape;
	private Point _start, _end;

	public MoveCommand(SketchyShape shape) {
		_shape = shape;
		_start = new Point((int) shape.getX(), (int) shape.getY());
	}

	@Override
	public void undo() {
		_end = new Point((int) _shape.getX(), (int) _shape.getY());
		_shape.setLocation(_start.x, _start.y);
	}

	@Override
	public void redo() {
		_shape.setLocation(_end.x, _end.y);
	}

}

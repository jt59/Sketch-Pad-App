package Sketchy;

import javafx.scene.paint.Color;

/**
 * Models the action of filling a {@link SketchyShape SketchyShape} with a
 * color.
 *
 * @author npucel
 *
 */
public class FillCommand implements Command {

	private Color _old, _new;
	private SketchyShape _shape;

	public FillCommand(SketchyShape shape) {
		_shape = shape;
		_old = _shape.getFill();
	}

	@Override
	public void undo() {
		_new = _shape.getFill();
		_shape.setFill(_old);
	}

	@Override
	public void redo() {
		_shape.setFill(_new);
	}
}

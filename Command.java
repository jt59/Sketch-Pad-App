package Sketchy;

/**
 * This interface represents a command or action that can be done in this
 * application, such as creating, moving, or resizing a {@link SketchyShape
 * SketchyShape}.
 *
 * @author npucel
 *
 */
public interface Command {

	/**
	 * Undoes the command's action.
	 */
	void undo();

	/**
	 * Re-does command's action.
	 */
	void redo();

}

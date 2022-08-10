package Sketchy;

import java.util.Stack;

/**
 * This class manages {@link Command Command}s that are done in the application.
 *
 * @author npucel
 *
 */
public class CommandManager {

	private Stack<Command> _undoStack, _redoStack;

	public CommandManager() {
		_undoStack = new Stack<>();
		_redoStack = new Stack<>();
	}

	/**
	 * Stores the given {@link Command Command}.
	 *
	 * @param command
	 */
	public void pushCommand(Command command) {
		_undoStack.add(command);
		_redoStack.clear();
	}

	/**
	 * Undoes the most recently completed or re-done command.
	 */
	public void undoCommand() {
		if (_undoStack.isEmpty()) {
			return;
		}
		Command curr = _undoStack.pop();
		curr.undo();
		_redoStack.push(curr);
	}

	/**
	 * Re-does the most recently un-done command.
	 */
	public void redoCommand() {
		if (_redoStack.isEmpty()) {
			return;
		}
		Command curr = _redoStack.pop();
		curr.redo();
		_undoStack.push(curr);
	}
	
	public void clearStacks() {
		_undoStack.clear();
		_redoStack.clear();
	}
}

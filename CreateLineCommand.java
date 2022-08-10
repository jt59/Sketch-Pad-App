package Sketchy;

/**
 * Models the action of creating a new {@link CurvedLine CurvedLine}.
 *
 * @author npucel
 *
 */
public class CreateLineCommand implements Command {

    private CurvedPolyLine _line;
	private Sketchy _sketchy;

	public CreateLineCommand(CurvedPolyLine line, Sketchy sketchy) {
		_line = line;
		_sketchy = sketchy;
	}

	@Override
	public void undo() {
		_sketchy.deleteLine(_line);
	}

	@Override
	public void redo() {
		_sketchy.addLine(_line);
	}

}

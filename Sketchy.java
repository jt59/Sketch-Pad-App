package Sketchy;

import java.util.ArrayList;
import java.util.List;

//import cs015.fnl.SketchySupport.FileIO;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This is the top-level class of the application. This class defines how user
 * interaction is handled, and it manages the interactions between the
 * {@link Control Control} class and the {@link Drawer Drawer} class.
 *
 * @author npucel
 * @updated emagaver 11/13/16
 *
 */
public class Sketchy {

	/** The currently selected option on the Control */
	private SelectOption _selectedOption;

	/** The currently selected shape */
	private SketchyShape _currShape;

	/** The line currently being drawn */
	private CurvedPolyLine _currLine;

	private MouseInformation _mouseInfo;

	private CommandManager _cmdManager;

	/** The canvas on which SketchyShapes and CurvedLines are drawn */
	private Pane _canvas;

	/** A list of the SketchyShapes on the canvas */
	private List<SketchyShape> _shapes;

	/** A list of the Saveable objects on the canvas */
	private List<Saveable> _saveables;

	/** The current color as specified by the Control */
	private Color _currColor;

	public Sketchy() {
		_canvas = new Pane();
		_shapes = new ArrayList<>();
		_saveables = new ArrayList<>();
		this.setupCanvas();

		_selectedOption = SelectOption.UNKNOWN;

		_mouseInfo = new MouseInformation();
		_canvas.addEventHandler(MouseEvent.ANY, new MouseHandler());

		_cmdManager = new CommandManager();
	}

	/****************************************/
	/* MUTATORS/ACCESSORS */
	/****************************************/

	/**
	 * Retrieves the {@link javafx.scene.Node Node} that represents the canvas
	 * on which {@link SketchyShape SketchyShape}s and {@link CurvedLine
	 * CurvedLine}s are drawn.
	 *
	 * @return the canvas
	 */
	public Node getCanvas() {
		return _canvas;
	}

	/**
	 * Sets the current option.
	 *
	 * @param o
	 */
	public void setOption(SelectOption o) {
		_selectedOption = o;
	}

	/**
	 * Sets the color of the Drawer to the given Color.
	 *
	 * @param color
	 */
	public void setColor(Color color) {
		_currColor = color;
	}

	/****************************************/
	/* DRAWER OPERATIONS */
	/****************************************/

	/**
	 * Creates a new {@link SketchyShape SketchyShape} and adds it to the
	 * canvas.
	 *
	 * @param x
	 *            The x coordinate of the new shape
	 * @param y
	 *            The y coordinate of the new shape
	 */
	public void createShape(double x, double y) {
		SketchyShape newShape = this.createShape(x, y, _selectedOption);
		if (newShape != null) {
			_cmdManager.pushCommand(new CreateShapeCommand(this, newShape));
		}
	}

	/**
	 * Creates a new {@link CurvedLine CurvedLine}.
	 *
	 * @param point
	 */
	public void createLine(Point2D point) {
		CurvedPolyLine currLine = new CurvedPolyLine(_currColor);
		currLine.addPoint(point);
		_saveables.add(currLine);
		_canvas.getChildren().add(currLine.getNodes());
		_currLine = currLine;
		_cmdManager.pushCommand(new CreateLineCommand(currLine, this));
	}

	/**
	 * Fills the currently selected shape with the current color.
	 */
	public void fillCurrentShape() {
		if (_currShape == null) {
			return;
		}
		_cmdManager.pushCommand(new FillCommand(_currShape));
		this.fillShape(_currShape);
	}

	/**
	 * Deletes the current shape from the canvas.
	 */
	public void deleteCurrentShape() {
		if (_currShape == null) {
			return;
		}
		Layer shapeLayer = this.getShapeLayer(_currShape);
		_cmdManager
				.pushCommand(new DeleteCommand(this, _currShape, shapeLayer));
		this.deleteShape(_currShape);
	}

	/**
	 * Brings the current shape to the front of all shapes and lines.
	 */
	public void raiseCurrentShape() {
		if (_currShape == null) {
			return;
		}
		this.raiseShape(_currShape);
		_cmdManager.pushCommand(new RaiseCommand(this, _currShape));
	}

	/**
	 * Moves the current shape behind all shapes and lines.
	 */
	public void lowerCurrentShape() {
		if (_currShape == null) {
			return;
		}
		this.lowerShape(_currShape);
		_cmdManager.pushCommand(new LowerCommand(this, _currShape));
	}

	private void rotateShape(Point2D curr) {
		if (!_mouseInfo._commandStarted) {
			_cmdManager.pushCommand(new RotateCommand(_currShape));
		}
		this.rotateShape(_mouseInfo._prev, curr);
	}

	private void resizeShape(Point2D curr) {
		if (!_mouseInfo._commandStarted) {
			_cmdManager.pushCommand(new ResizeCommand(_currShape));
		}
		this.resizeShape(_mouseInfo._prev, curr);
	}

	private void translateShape(Point2D curr) {
		if (!_mouseInfo._commandStarted) {
			_cmdManager.pushCommand(new MoveCommand(_currShape));
		}
		this.moveShape(_mouseInfo._prev, curr);
	}

	private void setupCanvas() {
		_canvas.setStyle("-fx-background-color: white;");
		_canvas.setPrefSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
	}

	private SketchyShape currentShape() {
		return _currShape;
	}

	/**
	 * Returns the canvas (the Pane on which {@link SketchyShape SketchyShape}s
	 * and {@link CurvedLine CurvedLine}s are drawn) so that it can be added to
	 * the root of the application.
	 *
	 * @return the canvas of this application
	 */
	public Node getNode() {
		return _canvas;
	}

	/**
	 * Draws a line segment between two points and appends it to the
	 * {@link CurvedLine CurvedLine} that is currently being drawn.
	 *
	 * @param prev
	 *            The start point
	 * @param curr
	 *            The end point
	 */
	public void drawLine(Point2D curr) {
		_currLine.addPoint(curr);
	}

	/**
	 * Creates a new {@link SketchyShape SketchyShape} and adds it to the
	 * canvas.
	 *
	 * @param x
	 *            The x coordinate of the new shape
	 * @param y
	 *            The y coordinate of the new shape
	 * @param option
	 *            The currently selected {@link SelectOption option}
	 * @return the newly created {@link SketchyShape SketchyShape}
	 */
	public SketchyShape createShape(double x, double y, SelectOption option) {
		SketchyShape newShape = null;
		switch (option) {
		case RECTANGLE:
			newShape = new SketchyRectangle(_currColor, x, y);
			break;
		case ELLIPSE:
			newShape = new SketchyEllipse(_currColor, x, y);
			break;
		default:
			System.err.println("Error in Drawer.createShape(): invalid shape");
			return null;
		}
		this.addShape(newShape);
		this.setCurrentShape(newShape);
		return newShape;
	}

	/**
	 * Resizes the currently selected shape.
	 *
	 * @param prev
	 *            The start point
	 * @param curr
	 *            The end point
	 */
	public void resizeShape(Point2D prev, Point2D curr) {
		this.currentShape().resize(prev, curr);
	}

	/**
	 * Moves the currently selected shape.
	 *
	 * @param prev
	 *            The start point
	 * @param curr
	 *            The end point
	 */
	public void moveShape(Point2D prev, Point2D curr) {
		this.currentShape().translate(prev, curr);
	}

	/**
	 * Rotates the currently selected shape.
	 *
	 * @param prev
	 *            The start point
	 * @param curr
	 *            The end point
	 */
	public void rotateShape(Point2D prev, Point2D curr) {
		this.currentShape().rotate(prev, curr);
	}

	/**
	 * Selects the {@link SketchyShape SketchyShape} on the canvas that contains
	 * the given location. If multiple {@link SketchyShape SketchyShape}s
	 * contain this {@link javafx.geometry.Point2D Point2D}, the
	 * {@link SketchyShape SketchyShape} closest to the front will be selected.
	 *
	 * @param point
	 */
	public void selectShape(Point2D point) {
		// The shape "in front" is at the end of the shapes list.
		this.deselectCurrentShape();
		for (int i = _shapes.size() - 1; i >= 0; i--) {
			SketchyShape shape = _shapes.get(i);
			if (shape.contains(point)) {
				this.setCurrentShape(shape);
				break;
			}
		}
	}

	/**
	 * Deselects the current shape.
	 */
	private void deselectCurrentShape() {
		if (this.currentShape() != null) {
			this.currentShape().setStrokeWidth(0);
		}
		_currShape = null;
	}

	/**
	 * Sets the current shape.
	 *
	 * @param shape
	 *            The shape to select
	 */
	private void setCurrentShape(SketchyShape shape) {
		this.deselectCurrentShape();
		_currShape = shape;
		this.currentShape().setStrokeWidth(Constants.SELECTED_STROKE_WIDTH);
	}

	/**
	 * Adds a {@link SketchyShape SketchyShape} to the canvas, in front of all
	 * other shapes/lines.
	 *
	 * @param shape
	 *            The shape to add to the canvas
	 */
	public void addShape(SketchyShape shape) {
		Layer shapeLayer = new Layer();
		shapeLayer.setShapesIndex(_shapes.size());
		shapeLayer.setSaveablesIndex(_saveables.size());
		shapeLayer.setChildrenIndex(_canvas.getChildren().size());
		this.addShape(shape, shapeLayer);
	}

	/**
	 * Adds a {@link SketchyShape SketchyShape} to the canvas.
	 *
	 * @param shape
	 *            The shape to add to the canvas
	 * @param shapesIndex
	 *            The index at which to insert the shape in the list of shapes
	 * @param saveablesIndex
	 *            The index at which to insert the shape in the list of
	 *            saveables
	 */
	/**
	 * Adds a {@link SketchyShape SketchyShape} to the canvas.
	 *
	 * @param shape
	 *            The shape to add to the canvas
	 * @param shapeLayer
	 *            An object representing the layer of the shape
	 */
	public void addShape(SketchyShape shape, Layer shapeLayer) {
		int shapesIndex = shapeLayer.getShapesIndex();
		int saveablesIndex = shapeLayer.getSaveablesIndex();
		int childrenIndex = shapeLayer.getChildrenIndex();
		_shapes.add(shapesIndex, shape);
		_saveables.add(saveablesIndex, shape);
		_canvas.getChildren().addAll(childrenIndex, shape.getNodes());
	}

	/**
	 * Deletes a {@link SketchyShape SketchyShape} from the canvas.
	 *
	 * @param shape
	 *            The shape to delete from the canvas
	 */
	public void deleteShape(SketchyShape shape) {
		_shapes.remove(shape);
		_saveables.remove(shape);
		_canvas.getChildren().removeAll(shape.getNodes());
		if (shape == this.currentShape()) {
			this.deselectCurrentShape();
		}
	}

	/**
	 * Fills a {@link SketchyShape SketchyShape} with the current color.
	 *
	 * @param shape
	 *            the SketchyShape to be filled.
	 */
	public void fillShape(SketchyShape shape) {
		shape.setFill(_currColor);
	}

	/**
	 * Bring a {@link SketchyShape SketchyShape} forwards
	 *
	 * @param shape
	 *            The shape to move
	 */
	public void raiseShape(SketchyShape shape) {
		// we store the layer we want the shape to move to
		Layer newLayer = new Layer();
		Node node = shape.getNodes().get(0);
		// math.min prevents indexoutofbounds
		newLayer.setChildrenIndex(Math.min(
				_canvas.getChildren().indexOf(node) + 1, _canvas.getChildren()
						.size() - 1));
		newLayer.setShapesIndex(Math.min(_shapes.indexOf(shape) + 1,
				_shapes.size() - 1));
		newLayer.setSaveablesIndex(Math.min(_saveables.indexOf(shape) + 1,
				_saveables.size() - 1));
		// then we actually move the shape to that layer
		this.moveShapeToLayer(shape, newLayer);
	}

	/**
	 * Moves a {@link SketchyShape SketchyShape} backwards
	 *
	 * @param shape
	 *            The shape to move
	 */
	public void lowerShape(SketchyShape shape) {
		// we store the layer we want the shape to move to
		Layer newLayer = new Layer();
		Node node = shape.getNodes().get(0);
		// math.max prevents indexoutofbounds
		newLayer.setChildrenIndex(Math.max(
				_canvas.getChildren().indexOf(node) - 1, 0));
		newLayer.setShapesIndex(Math.max(_shapes.indexOf(shape) - 1, 0));
		newLayer.setSaveablesIndex(Math.max(_saveables.indexOf(shape) - 1, 0));
		// then we actually move the shape to that layer
		this.moveShapeToLayer(shape, newLayer);
	}

	/**
	 * Moves a shape to a layer in the 3 data structures.
	 *
	 * @param shape
	 * @param shapeLayer
	 *            the layer (consisting of 3 indices) to move the shape to
	 */
	public void moveShapeToLayer(SketchyShape shape, Layer shapeLayer) {
		_shapes.remove(shape);
		_saveables.remove(shape);
		_canvas.getChildren().removeAll(shape.getNodes());
		int shapesIndex = shapeLayer.getShapesIndex();
		int saveablesIndex = shapeLayer.getSaveablesIndex();
		int childrenIndex = shapeLayer.getChildrenIndex();
		_shapes.add(shapesIndex, shape);
		_saveables.add(saveablesIndex, shape);
		_canvas.getChildren().addAll(childrenIndex, shape.getNodes());
	}

	/**
	 * Gets the layer of the passed in shape.
	 *
	 * @param shape
	 * @return
	 */
	public Layer getShapeLayer(SketchyShape shape) {
		Layer layer = new Layer();
		layer.setShapesIndex(_shapes.indexOf(shape));
		layer.setSaveablesIndex(_saveables.indexOf(shape));
		layer.setChildrenIndex(_canvas.getChildren().indexOf(
				shape.getNodes().get(0)));
		return layer;
	}

	/**
	 * Deletes a given {@link CurvedLine CurvedLine} from the canvas.
	 *
	 * @param line
	 *            The CurvedLine to delete
	 */
	public void deleteLine(CurvedPolyLine line) {
		_saveables.remove(line);
		_canvas.getChildren().removeAll(line.getNodes());
	}

	/**
	 * Adds a given {@link CurvedPolyLine CurvedPolyLine} to the canvas.
	 *
	 * @param line
	 *            The {@link CurvedPolyLine CurvedPolyLine} to add
	 */
	public void addLine(CurvedPolyLine line) {
		_saveables.add(line);
		_canvas.getChildren().addAll(line.getNodes());
	}

	/**
	 * Clears the canvas.
	 */
	public void clearCanvas() {
		_canvas.getChildren().clear();
		_shapes.clear();
		_saveables.clear();

		// otherwise, you can hit undo and then redo and a random shape will pop
		// up out of nowhere
		_cmdManager.clearStacks();
	}

	/**
	 * Rotates a {@linkplain javafx.geometry.Point2D point} around another point
	 * by the given angle.
	 *
	 * @param toRotate
	 *            the {@linkplain javafx.geometry.Point2D point} that is
	 *            rotating
	 * @param rotateAround
	 *            the {@linkplain javafx.geometry.Point2D point} that toRotate
	 *            is being rotated around
	 * @param angle
	 *            the angle by which to rotate toRotate (in degrees).
	 * @return
	 */
	public static final Point2D rotatePoint(Point2D toRotate,
			Point2D rotateAround, double angle) {
		double sine = Math.sin((Math.PI / 180) * angle);
		double cosine = Math.cos((Math.PI / 180) * angle);
		Point2D toReturn = new Point2D(toRotate.getX() - rotateAround.getX(),
				toRotate.getY() - rotateAround.getY());
		toReturn = new Point2D(toReturn.getX() * cosine + toReturn.getY()
				* sine, (-1) * toReturn.getX() * sine + toReturn.getY()
				* cosine);
		toReturn = new Point2D(toReturn.getX() + rotateAround.getX(),
				toReturn.getY() + rotateAround.getY());
		return toReturn;
	}

	/****************************************/
	/* SAVING/LOADING */
	/****************************************/

	/**
	 * Saves the current state of the canvas.
	 */
	public void save() {
		String fname = FileIO.getFileName(true, _canvas.getScene().getWindow());
		if (fname == null) {
			// cancel pressed
			return;
		}
		FileIO io = new FileIO();
		io.openWrite(fname);
		for (Saveable s : _saveables) {
			s.save(io);
		}
		io.closeWrite();
	}

	/**
	 * Loads a file and draws its contents on the canvas.
	 */
	public void load() {
		String fname = FileIO
				.getFileName(false, _canvas.getScene().getWindow());
		if (fname == null) {
			// cancel pressed
			return;
		}
		this.clearCanvas();
		FileIO io = new FileIO();
		io.openRead(fname);
		while (io.hasMoreData()) {
			String shapeType = io.readString();
			switch (shapeType) {
			case "RECTANGLE":
			case "ELLIPSE":
				SketchyShape shape = Sketchy.loadShape(shapeType, io);
				if (shape == null) {
					this.clearCanvas();
					return;
				}
				this.addShape(shape);
				break;
			case "LINE":
				this.addLine(Sketchy.loadLine(io));
				break;
			default:
				System.err.println("Error in Drawer.load(): invalid shape");
				return;
			}
		}
		io.closeRead();
	}

	private static SketchyShape loadShape(String shapeType, FileIO io) {
		// Assumes io is already open for reading.
		SketchyShape shape = null;
		switch (shapeType) {
		case "RECTANGLE":
			shape = new SketchyRectangle();
			break;
		case "ELLIPSE":
			shape = new SketchyEllipse();
			break;
		default:
			System.err.println("Error in Drawer.load(): invalid shape");
			return null;
		}
		double x = io.readDouble();
		double y = io.readDouble();
		double width = io.readDouble();
		double height = io.readDouble();
		double rotation = io.readDouble();
		Color color = Color.rgb(io.readInt(), io.readInt(), io.readInt());
		shape.setFill(color);
		shape.setSize(width, height);
		shape.setRotate(rotation);
		shape.setLocation(x, y);
		return shape;
	}

	private static CurvedPolyLine loadLine(FileIO io) {
		// Assumes io is already open for reading.
		CurvedPolyLine line = new CurvedPolyLine();
		line.setColor(Color.rgb(io.readInt(), io.readInt(), io.readInt()));
		int numPoints = io.readInt();
		ArrayList<Double> points = new ArrayList<Double>();
		for (int i = 0; i < numPoints; i++) {
			points.add(io.readDouble());
		}
		line.addPoints(points);
		return line;
	}

	/****************************************/
	/* UNDO/REDO */
	/****************************************/

	/**
	 * Undoes the most recently completed or re-done command.
	 */
	public void undoCommand() {
		_cmdManager.undoCommand();
	}

	/**
	 * Re-does the most recently un-done command.
	 */
	public void redoCommand() {
		_cmdManager.redoCommand();
	}

	/****************************************/
	/* MOUSE METHODS */
	/****************************************/

	private void mousePressed(Point2D point) {
		switch (_selectedOption) {
		case SELECT:
			Sketchy.this.selectShape(point);
			break;
		case PEN:
			this.createLine(point);
			break;
		case ELLIPSE:
		case RECTANGLE:
			Sketchy.this.createShape(point.getX(), point.getY());
			break;
		default:
		}
	}

	private void mouseDragged(Point2D curr, boolean isShiftDown,
			boolean isControlDown) {
		switch (_selectedOption) {
		case SELECT:
			if (_currShape == null) {
				return;
			}
			if (isShiftDown) {
				this.resizeShape(curr);
			} else if (isControlDown) {
				this.rotateShape(curr);
			} else {
				this.translateShape(curr);
			}
			break;
		case PEN:
			Sketchy.this.drawLine(curr);
			break;
		case RECTANGLE:
		case ELLIPSE:
			// Shape is being created
			Sketchy.this.resizeShape(_mouseInfo._prev, curr);
			break;
		default:
		}
	}

	/**
	 * Simple wrapper class to hold needed information about previous
	 * MouseEvents, such as the previous point, the point at which the mouse was
	 * pressed, and whether or not a command (drag) has started.
	 *
	 * @author npucel
	 *
	 */
	private class MouseInformation {

		private Point2D _prev;
		private boolean _commandStarted;

		public void setMousePressedPoint(Point2D pressedPoint) {
			_prev = pressedPoint;
		}

		public void reset() {
			_commandStarted = false;
			_prev = null;
		}
	}

	/**
	 * The {@link javafx.event.EventHandler EventHandler} that determines the
	 * handling of {@link javafx.scene.input.MouseEvent MouseEvent}s in Sketchy.
	 * Should be added to the {@link Drawer Drawer}'s "canvas."
	 *
	 * @author npucel
	 *
	 */
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent e) {
			EventType<? extends MouseEvent> type = e.getEventType();
			if (type == MouseEvent.MOUSE_PRESSED) {
				this.mousePressed(e);
			} else if (type == MouseEvent.MOUSE_DRAGGED) {
				this.mouseDragged(e);
			}
		}

		private void mouseDragged(MouseEvent e) {
			Point2D curr = this.getMousePoint(e);
			Sketchy.this.mouseDragged(curr, e.isShiftDown(), e.isControlDown());
			_mouseInfo._prev = curr;
			_mouseInfo._commandStarted = true;
		}

		private void mousePressed(MouseEvent e) {
			_mouseInfo.reset();
			Point2D curr = this.getMousePoint(e);
			Sketchy.this.mousePressed(curr);
			_mouseInfo.setMousePressedPoint(curr);
		}

		private Point2D getMousePoint(MouseEvent e) {
			return new Point2D(e.getX(), e.getY());
		}

	}
}

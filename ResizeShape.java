package Sketchy;
//Command that's pushed onto the undo stack when a shape is selected with shift down
public class ResizeShape implements Command{
	private double _x;
	private double _y;
	private SketchyShape _shape;
	private int _currShape;
	private Sketchy _sketchy;
	private double _height;
	private double _width;
	private double _newx;
	private double _newy;
	private double _newHeight;
	private double _newWidth;
		
	public ResizeShape(Sketchy sketchy, SketchyShape shape, double x, double y, double height, double width, int currShape){
		_x = x;
		_y = y;
		_shape = shape;
		_currShape = currShape;
		_sketchy = sketchy;
		_height = height;
		_width = width;
	}
//returns the shape to its original size	
	@Override
	public void undo(){
		_sketchy.setCurrShape(_currShape);
		_newx = _shape.shapeX();
		_newy = _shape.shapeY();
		_newHeight = _shape.getHeight();
		_newWidth = _shape.getWidth();
		_shape.setHeight(_height);
		_shape.setWidth(_width);
		_shape.setX(_x);
		_shape.setY(_y);		
	}

//	returns the shape to its new size
	@Override
	public void redo(){
		_sketchy.setCurrShape(_currShape);
		_shape.setX(_newx);
		_shape.setY(_newy);
		_shape.setHeight(_newHeight);
		_shape.setWidth(_newWidth);
	}
}
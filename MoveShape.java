package Sketchy;
//Command that's pushed onto the undo stack when a shape is moved
public class MoveShape implements Command{
	private double _x;
	private double _y;
	private SketchyShape _shape;
	private int _currShape;
	private Sketchy _sketchy;
	private double _newx;
	private double _newy;
	
	public MoveShape(Sketchy sketchy, SketchyShape shape, double x, double y, int currShape){
		_x = x;
		_y = y;
		_shape = shape;
		_currShape = currShape;
		_sketchy = sketchy;
	}
//returns the shape to its original position	
	@Override
	public void undo(){
		_sketchy.setCurrShape(_currShape);
		_newx = _shape.shapeX();
		_newy = _shape.shapeY();
		_shape.setX(_x);
		_shape.setY(_y);
	}
//puts the shape back to where it was when it was moved	
	@Override
	public void redo(){
		_sketchy.setCurrShape(_currShape);
		_shape.setX(_newx);
		_shape.setY(_newy);
	}
	
}
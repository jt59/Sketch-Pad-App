package Sketchy;

//Command that's pushed onto the undo stack when a shape is selected with control down
public class RotateShape implements Command{
	private double _angle;
	private SketchyShape _shape;
	private Sketchy _sketchy;
	private int _currShape;
	private double _newAngle;
	
	public RotateShape(Sketchy sketchy, SketchyShape shape, double angle, int currShape){
		_sketchy = sketchy;
		_shape = shape;
		_angle = angle;
		_currShape = currShape;
	}

//	sets the shape back to its previous angle
	@Override
	public void undo(){
		_sketchy.setCurrShape(_currShape);
		_newAngle = _shape.getAngle();
		_shape.rotateShape(_angle);
		
	}
//sets the shape to its new angle	
	@Override
	public void redo(){
		_sketchy.setCurrShape(_currShape);
		_shape.rotateShape(_newAngle);
	}
}
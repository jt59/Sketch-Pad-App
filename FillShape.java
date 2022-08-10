package Sketchy;

import javafx.scene.paint.Color;

public class FillShape implements Command{
	private Sketchy _sketchy;
	private SketchyShape _shape;
	private Color _color;
	private Color _newColor;
	private int _currShape;

//	Command that's pushed onto the undo stack when a shape is filled
	public FillShape(Sketchy sketchy, SketchyShape shape, Color color, int currShape){
		_sketchy = sketchy;
		_shape = shape;
		_color = color;
		_currShape = currShape;
	}

//	returns the shape to its original color
	@Override
	public void undo(){
		_sketchy.setCurrShape(_currShape);
		_newColor = _shape.getColor();
		_shape.newColor(_color);
	}

//	fills the shape with its new color
	@Override
	public void redo(){
		_sketchy.setCurrShape(_currShape);
		_shape.newColor(_newColor);
	}
	
	
}
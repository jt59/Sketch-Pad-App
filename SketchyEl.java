package Sketchy;

import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class SketchyEl implements SketchyShape, Saveable{
	private double _x;
	private double _y;
	private Ellipse _el;
	private Pane _pane;
	private boolean _selected;
	private double _angle;
	private Color _color;

//constructs and initializes an ellipse. Adds it to the pane	
	public SketchyEl(double x, double y, Color color, Pane pane){
		_pane = pane;
		 _el = new Ellipse(Constants.EL_DIM,Constants.EL_DIM);
		 _x = x;
		 _y = y;
		 _color = color;
		 _el.setCenterX(_x);
		 _el.setCenterY(_y);
		 _el.setFill(_color);
		 _el.setStroke(Color.BLACK);
		 _el.setStrokeWidth(2);
		 _pane.getChildren().add(_el);
	}

//	sets the dimensions of the ellipse depending on where the mouse is dragged
	@Override
	public void create(MouseEvent event){
		double x = event.getSceneX() - 200 - _x + Constants.RECT_DIM/2;
		 if(x < 0){
			 x = -x;
		 }
		 double y = event.getSceneY() - _y + Constants.RECT_DIM/2;
		 if(y < 0){
			 y = -y;
		 }
		 _el.setRadiusX(Constants.EL_DIM + 2*x);
		 _el.setRadiusY(Constants.EL_DIM + 2*y);
	}

//	adds shape to the pane
	@Override
	public void addtoPane(){
		_pane.getChildren().add(_el);
	}
//removes shape from the pane	
	@Override
	public void delete(){
		_pane.getChildren().remove(_el);
	}
	
//reorders the pane to correspond with the savelist	
	@Override
	public void reorderPane(ArrayList<Saveable> saveList){
		_pane.getChildren().clear();
		for(int i = 0; i <saveList.size(); i++){
			saveList.get(i).addtoPane();
		}
	}

//	logically and graphically selects a SketchyEl
	@Override
	public void select(){
		_el.setStroke(Color.BLACK);
		_el.setStrokeWidth(2);
		_selected = true;
	}

//	logically and graphically deselects SketchyEl
	@Override
	public void deselect(){
		_el.setStroke(null);
		_selected = false;
	}

//	rotates one point around another point. Useful in the isContained() method.
	private double[] rotatePoint(double[] pRotate, double angle, double[] pAround){
		double prx = pRotate[0];
		double pry = pRotate[1];
		double pax = pAround[0];
		double pay = pAround[1];
		double sin = Math.sin(Math.toRadians(angle));
		double cos = Math.cos(Math.toRadians(angle));
		double[] point = new double[]{prx - pax, pry - pay};
		double pointx = point[0];
		point[0] = point[0]*cos + point[1]*sin;
		point[1] = -pointx*sin + point[1]*cos;
		point[0] = point[0] + pax;
		point[1] = point[1] + pay;
		return point;
	}

//	determines whether a shape contains a mouse click. rotatePoint() is used to rotate the click around the shape's
//	center to account for the angle of rotation.
	@Override
	public boolean isContained(MouseEvent event){
		double x = event.getSceneX() - 200;
		double y = event.getSceneY();
		double angle = this.getAngle();
		double[] point = new double[]{x,y};
		if(angle !=0){
			double[] center = new double[]{_el.getCenterX(), _el.getCenterY()};
			point = this.rotatePoint(point, angle, center);
		}
		if(_el.contains(point[0],point[1])){
			_el.setStroke(Color.BLACK);
			_el.setStrokeWidth(2);
			return true;
		}
		else{
			this.deselect();
			return false;
		}
	}

//	returns whether or not the shape is logically selected
	@Override
	public boolean isSelected(){
		return _selected;
	}

//	various setters and getters for shape dimensions/location
	@Override
	public double shapeX(){
		return _el.getCenterX();
	}
	
	@Override
	public double shapeY(){
		return _el.getCenterY();
	}
	
	@Override
	public void setX(double x){
		_el.setCenterX(x);
	}
	
	@Override
	public void setY(double y){
		_el.setCenterY(y);
	}
	
	@Override
	public double getWidth(){
		return _el.getRadiusX();
	}
	
	@Override
	public double getHeight(){
		return _el.getRadiusY();
	}
	
	@Override
	public void setWidth(double width){
		_el.setRadiusX(width);
	}
	
	@Override
	public void setHeight(double height){
		_el.setRadiusY(height);
	}

//	moves the SketchyEl depending on where the mouse is dragged
	@Override
	public void translate(MouseEvent event, double[] rel){
		_el.setCenterX(event.getSceneX() - rel[0]);
		_el.setCenterY(event.getSceneY() - rel[1]);
	}

//	returns the shape's current color
	@Override
	public Color getColor(){
		return _color;
	}

//	sets the fill of the shape to a new color
	@Override
	public void newColor(Color color){
		_el.setFill(color);
		_color = color;
	}

//	rotates the shape depending on where the mouse is dragged
	@Override
	public void rotate(MouseEvent event, double[] init, double[] dim, double[] rel, double prevAngle){
		double initx = init[0];
		double inity = init[1];
		double currx = event.getSceneX();
		double curry = event.getSceneY();
		double relx = rel[0];
		double rely = rel[1];
		double centerx = initx - relx + 200;
		double centery = inity - rely;
		_angle = Math.atan2(inity - centery, initx - centerx) - Math.atan2(curry - centery, currx - centerx);
		_angle = -_angle * 180/3.1415 + prevAngle;
		_el.setRotate(_angle);
	}

//	rotates the shape without dependence on a mouse event
	@Override
	public void rotateShape(double angle){
		_el.setRotate(angle);
		_angle = angle;
	}
	
//returns the SketchyEl's angle of rotation	
	@Override
	public double getAngle(){
		return _angle;
	}
	
//resizes the shape depending on where the mouse is dragged. Uses the shape's previous dimensions/location as references	
	@Override
	public void resize(MouseEvent event, double[] init, double[] dim, double[] rel){
		double[] curr = new double[]{event.getSceneX(),event.getSceneY()};
		double width = dim[0];
		double height = dim[1];
		double centerx = init[0] - rel[0] + 200;
		double centery = init[1] - rel[1];	
		double angle  = this.getAngle();
		double[] center = new double[]{centerx,centery};
		curr = this.rotatePoint(curr, angle, center);
		init = this.rotatePoint(init,angle,center);
		double currx = curr[0];
		double curry = curr[1];
		double initx = init[0];
		double inity = init[1];		
		double x = currx - initx;
		if(centerx - initx < 0){
			if(width + x >= 0){
				_el.setRadiusX(width + x);
			}
			else{
				_el.setRadiusX(-(width + x));
			}
		}
		else{
			
			x = -x;
			if(width + x >= 0){
				_el.setRadiusX(width + x);
			}
			else{
				_el.setRadiusX(-(width + x));
			}
		}
		double y = curry - inity;
		if(centery - inity < 0){
			if(height + y >=0){
				_el.setRadiusY(height + y);
			}
			else{
				_el.setRadiusY(-(height + y));
				
			}
		}
		else{
			y = -y;
			if(height + y >= 0){
				_el.setRadiusY(height + y);
			}
			else{
				_el.setRadiusY(-(height + y));
			}
		}		
	}

//	returns all of the dimensions needed in order to reconstruct the shape
	@Override
	public double[] saveDimensions(){
		return new double[]{9, _el.getCenterX(),_el.getCenterY(),_el.getRadiusY(),_el.getRadiusX(),_angle,_color.getRed(), _color.getGreen(), _color.getBlue()};
	}
	
//	returns the name of the shape 
	@Override
	public String saveName(){
		return "Ellipse";
	}
}
package Sketchy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.Math;
import java.util.ArrayList;

//SketchyRect is both a SketchyShape and a Saveable
public class SketchyRect implements SketchyShape, Saveable{
	private double _x;
	private double _y;
	private Rectangle _rect;
	private Pane _pane;
	private boolean _selected;
	private double _angle;
	private Color _color;

//	creates the rectangle and adds it to the pane in response to a mouse click. Everything is initialized and the shape
//	is outlined
	public SketchyRect(double x, double y, Color color, Pane pane){
		 _rect = new Rectangle(Constants.RECT_DIM,Constants.RECT_DIM);
		 _x = x;
		 _y = y;
		 _color = color;
		 _pane = pane;
		 _rect.setX(_x);
		 _rect.setY(_y);
		 _rect.setFill(_color);
		 _rect.setStroke(Color.BLACK);
		 _rect.setStrokeWidth(2);
		 _pane.getChildren().add(_rect);
		 _angle = 0;
	}

//dynamically creates the rectangle as the user is dragging the mouse.
	@Override
	public void create(MouseEvent event){
		double x = event.getSceneX() - 200 - _x;
		 if(x < 0){
			 x = -x;
		 }
		 double y = event.getSceneY() - _y;
		 if(y < 0){
			 y = -y;
		 }
		 _rect.setWidth(Constants.RECT_DIM + 2*x);
		 _rect.setHeight(Constants.RECT_DIM + 2*y);
		 _rect.setX(_x - x);
		 _rect.setY(_y - y);
	}

//	graphically and logically de-selects the rectangle
	@Override
	public void deselect(){
		_rect.setStroke(null);
		_selected = false;
	}
	
//	rearranges the pane so that it corresponds to the saveList. Useful in raising and lowering shapes
	@Override
	public void reorderPane(ArrayList<Saveable> saveList){
		_pane.getChildren().clear();
		for(int i = 0; i <saveList.size(); i++){		
			saveList.get(i).addtoPane();
		}
	}

//	returns true if the mouse is clicked within a shape. Takes rotation into account with rotatePoint() method
	@Override
	public boolean isContained(MouseEvent event){
		double x = event.getSceneX() - 200;
		double y = event.getSceneY();
		double angle = this.getAngle();
		double[] point = new double[]{x,y};
		if(angle !=0){
			//double[] click = new double[]{x,y};
			double[] center = new double[]{_rect.getX() + _rect.getWidth()/2, _rect.getY() + _rect.getHeight()/2};
			point = this.rotatePoint(point, angle, center);
		}
		
		if(_rect.contains(point[0],point[1])){	
			return true;
		}
		else{
			return false;
		}
	}

//	returns whether or not the shape is logically selected
	@Override
	public boolean isSelected(){
		return _selected;
	}

//	sets the x location
	@Override
	public void setX(double x){
		_rect.setX(x);
	}
//sets the y location	
	@Override
	public void setY(double y){
		_rect.setY(y);
	}

//gets the x location
	@Override
	public double shapeX(){
		return _rect.getX();
	}
	
//gets the y location	
	@Override
	public double shapeY(){
		return _rect.getY();
	}
	
	@Override
	public double getWidth(){
		return _rect.getWidth();
	}
	
	@Override
	public void setWidth(double width){
		_rect.setWidth(width);
	}
	
	@Override
	public void setHeight(double height){
		_rect.setHeight(height);
	}
	
	@Override
	public double getHeight(){
		return _rect.getHeight();
	}

//	rotates one point around another point (the center of the shape). This is useful in determining whether the mouse 
//	was clicked in a rotated shape.
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

//	resizes the shape, using the initial shape dimensions as references
	@Override
	public void resize(MouseEvent event,  double[] init, double[] dim, double[] rel){
		double[] curr = new double[]{event.getSceneX(),event.getSceneY()};
		double width = dim[0];
		double height = dim[1];
		double shapex = init[0] - rel[0];
		double shapey = init[1] - rel[1];	
		double centerx = shapex + width/2 + 200;
		double centery = shapey + height/2;
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
			if(width + 2*x >= 0){
				_rect.setWidth(width + 2*x);
				_rect.setX(shapex - x);
			}
			else{
				_rect.setWidth(-(width + 2*x));
				_rect.setX(currx + shapex + width - initx);
			}
		}
		else{
			x = -x;
			if(width + 2*x >= 0){
				_rect.setWidth(width + 2*x);
				_rect.setX(shapex - x);
			}
			else{
				_rect.setWidth(-(width + 2*x));
				_rect.setX(shapex + width + x);
			}
		}
		double y = curry - inity;
		if(centery - inity < 0){
			if(height + 2*y >=0){
				_rect.setHeight(height + 2*y);
				_rect.setY(shapey - y);
			}
			else{
				_rect.setHeight(-(height + 2*y));
				_rect.setY(curry + height + shapey - inity);	
			}
		}
		else{
			y = -y;
			if(height + 2*y >= 0){
				_rect.setHeight(height + 2*y);
				_rect.setY(shapey - y);
			}
			else{
				_rect.setHeight(-(height + 2*y));
				_rect.setY(shapey + height + y);
			}
		}
		
	}

//	graphically and logically selects a rectangle
	@Override
	public void select(){
		_rect.setStroke(Color.BLACK);
		_rect.setStrokeWidth(2);
		_selected = true;
	}
	
//moves the shape depending on where the mouse is dragged	
	@Override
	public void translate(MouseEvent event, double[] rel){
		_rect.setX(event.getSceneX() - rel[0]);
		_rect.setY(event.getSceneY() - rel[1]);
	}
	
//	rotates the shape depending on where the mouse is dragged. Uses initial shape dimensions to find the angle of
//	rotation
	@Override
	public void rotate(MouseEvent event, double[] init, double[] dim, double[] rel, double prevAngle){
		double initx = init[0];
		double inity = init[1];
		double currx = event.getSceneX();
		double curry = event.getSceneY();
		double relx = rel[0];
		double rely = rel[1];
		double width = dim[0];
		double height = dim[1];
		double shapex = initx - relx;
		double shapey = inity - rely;
		double centerx = shapex + width/2 + 200;
		double centery = shapey + height/2;
		_angle = Math.atan2(inity - centery, initx - centerx) - Math.atan2(curry - centery, currx - centerx);
		_angle = -_angle * 180/3.1415 + prevAngle;
		_rect.setRotate(_angle);
	}

//	enables rotation of a shape without using the mouse event
	@Override
	public void rotateShape(double angle){
		_rect.setRotate(angle);
		_angle = angle;
	}
	
//	returns the angle of shape
	@Override
	public double getAngle(){
		return _angle;
	}
	
//	adds to the pane
	@Override
	public void addtoPane(){
		_pane.getChildren().add(_rect);
	}

//	removes from the pane
	@Override
	public void delete(){
		_pane.getChildren().remove(_rect);
	}
//returns current color of the shape
	@Override
	public Color getColor(){
		return _color;
	}
//fills the shape with a new color	
	@Override
	public void newColor(Color color){
		_rect.setFill(color);
		_color = color;
	}
//saves all of the dimensions needed in order to reconstruct the shape	
	@Override
	public double[] saveDimensions(){
		return new double[]{9, _rect.getX(),_rect.getY(),_rect.getHeight(),_rect.getWidth(),_angle,_color.getRed(), _color.getGreen(), _color.getBlue()};
	}
//saves the name of the shape	
	@Override
	public String saveName(){
		return "Rectangle";
	}
}
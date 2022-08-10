package Sketchy;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

//CurvedLine is a Saveable
public class CurvedLine implements Saveable{
	private double _x;
	private double _y;
	private Polyline _line;
	private Pane _pane;
	private Color _color;

//	sets the initial point of the line and adds it to the pane
	public CurvedLine(double x, double y, Color color, Pane pane){
		_line = new Polyline();
		_color = color;
		_line.setStroke(_color);
		 _x = x;
		 _y = y;
		 _line.getPoints().addAll(new Double[]{_x,_y});
		 _pane = pane;
		 _pane.getChildren().add(_line);	 
	}

//	creates the line as the user draws. Depends on the current mouse location as it's dragged.
	@Override
	public void create(MouseEvent event){
		double x = event.getSceneX() - 200;
		double y = event.getSceneY();
		_line.getPoints().addAll(new Double[]{x,y});
	}
		
//	sets the color of the line to the color chosen by the user
	public void setColor(Color color){
		_color = color;
		_line.setStroke(_color);
	}

//	adds the line to the pane
	@Override
	public void addtoPane(){
		_pane.getChildren().add(_line);
	}

//	reorders the pane to correspond with the list of saveables
	@Override
	public void reorderPane(ArrayList<Saveable> saveList){
		_pane.getChildren().clear();
		for(int i = 0; i <saveList.size(); i++){
			saveList.get(i).addtoPane();
		}
	}

//	removes the line from the pane
	@Override
	public void delete(){
		_pane.getChildren().remove(_line);
	}

//	adds a point to a line. This is useful in loading lines.
	public void addPoints(double[] points){
		double x = points[0];
		double y = points[1];
		_line.getPoints().addAll(new Double[]{x,y});
	}

//	returns all of the dimensions necessary to reconstruct the line.
	@Override
	public double[] saveDimensions(){
		ObservableList<Double> points = _line.getPoints();
		double[] list = new double[points.size() + 4];
		list[0] = points.size() + 4;
		list[1] = _color.getRed();
		list[2] = _color.getGreen();
		list[3] = _color.getBlue();
		for(int i = 4; i < points.size() + 4; i++){
			list[i] = points.get(i - 4);
		}
		return list;
	}

//returns the name "Line"	
	@Override
	public String saveName(){
		return "Line";
	}
	
	
}
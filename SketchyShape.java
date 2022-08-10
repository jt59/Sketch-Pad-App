package Sketchy;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Models a shape in the Sketchy application.
 *
 * @author npucel
 *
 */
public interface SketchyShape extends Saveable {

	/**
	 *
	 * @return a {@linkplain java.util.List list} of the
	 *         {@link javafx.scene.Node Node}(s) that represent this shape.
	 */
	List<Node> getNodes();

	/**
	 * Sets the location of this shape to the point represented by (x,y).
	 *
	 * @param x
	 * @param y
	 */
	void setLocation(double x, double y);

	/**
	 *
	 * @return the x coordinate of this shape.
	 */
	double getX();

	/**
	 *
	 * @return the y coordinate of this shape.
	 */
	double getY();

	/**
	 *
	 * @return the center of this shape.
	 */
	Point2D getCenter();

	/**
	 * Moves this shape to the front of the canvas.
	 */
	void toFront();

	/**
	 * Moves this shape to the back of the canvas.
	 */
	void toBack();

	/**
	 * Sets the size of this shape.
	 *
	 * @param width
	 * @param height
	 */
	void setSize(double width, double height);

	/**
	 *
	 * @return the width of this shape
	 */
	double getWidth();

	/**
	 *
	 * @return the height of this shape
	 */
	double getHeight();

	/**
	 * Sets the angle by which to rotate this shape (in degrees)
	 *
	 * @param degrees
	 */
	void setRotate(double degrees);

	/**
	 *
	 * @return the angle of this shape's rotation (in degrees)
	 */
	double getRotate();

	/**
	 *
	 * @return the fill {@linkplain javafx.scene.paint.Color color} of this
	 *         shape.
	 */
	Color getFill();

	/**
	 * Sets the fill {@linkplain javafx.scene.paint.Color color} of this shape.
	 *
	 * @param color
	 */
	void setFill(Color color);

	/**
	 * Sets the width of this shape's stroke (border).
	 *
	 * @param width
	 */
	void setStrokeWidth(double width);

	/**
	 * Determines whether or not the given {@linkplain javafx.geometry.Point2D
	 * point} is contained within this shape's area.
	 *
	 * @param p
	 * @return true if p is contained this shape's area, false otherwise.
	 */
	boolean contains(Point2D p);
	
	/* TRANSFORMATIONS */
	
	/**
	 * Translates shape along each axis by the distance between prev and curr
     * along the corresponding axis.
	 * @param prev
	 * @param curr
	 */
	void translate(Point2D prev, Point2D curr);
	
	/**
	 * Rotates shape based on the distance between prev and curr.
	 * @param prev
	 * @param curr
	 */
	void rotate(Point2D prev, Point2D curr);
	
	/**
	 * Resizes shape based on the distance between prev and curr along each
     * axis.
	 * @param prev
	 * @param curr
	 */
	void resize(Point2D prev, Point2D curr);

}
package Sketchy;

import java.util.Arrays;
import java.util.List;

//import cs015.fnl.SketchySupport.FileIO;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * Models an ellipse in the Sketchy application.
 *
 * @author npucel
 *
 */
public class SketchyEllipse implements SketchyShape {

    private Ellipse _ellipse;
    private Color _color;

    public SketchyEllipse() {
        this.initialize();
    }

    public SketchyEllipse(Color col, double x, double y) {
        this.initialize();
        this.setFill(col);
        this.setLocation(x, y);
    }

    private void initialize() {
        _ellipse = new Ellipse();
        _ellipse.setStroke(Color.BLACK);
        _ellipse.setStrokeWidth(0);
    }

    @Override
    public List<Node> getNodes() {
        return Arrays.asList(_ellipse);
    }

    @Override
    public void setLocation(double x, double y) {
        _ellipse.setCenterX(x);
        _ellipse.setCenterY(y);
    }

    @Override
    public double getX() {
        return _ellipse.getCenterX();
    }

    @Override
    public double getY() {
        return _ellipse.getCenterY();
    }

    @Override
    public Point2D getCenter() {
        return new Point2D(_ellipse.getCenterX(), _ellipse.getCenterY());
    }

    /*
     * Are the following two methods too trivial now?
     */

    @Override
    public void toFront() {
        _ellipse.toFront();
    }

    @Override
    public void toBack() {
        _ellipse.toBack();
    }

    @Override
    public void setSize(double width, double height) {
        _ellipse.setRadiusX(width / 2);
        _ellipse.setRadiusY(height / 2);
    }

    @Override
    public double getWidth() {
        return _ellipse.getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
        return _ellipse.getRadiusY() * 2;
    }

    @Override
    public void setRotate(double degrees) {
        _ellipse.setRotate(degrees);
    }

    @Override
    public double getRotate() {
        return _ellipse.getRotate();
    }

    @Override
    public void setStrokeWidth(double width) {
        _ellipse.setStrokeWidth(width);
    }

    @Override
    public Color getFill() {
        return _color;
    }

    @Override
    public void setFill(Color color) {
        _color = color;
        _ellipse.setFill(color);
    }

    @Override
    public boolean contains(Point2D p) {
        Point2D rotatedPoint = Sketchy.rotatePoint(p, this.getCenter(), this.getRotate());
        return _ellipse.contains(rotatedPoint);
    }

    @Override
    public void translate(Point2D prev, Point2D curr) {
        double dx = curr.getX() - prev.getX();
        double dy = curr.getY() - prev.getY();
        this.setLocation(this.getX() + dx, this.getY() + dy);
    }
    
    @Override
    public void rotate(Point2D prev, Point2D curr) {
        Point2D center = this.getCenter();
        // Math to find angle between two points is given to students
        double dTheta = Math.atan2(prev.getY() - center.getY(), prev.getX() - center.getX())
                - Math.atan2(curr.getY() - center.getY(), curr.getX() - center.getX());
        this.setRotate(this.getRotate() - (180.0 / Math.PI) * dTheta);
    }
    
    @Override
    public void resize(Point2D prev, Point2D curr) {
        double rotation = this.getRotate();
        Point2D oldCenter = this.getCenter();
        Point2D rotatedPrev = Sketchy.rotatePoint(prev, oldCenter, rotation);
        Point2D rotatedCurr = Sketchy.rotatePoint(curr, oldCenter, rotation);

        double dx = Math.abs(rotatedCurr.getX() - rotatedPrev.getX());
        double dy = Math.abs(rotatedCurr.getY() - rotatedPrev.getY());
        if (Math.abs(oldCenter.getX() - rotatedPrev.getX()) > Math.abs(oldCenter.getX() - rotatedCurr.getX())) {
            dx = (-1) * dx;
        }
        if (Math.abs(oldCenter.getY() - rotatedPrev.getY()) > Math.abs(oldCenter.getY() - rotatedCurr.getY())) {
            dy = (-1) * dy;
        }

        if (this.getWidth() + 2 * dx < 1) {
            dx = 0;
        }
        if (this.getHeight() + 2 * dy < 1) {
            dy = 0;
        }

        this.setSize(this.getWidth() + 2 * dx, this.getHeight() + 2 * dy);

        // If the Shape's center is now different, we need to reset its
        // location so that it is.
        Point2D newCenter = this.getCenter();
        if (!oldCenter.equals(newCenter)) {
            dx = oldCenter.getX() - newCenter.getX();
            dy = oldCenter.getY() - newCenter.getY();
            this.setLocation(this.getX() + dx, this.getY() + dy);
        }
    }

    // @Override
    // public void save(FileIO io) {
    //     io.writeString("ELLIPSE");
    //     io.writeDouble(this.getX());
    //     io.writeDouble(this.getY());
    //     io.writeDouble(this.getWidth());
    //     io.writeDouble(this.getHeight());
    //     io.writeDouble(this.getRotate());
    //     io.writeInt((int) (_color.getRed() * 255.0));
    //     io.writeInt((int) (_color.getGreen() * 255.0));
    //     io.writeInt((int) (_color.getBlue() * 255.0));
    // }
}

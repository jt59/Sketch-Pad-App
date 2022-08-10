package Sketchy;

import java.util.List;

import cs015.fnl.SketchySupport.FileIO;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * This class models a curved line that the user can freely draw on the canvas.
 *
 * @author emagaver
 *
 */
public class CurvedPolyLine implements Saveable {

    private Polyline _line;
    private Color _color;

    public CurvedPolyLine() {
        _line = new Polyline();
    }

    /**
     *
     * @param color
     *            the color of this line.
     */
    public CurvedPolyLine(Color color) {
        _line = new Polyline();
        _color = color;
        _line.setStroke(color);
    }
    

    /**
     * Sets the color of this line.
     *
     * @param color
     */
    public void setColor(Color color) {
        _color = color;
        _line.setStroke(color);
    }

    /**
     * Adds a {@linkplain javafx.scene.shape.Line line segment} to this
     * CurvedLine.
     *
     * @param line
     *            the segment to add
     */
    public void addPoint(Point2D pt) {
        _line.getPoints().addAll(pt.getX(), pt.getY());
    }
    
    public void addPoints(List<Double> pts) {
        _line.getPoints().addAll(pts);
    }

    /**
     * Returns the {@link javafx.scene.shape.Polyline Polyline} that represents this
     * CurvedPolyLine.
     *
     * @return
     */
    public Node getNodes() {
        return _line;
    }

    // @Override
    // public void save(FileIO io) {
    //     io.writeString("LINE");
    //     io.writeInt((int) (_color.getRed() * 255.0));
    //     io.writeInt((int) (_color.getGreen() * 255.0));
    //     io.writeInt((int) (_color.getBlue() * 255.0));
    //     io.writeInt(_line.getPoints().size());
    //     for (double d : _line.getPoints()) {
    //         io.writeDouble(d);
    //     }
    //}

}

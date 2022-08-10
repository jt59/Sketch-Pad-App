package Sketchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This class models the control portion of the application, where the user can
 * commit certain actions to interact with the canvas. The user uses the control
 * to decide what they want to draw, save and load drawings, undo and redo
 * commands, and perform various actions on shapes.
 *
 * @author npucel
 *
 */
public class Control {

    private Sketchy _sketchy;
    private Pane _controlPane;

    private enum Operation {
        TO_FRONT, TO_BACK, FILL, DELETE, UNDO, REDO, SAVE, LOAD;
    }

    public Control(Sketchy sketchy) {
        _sketchy = sketchy;
        _controlPane = this.centeredVBox();
        this.setUpControl();
    }

    /**
     * Accessor for a fully populated set up controls to add to the scene graph.
     * @return A pane with all controls on it.
     */
    public Node getNode() {
        return _controlPane;
    }

    private VBox centeredVBox() {
        VBox box = new VBox(15.0);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void setUpControl() {
        // Sets the style and size of the control Pane.
        //
        // Note that the background of _controlPane *must* be set, otherwise it
        // will default to Color.TRANSPARENT, so Shapes would still be visible
        // even if moved over the control section.

        _controlPane.setStyle("-fx-background-color: #F0F0F0;");
        _controlPane.setPrefSize(Constants.CONTROL_WIDTH, Constants.CANVAS_HEIGHT);

        // Sets up each of the sub-Panes
        this.setupDrawingOptionsPane();
        this.setupColorControlPane();
        this.setupShapeActionPane();
        this.setupOperationsPane();
    }

    /****************************************/
    /* DRAWING OPTIONS PANE */
    /****************************************/

    private void setupDrawingOptionsPane() {
        ToggleGroup group = new ToggleGroup();
        String[] buttonTexts = { Constants.BTNTXT_SELECT, Constants.BTNTXT_DRAW, Constants.BTNTXT_RECTANGLE,
                Constants.BTNTXT_ELLIPSE };
        List<ButtonBase> buttons = new ArrayList<>();
        for (int i = 0; i < buttonTexts.length; i++) {
            RadioButton button = new RadioButton(buttonTexts[i]);
            button.setToggleGroup(group);
            buttons.add(button);
        }
        Pane drawingOptionsPane = new ButtonPaneBuilder()
                .withHeaderText(Constants.DRAW_OPTIONS_HEADER)
                .withButtons(buttons)
                .withButtonHandlers(this.getDrawingOptionsButtonHandlers())
                .populatePane();
        _controlPane.getChildren().add(drawingOptionsPane);
    }

    /**
     * Retrieves the list of handlers for the drawing options buttons.
     *
     * @return a List of EventHandler<ActionEvent>s; one for each button
     */
    private List<EventHandler<ActionEvent>> getDrawingOptionsButtonHandlers() {
        // The order of options matters as it corresponds to the order in which
        // the buttons are created
        SelectOption[] buttonOptions = { SelectOption.SELECT, SelectOption.PEN, SelectOption.RECTANGLE,
                SelectOption.ELLIPSE };
        List<EventHandler<ActionEvent>> handlers = new ArrayList<>();
        for (SelectOption option : buttonOptions) {
            handlers.add(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Control.this._sketchy.setOption(option);
                }
            });
        }
        return handlers;
    }

    /****************************************/
    /* COLOR CONTROL PANE */
    /****************************************/

    private ColorPicker _picker;

    private void setupColorControlPane() {
        Pane colorControlPane = this.centeredVBox();
        Text header = new Text(Constants.COLOR_CONTROL_HEADER);
        _picker = new ColorPicker(Color.BLACK);
        _picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Control.this.updateColor();
            }
        });

        colorControlPane.getChildren().add(header);
        colorControlPane.getChildren().add(_picker);

        this.updateColor();
        _controlPane.getChildren().add(colorControlPane);
    }

    private void updateColor() {
        Control.this._sketchy.setColor(_picker.getValue());
    }

    /****************************************/
    /* SHAPE ACTION PANE */
    /****************************************/

    private void setupShapeActionPane() {
        String[] buttonTexts = { Constants.BTNTXT_TOFRONT, Constants.BTNTXT_TOBACK, Constants.BTNTXT_FILL,
                Constants.BTNTXT_DELETE };
        List<ButtonBase> buttons = new ArrayList<>();
        for (String text : buttonTexts) {
            buttons.add(new Button(text));
        }
        Pane shapeActionPane = new ButtonPaneBuilder()
                .withHeaderText(Constants.SHAPE_ACTIONS_HEADER)
                .withButtons(buttons)
                .withButtonHandlers(this.getShapeActionButtonHandlers())
                .populatePane();
        _controlPane.getChildren().add(shapeActionPane);
    }

    /**
     * Retrieves the list of handlers for the shape action buttons.
     *
     * @return a List of EventHandler<ActionEvent>s; one for each button
     */
    private List<EventHandler<ActionEvent>> getShapeActionButtonHandlers() {
        // The order of handlers matters as it corresponds to the order in which
        // the buttons are created
        return Arrays.asList(new OperationHandler(Operation.TO_FRONT), new OperationHandler(Operation.TO_BACK),
                new OperationHandler(Operation.FILL), new OperationHandler(Operation.DELETE));
    }

    /****************************************/
    /* SKETCHY OPERATIONS PANE */
    /****************************************/

    private void setupOperationsPane() {
        String[] buttonTexts = { Constants.BTNTXT_UNDO, Constants.BTNTXT_REDO, Constants.BTNTXT_SAVE,
                Constants.BTNTXT_LOAD };
        List<ButtonBase> buttons = new ArrayList<>();
        for (String text : buttonTexts) {
            buttons.add(new Button(text));
        }
        Pane operationsPane = new ButtonPaneBuilder()
                .withHeaderText(Constants.OPERATIONS_HEADER)
                .withButtons(buttons)
                .withButtonHandlers(this.getOperationsButtonHandlers())
                .populatePane();
        _controlPane.getChildren().add(operationsPane);
    }

    /**
     * Retrieves the list of handlers for the operations buttons.
     *
     * @return a List of EventHandler<ActionEvent>s; one for each button
     */
    private List<EventHandler<ActionEvent>> getOperationsButtonHandlers() {
        // The order of handlers matters as it corresponds to the order in which
        // the buttons are created
        return Arrays.asList(new OperationHandler(Operation.UNDO), new OperationHandler(Operation.REDO),
                new OperationHandler(Operation.SAVE), new OperationHandler(Operation.LOAD));
    }

    /**
     * EventHandler for all buttons.
     *
     * @author emagaver
     *
     */
    private class OperationHandler implements EventHandler<ActionEvent> {

        private Operation _type;

        private OperationHandler(Operation type) {
            _type = type;
        }

        @Override
        public void handle(ActionEvent e) {
            switch (_type) {
            case TO_FRONT:
                Control.this._sketchy.raiseCurrentShape();
                break;
            case TO_BACK:
                Control.this._sketchy.lowerCurrentShape();
                break;
            case FILL:
                Control.this._sketchy.fillCurrentShape();
                break;
            case DELETE:
                Control.this._sketchy.deleteCurrentShape();
                break;
            case UNDO:
                Control.this._sketchy.undoCommand();
                break;
            case REDO:
                Control.this._sketchy.redoCommand();
                break;
            case SAVE:
                Control.this._sketchy.save();
                break;
            case LOAD:
                Control.this._sketchy.load();
                break;
            default:
                break;
            }
        }
    }

    /**
     * Utility class to populate and format a Pane with a given list of buttons.
     *
     * @author npucel
     *
     */
    private class ButtonPaneBuilder {

        private static final double VERTICAL_SPACING = 10.0;

        String _headerText;
        List<ButtonBase> _buttons;
        List<EventHandler<ActionEvent>> _buttonHandlers;

        ButtonPaneBuilder withButtons(List<ButtonBase> buttons) {
            _buttons = buttons;
            return this;
        }

        ButtonPaneBuilder withHeaderText(String text) {
            _headerText = text;
            return this;
        }

        ButtonPaneBuilder withButtonHandlers(List<EventHandler<ActionEvent>> handlers) {
            _buttonHandlers = handlers;
            return this;
        }

        Pane populatePane() {
            // Creates the container
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setSpacing(VERTICAL_SPACING);
            container.getChildren().add(new Text(_headerText));
            // Attaches handlers to buttons and adds buttons to the container
            for (int i = 0; i < _buttons.size(); i++) {
                ButtonBase button = _buttons.get(i);
                button.setOnAction(_buttonHandlers.get(i));
                container.getChildren().add(button);
            }
            return container;
        }

    }
}

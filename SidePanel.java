package Sketchy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class SidePanel{
	private VBox _sidePane;
	private Sketchy _sketchy;
	private ColorPicker _color;

	
	//takes in a vBox as a parameter to provide a space for the buttons. Also takes in sketchy as a parameter so that the
	//buttons can communicate with the drawing area
	public SidePanel(VBox sidePane, Sketchy sketchy){
		_sidePane = sidePane;
		_sketchy = sketchy;
		_sidePane.setPrefSize(200,90);
		_sidePane.setStyle("-fx-background-color:lightblue;");
		_sidePane.setAlignment(Pos.CENTER);
		this.setupRadioButtons();
		this.setUpColorPicker();
		this.setUpShapeActions();
		this.setUpOperations();
		
	}
	
//instantiates the RadioButtons and adds them all to the same instance of a ToggleGroup. Once all the buttons are 
//	set, they're added to the pane. Also, a header is added above the buttons to specify what they are.
	private void setupRadioButtons(){ 
		Label header = new Label("Drawing Options");
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		ToggleGroup group = new ToggleGroup();
		RadioButton button1 = new RadioButton("Select Shape");
	    button1.setToggleGroup(group);
	    button1.setSelected(true);
	    button1.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Select));
	    RadioButton button2 = new RadioButton("Draw with Pen");
	    button2.setToggleGroup(group);
	    button2.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Pen));
	    RadioButton button3 = new RadioButton("Draw Rectangle");
	    button3.setToggleGroup(group);
	    button3.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Rectangle));
	    RadioButton button4 = new RadioButton("Draw Elipse");
	    button4.setToggleGroup(group);
	    button4.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Ellipse));		
		_sidePane.getChildren().addAll(header,button1,button2,button3,button4);
		
		  }

//	sets up the color picker under a header "Set the Color". The initial color is set to blue, and the chosen
//	color is passed to the instance of Sketchy _sketchy. The color picker calls the setOnAction method so that
//	action will be taken when the color is changed.
	private void setUpColorPicker(){
		Label header = new Label("Set the Color");
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		_color = new ColorPicker(Color.BLUE);
		_color.getStyleClass().add("button");
		_sketchy.setColor(_color.getValue());
		_sidePane.getChildren().addAll(header,_color);
		_color.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Color));
	}

//	sets up the buttons that correspond to the various shape actions.The enums that match the action are passed
//	in as parameters to the MoveHandler
	private void setUpShapeActions(){
		Label header = new Label("Shape Actions");
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		Button raise = new Button("Raise");
		raise.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Raise));
		Button lower = new Button("Lower");
		lower.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Lower));
		Button fill = new Button("Fill");
		fill.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Fill));
		Button delete = new Button("Delete");
		delete.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Delete));
		_sidePane.getChildren().addAll(header,raise,lower,fill,delete);
		
	}

//	sets up the remaining operations in a similar way as setUpShapeActions()
	private void setUpOperations(){
		Label header = new Label("Operations");
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		Button undo = new Button("Undo");
		undo.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Undo));
		Button redo = new Button("Redo");
		redo.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Redo));
		Button save = new Button("Save");
		save.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Save));
		Button load = new Button("Load");
		load.setOnAction((EventHandler<ActionEvent>) new MoveHandler(ButtonEnums.Load));
		_sidePane.getChildren().addAll(header,undo,redo,save,load);
	}
	
	



//controls what happens when each button is clicked
	private class MoveHandler implements EventHandler<ActionEvent>{
		private ButtonEnums _b;

//		MoveHandler takes in a ButtonEnums as a parameter, and sets it as an instance variable so that the handle method
//		has access to the value
		public MoveHandler(ButtonEnums b){
			_b = b;
			
		}

//the handle method takes in the ActionEvent as a parameter, and the event is clicking a button. It switches on the 
//		ButtonEnums that was passed into the MoveHandler. Each ButtonEnums corresponds to a different action. Undo and 
//		Redo access the undo and redo stacks within Sketchy, pop the last-in Command, perform the undo/redo action,
//		then push the Command to the opposite stack.
	  public void handle(ActionEvent event){
		  switch(_b){
		  case Color:
			  _sketchy.setColor(_color.getValue());
			  break;
		  case Select:
			  _sketchy.setSelected(_b);
			  break;
		  case Pen:
			  _sketchy.setSelected(_b);
			  break;
		  case Rectangle:
			  _sketchy.setSelected(_b);
			  break;
		  case Ellipse:
			  _sketchy.setSelected(_b);
			  break;
		  case Raise:
			  _sketchy.moveShapetoLayer(false,true);
			  break;
		  case Lower:
			  _sketchy.moveShapetoLayer(true,true);
			  break;
		  case Fill:
			  _sketchy.fillShape();
			  break;
		  case Delete:
			  _sketchy.delete(true,true);
			  break;
		  case Undo:
			  if(_sketchy.getUndoStack().size() > 0){
				  Command command = _sketchy.getUndoStack().pop();
				  command.undo();
				  _sketchy.getRedoStack().push(command);
			  }
			  break;
		  case Redo:
			  if(_sketchy.getRedoStack().size() > 0){
				  Command command = _sketchy.getRedoStack().pop();
				  command.redo();
				  _sketchy.getUndoStack().push(command);
			  }
			  break;
		  case Save:
			  _sketchy.saveFile();
			  break;
		  case Load:
			  _sketchy.loadFile();
			  break;		  
		  default:		  
		  }
		 
			  
		  
	  }
	  
	}
}
package Sketchy;

import java.util.ArrayList;
//CreateShape is a command that's added to the undo stack whenever s shape is drawn
public class CreateShape implements Command{
	private SketchyShape _shape;
	private Saveable _save;
	private int _iShape;
	private int _iSave;
	private ArrayList<SketchyShape> _shapeList;
	private ArrayList<Saveable> _saveList;
	private Sketchy _sketchy;

//	takes in all of the current lists and indexes as parameters in order to logically undo and redo creating a shape
	public CreateShape(Sketchy sketchy, SketchyShape shape, Saveable save, int iShape, int iSave,ArrayList<SketchyShape> shapeList, ArrayList<Saveable> saveList){
		_sketchy = sketchy;
		_shape = shape;
		_save = save;
		_iShape = iShape;
		_iSave = iSave;
		_saveList = saveList;
		_shapeList = shapeList;
	}
//deletes the shape. 	
	@Override
	public void undo(){
		_sketchy.setSelectMode(true);
		_sketchy.setCurrShape(_iShape);
		_sketchy.delete(true,false);
		_sketchy.setSelectMode(false);
	}

	//puts the shape back graphically and logically
	@Override
	public void redo(){
		_saveList.add(_iSave, _save);
		_shapeList.add(_iShape, _shape);
		_save.reorderPane(_saveList);
		if(_iShape > 0){
			_shapeList.get(_iShape - 1).deselect();
		}
		_sketchy.setCurrShape(_iShape);
	}
}
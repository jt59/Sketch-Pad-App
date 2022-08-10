package Sketchy;

import java.util.ArrayList;

//Command that's pushed onto the undo stack when a shape is deleted
public class DeleteShape implements Command{
	private SketchyShape _shape;
	private Saveable _save;
	private int _iShape;
	private int _iSave;
	private ArrayList<SketchyShape> _shapeList;
	private ArrayList<Saveable> _saveList;
	private Sketchy _sketchy;

//	the current lists and indices are past in as parameters
	public DeleteShape(Sketchy sketchy, SketchyShape shape, Saveable save, int iShape, int iSave,ArrayList<SketchyShape> shapeList, ArrayList<Saveable> saveList){
		_sketchy = sketchy;
		_shape = shape;
		_save = save;
		_iShape = iShape;
		_iSave = iSave;
		_saveList = saveList;
		_shapeList = shapeList;
	}

//	adds the shape back logically and graphically
	@Override
	public void undo(){
		_saveList.add(_iSave, _save);
		_shapeList.add(_iShape, _shape);
		_save.reorderPane(_saveList);
		_sketchy.setCurrShape(_iShape);
	}

//	deletes the shape logically and graphically
	@Override
	public void redo(){
		_sketchy.setCurrShape(_iShape);
		_sketchy.setSelectMode(true);
		_sketchy.delete(true,false);
		_sketchy.setSelectMode(false);
	}
}
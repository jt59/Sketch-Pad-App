package Sketchy;

import java.util.ArrayList;
//Command that's pushed onto the undo stack when a line is drawn
public class DrawLine implements Command{
	private Saveable _save;
	private int _iSave;
	private ArrayList<Saveable> _saveList;
	private Sketchy _sketchy;

//	takes in the current save list, index, and Saveable as a parameter
	public DrawLine(Sketchy sketchy,Saveable save, int iSave, ArrayList<Saveable> saveList){
		_sketchy = sketchy;
		_save = save;
		_iSave = iSave;
		_saveList = saveList;
	}
//deletes the line	
	public void undo(){
		_sketchy.setSelectMode(true);
		_sketchy.delete(false,false);
		_sketchy.setSelectMode(false);
	}
//adds the line back in	
	public void redo(){
		_saveList.add(_iSave, _save);
		_save.reorderPane(_saveList);
	}
}
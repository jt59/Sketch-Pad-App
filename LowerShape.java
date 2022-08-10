package Sketchy;

//Command that's pushed onto the stack when a shape is lowered
public class LowerShape implements Command{
	private Sketchy _sketchy;
	private int _currShape;
	
	public LowerShape(Sketchy sketchy, int currShape){
		_sketchy = sketchy;
		_currShape = currShape;
	}

//	raises the shape by calling the Sketchy method moveShapeto Layer(). It's called in a way that prevents this from
//	getting added to the undo stack
	@Override
	public void undo(){
		_sketchy.setSelectMode(true);
		_sketchy.setCurrShape(_currShape);
		_sketchy.moveShapetoLayer(false,false);
		_currShape = _sketchy.getCurrShapeIndex();
		_sketchy.setSelectMode(false);
	}

//	re-lowers the shape
	@Override
	public void redo(){
		_sketchy.setSelectMode(true);
		_sketchy.setCurrShape(_currShape);
		_sketchy.moveShapetoLayer(true,false);
		_sketchy.setSelectMode(false);
	}
}
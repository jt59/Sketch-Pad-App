package Sketchy;

//Command that's added to the undo stack when a shape is raised
public class RaiseShape implements Command{
	private Sketchy _sketchy;
	private int _currShape;
	
	public RaiseShape(Sketchy sketchy, int currShape){
		_sketchy = sketchy;
		_currShape = currShape;
	}

//lowers the shape using the method moveShapeto Layer. This is called in a way that prevents another LowerShape from
//	getting added to the stack.
	@Override
	public void undo(){
		_sketchy.setSelectMode(true);
		_sketchy.setCurrShape(_currShape);
		_sketchy.moveShapetoLayer(true,false);
		_currShape = _sketchy.getCurrShapeIndex();
		_sketchy.setSelectMode(false);
		
	}

//	re-raises the shape
	@Override
	public void redo(){
		_sketchy.setSelectMode(true);
		_sketchy.setCurrShape(_currShape);
		_sketchy.moveShapetoLayer(false,false);
		_currShape = _sketchy.getCurrShapeIndex();
		_sketchy.setSelectMode(false);
	}
}
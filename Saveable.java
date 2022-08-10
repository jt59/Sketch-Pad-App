package Sketchy;

//import cs015.fnl.SketchySupport.FileIO;

/**
 * Models an object that can be saved with a
 * {@link cs015.fnl.SketchySupport.FileIO FileIO}.
 *
 * @author npucel
 *
 */
public interface Saveable {

	/**
	 * Saves this object to the file being written to by io.
	 * 
	 * @param io
	 *            an instance of {@link cs015.fnl.SketchySupport.FileIO FileIO}
	 *            that has already been opened for writing.
	 */
	void save(FileIO io);
}

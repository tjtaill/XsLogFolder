package xslogfolder;

import java.util.regex.Pattern;

import xorg.gjt.sp.jedit.buffer.FoldHandler;

/**
 * @author Troy Taillefer
 * class XsLogFoldHandler
 * Folds XS log files based on their date time stamps
 */
public class XsLogFoldHandler extends FoldHandler {
	private Pattern datePattern = 
		Pattern.compile("^\\d{4}\\.\\d{2}\\.\\d{2}.+?$");

	public XsLogFoldHandler(){
		super("xslog");
	}
	
	public int getFoldLevel(JEditBuffer buffer, int lineIndex, Segment segment) {
	
	}
		
	
}
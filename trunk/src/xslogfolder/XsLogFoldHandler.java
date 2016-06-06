package xslogfolder;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.buffer.FoldHandler;
import javax.swing.text.Segment;

public class XsLogFoldHandler extends FoldHandler {
	private static final Pattern RESPONSE = Pattern.compile("^SIP/2.0 \\d{3} .+$");
	private static final Pattern REQUEST = Pattern.compile("^[A-Z]+ sip:.+ SIP/2.0$");
	private static final Pattern XS_EVENT = Pattern.compile("com.+?Event");		
	private static final Pattern DATE = Pattern.compile("^\\d{4}\\.\\d{2}\\.\\d{2} .+$");
	private static final Pattern DIAMETER_HEADER = Pattern.compile("^Message header:$");
	private static final Pattern DIAMETER_AVP = Pattern.compile("^AVPs:$");
	private FoldState foldState = FoldState.NOT_FOLDING; 
	
	private enum FoldState {
		NOT_FOLDING,
		DATE_FOLDING,
		SIP_FOLDING,
		EVENT_FOLDING,
		DIAMETER_HEADER_FOLDING,
		DIAMETER_AVP_FOLDING
	}

	public XsLogFoldHandler(){
		super("xslog");
	}
	
	public int getFoldLevel(JEditBuffer buffer, int lineIndex, Segment segment) {
        buffer.getLineText(lineIndex, segment);
        String line = segment.toString();
        Matcher eventMatcher = XS_EVENT.matcher(line);
        Matcher reqMatcher = REQUEST.matcher(line);
        Matcher respMatcher = RESPONSE.matcher(line);
        Matcher dateMatcher = DATE.matcher(line);
        Matcher headerMatcher = DIAMETER_HEADER.matcher(line);
        Matcher avpMatcher = DIAMETER_AVP.matcher(line);

        if (dateMatcher.matches()) {
            foldState = FoldState.DATE_FOLDING;
            return 0;
        } else if (reqMatcher.matches() || respMatcher.matches()) {
            foldState = FoldState.SIP_FOLDING;
            return 1;
        } else if (eventMatcher.find()) {
            foldState = FoldState.EVENT_FOLDING;
            return 1;
        } else if (headerMatcher.find()) {
            foldState = FoldState.DIAMETER_HEADER_FOLDING;
            return 1;
        } else if (avpMatcher.find()) {
            foldState = FoldState.DIAMETER_AVP_FOLDING;
            return 1;
        } else {
			int foldLevel = 0;
			switch(foldState) {
			case NOT_FOLDING:
				foldLevel = 0;
				break;
			case DATE_FOLDING:
				foldLevel = 1;
				break;
			case EVENT_FOLDING:
				foldState = FoldState.DATE_FOLDING;
			case SIP_FOLDING:
            case DIAMETER_AVP_FOLDING:
            case DIAMETER_HEADER_FOLDING:
				foldLevel = 2;
			}
			return foldLevel;
		}
	}
		
	
}
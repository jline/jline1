package jline.console;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class PrevSpaceWordAction extends ConsoleAction {

    public PrevSpaceWordAction(int start, int action) {
        super(start, action);
    }

    public void doAction(StringBuffer buffer) {
        int cursor = getStart();

        //the cursor position in jline might be > the buffer
        if(cursor > buffer.length())
            cursor = buffer.length()-1;

        //move back every potential space first
        while(cursor > 0 && isSpace(buffer.charAt(cursor-1)))
            cursor--;

        while(cursor > 0 && !isSpace(buffer.charAt(cursor-1)))
            cursor--;

        setEnd(cursor);
    }

}

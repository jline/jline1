package jline.console;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class PrevWordAction extends ConsoleAction {

    public PrevWordAction(int start, int action) {
        super(start, action);
    }

    public void doAction(StringBuffer buffer) {
        int cursor = getStart();
        //the cursor position in jline might be > the buffer
        if(cursor > buffer.length())
            cursor = buffer.length()-1;

        //move back every space
        while(cursor > 0 && isSpace(buffer.charAt(cursor-1)))
            cursor--;

        if(cursor > 0 && isDelimiter(buffer.charAt(cursor-1))) {
            while(cursor > 0 && isDelimiter(buffer.charAt(cursor-1)))
                cursor--;
        }
        else {

            while(cursor > 0 && !isDelimiter(buffer.charAt(cursor-1))) {
                cursor--;
            }
        }

        setEnd(cursor);
    }

}

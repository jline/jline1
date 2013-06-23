package jline.console;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class NextSpaceWordAction extends ConsoleAction {

    public NextSpaceWordAction(int start, int action) {
        super(start, action);
    }

    public void doAction(StringBuffer buffer) {
        int cursor = getStart();
        //if cursor stand on a delimiter, move till its no more delimiters
        if(cursor < buffer.length() && (isDelimiter(buffer.charAt(cursor))))
            while(cursor < buffer.length() && (isDelimiter(buffer.charAt(cursor))))
                cursor++;
        //if we stand on a non-delimiter
        else {
            while(cursor < buffer.length() && !isSpace(buffer.charAt(cursor)))
                cursor++;

            //if we end up on a space we move past that too
            if(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                while(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                    cursor++;
        }

        setEnd(cursor);
    }

}

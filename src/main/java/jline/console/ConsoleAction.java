package jline.console;

/**
 * Base class for console actions
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public abstract class ConsoleAction {

    private int start;
    private int end;
    private int action;

    /**
     * All ConsoleAction classes must specify a start entry and action
     * @param start entry
     * @param action type
     */
    protected ConsoleAction(int start, int action) {
        setStart(start);
        setAction(action);
    }

    /**
     * Perform an action
     *
     * @param buffer console
     */
    public abstract void doAction(StringBuffer buffer);

    private void setAction(int action) {
        this.action = action;
    }


    public final int getAction() {
        return action;
    }

    private void setStart(int start) {
        this.start = start;
    }

    public final int getStart() {
        return start;
    }

    protected void setEnd(int end) {
        this.end = end;
    }

    public final int getEnd() {
        return end;
    }

    /**
     * Checks to see if the specified character is a delimiter. We consider a
     * character a delimiter if it is anything but a letter or digit.
     *
     * @param c the character to test
     * @return true if it is a delimiter
     */
    protected final boolean isDelimiter(char c) {
        return !Character.isLetterOrDigit(c);
    }

     /**
     * Checks to see if the specified character is a space.
     *
     * @param c the character to test
     * @return true if the char is a space
     */
    protected final boolean isSpace(char c) {
        return Character.isWhitespace(c);
    }
}

package jline.console;

/**
 * A placeholder class for simple actions that require little logic.
 * Typical movement one char back/forth, to the end/beginning of buffer.
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class SimpleAction extends ConsoleAction {

    public SimpleAction(int start, int action) {
        super(start, action);
    }

    public SimpleAction(int start, int action, int end) {
        super(start, action);
        setEnd(end);
    }

    public void doAction(StringBuffer buffer) {
        if(buffer.length() < getEnd())
            setEnd(buffer.length());

        if(getEnd() < 0)
            setEnd(0);
    }

}

package jline.console;

/**
 * An action that either delete as backspace or delete
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class DeleteAction extends ConsoleAction {

    private boolean backspace = false;

    public DeleteAction(int start, int action) {
        super(start, action);
    }

    public DeleteAction(int start, int action, boolean backspace) {
        super(start, action);
        this.backspace = backspace;
    }

    public void doAction(StringBuffer buffer) {
        if(backspace) {
            if(getStart() == 0)
                setEnd(0);
            else
                setEnd(getStart()-1);
        }
        else {
            if(buffer.length() <= getStart())
                setEnd(getStart());
            else
                setEnd(getStart()+1);
        }
    }
}

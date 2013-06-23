package jline;

/**
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class UndoAction {

    private int cursorPosition;
    private String buffer;

    public UndoAction(int cursorPosition, String buffer) {
        setCursorPosition(cursorPosition);
        setBuffer(buffer);
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    private void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public String getBuffer() {
        return buffer;
    }

    private void setBuffer(String buffer) {
        this.buffer = buffer;
    }
}

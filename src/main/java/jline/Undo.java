package jline;

import java.util.Stack;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class Undo {

    private static short UNDO_SIZE = 50;

    private Stack undoStack;
    private int counter;

    public Undo() {
        undoStack = new Stack();
        undoStack.setSize(UNDO_SIZE);
        counter = 0;
    }

    public UndoAction getNext() {
        if(counter > 0) {
            counter--;
            return (UndoAction) undoStack.pop();
        }
        else
            return null;
    }

    public void addUndo(UndoAction u) {
        if(counter <= UNDO_SIZE) {
            counter++;
            undoStack.push(u);
        }
        else {
            undoStack.remove(UNDO_SIZE);
            undoStack.push(u);
        }

    }

    public void clear() {
        undoStack.clear();
        counter = 0;
    }

    public boolean isEmpty() {
        return (counter == 0);
    }

    public int size() {
        return counter;
    }
}

package jline.console;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class PasteManager {

    private static final int PASTE_SIZE = 10;
    private List pasteStack;

    public PasteManager() {
        pasteStack = new ArrayList(PASTE_SIZE);
    }

    public void addText(StringBuffer buffer) {
        checkSize();
        pasteStack.add(buffer);
    }

    private void checkSize() {
        if(pasteStack.size() >= PASTE_SIZE) {
            pasteStack.remove(0);
        }
    }

    public StringBuffer get(int index) {
        if(index < pasteStack.size())
            return (StringBuffer) pasteStack.get((pasteStack.size()-index-1));
        else
            return (StringBuffer) pasteStack.get(0);
    }
}

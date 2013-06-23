package jline;

import java.io.IOException;
import java.io.InputStream;

/**
 * ViParser parses key presses if the terminal is in vi-mode
 * NOTE: Arrow keys will atm not work in vi-mode, use vi keys for movement instead
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class ViParser implements ConsoleOperations {

    public static final short ESCAPE = 27;
    private static final short VI_S = 115;
    private static final short VI_SHIFT_S = 83;
    private static final short VI_D = 100;
    private static final short VI_SHIFT_D = 68;
    private static final short VI_C = 99;
    private static final short VI_SHIFT_C = 67;
    private static final short VI_A = 97;
    private static final short VI_SHIFT_A = 65;
    private static final short VI_0 = 48;
    private static final short VI_$ = 36;
    private static final short VI_X = 120;
    private static final short VI_P = 112;
    private static final short VI_SHIFT_P = 80;
    private static final short VI_I = 105;
    private static final short VI_SHIFT_I = 73;
    private static final short VI_TILDE = 126;
    private static final short VI_Y = 121;


    //movement
    private static final short VI_H = 104;
    private static final short VI_J = 106;
    private static final short VI_K = 107;
    private static final short VI_L = 108;
    private static final short VI_B = 98;
    private static final short VI_SHIFT_B = 66;
    private static final short VI_W = 119;
    private static final short VI_SHIFT_W = 87;
    private static final short VI_SPACE = 32;

    public static final short VI_ENTER = 10;
    private static final short VI_PERIOD = 46;
    private static final short VI_U = 117;

    private Terminal terminal;
    private ViMode viMode;
    private ViMode previousMode;

    private int previousAction;

    public ViParser(Terminal terminal) {
        this.terminal = terminal;
        viMode = new ViMode();
        previousMode = new ViMode();
    }

    public int parseViInput(InputStream in) throws IOException {
        int c = terminal.readCharacter(in);
         if (c == ESCAPE) {
            switchEditMode();
             if(isInEditMode())
                 return c;
             else
                return CTRL_B;
        }

        else if(!isInEditMode()) {
            c = enterCommandMode(c);
            while (c == 0) {
                c = enterCommandMode(terminal.readCharacter(in));
            }
            return c;
        }

        return c;
    }

    public boolean isInEditMode() {
        return viMode.isInEditMode();
    }

    public void switchEditMode() {
        viMode.switchEditMode();
    }

    public void setDeleteMode(boolean deleteMode) {
        viMode.setDeleteMode(deleteMode);
    }

    public boolean isDeleteMode() {
        return viMode.isDeleteMode();
    }

    public boolean isChangeMode() {
        return viMode.isChangeMode();
    }

    public boolean isYankMode() {
        return viMode.isYankMode();
    }

    public void setYankMode(boolean yankMode) {
        viMode.setYankMode(yankMode);
    }

    private int saveAction(int action) {
        if(isYankMode() || isChangeMode() || isDeleteMode()) {
            previousAction = action;
            previousMode.replicate(viMode);
        }
        return action;
    }

    private int enterCommandMode(int c) throws IOException {

        // an extra check because of "i"
        if(isInEditMode())
            return c;

        //movement
        if(c == VI_H)
            return saveAction(CTRL_B);
        else if(c == VI_L || c == VI_SPACE)
            return saveAction(CTRL_F);
        else if(c == VI_J)
            return saveAction(CTRL_N);
        else if(c == VI_K)
            return saveAction(CTRL_P);
        else if(c == VI_B)
            return saveAction(CTRL_X);
        else if(c == VI_SHIFT_B)
            return saveAction(CTRL_SHIFT_G);
        else if(c == VI_W)
            return saveAction(CTRL_O);
        else if(c == VI_SHIFT_W)
            return saveAction(CTRL_SHIFT_O);
        else if(c == VI_0)
            return saveAction(CTRL_A);
        else if(c == VI_$)
            return saveAction(CTRL_E);

        //edit
        else if(c == VI_X) {
            return saveAction(DELETE);
        }
        // paste
        else if(c == VI_P)
           return saveAction(VI_PASTE_AFTER);
        else if(c == VI_SHIFT_P)
            return saveAction(VI_PASTE_BEFORE);
        // replace
        else if(c == VI_S) {
            switchEditMode();
            return saveAction(DELETE);
        }
        else if(c == VI_SHIFT_S) {
            viMode.setChangeMode(true);
            return saveAction(CTRL_SHIFT_K);
        }
        // insert
        else if(c == VI_A) {
            switchEditMode();
            return saveAction(CTRL_F);
        }
        else if(c == VI_SHIFT_A) {
            switchEditMode();
            return saveAction(CTRL_E);
        }
        else if(c == VI_I) {
            switchEditMode();
            return saveAction(0);
        }
        else if(c == VI_SHIFT_I) {
            switchEditMode();
            return saveAction(CTRL_A);
        }
        //delete
        else if(c == VI_D) {
            //if we're already in delete-mode, delete the whole line
            if(isDeleteMode())
                return saveAction(CTRL_SHIFT_K);
            else
                viMode.setDeleteMode(true);
        }
        else if(c == VI_SHIFT_D) {
            viMode.setDeleteMode(true);
            return saveAction(CTRL_E);
        }
        else if(c == VI_C) {
            viMode.setChangeMode(true);
        }
        else if(c == VI_SHIFT_C) {
            viMode.setChangeMode(true);
            return saveAction(CTRL_E);
        }
        else if(c == VI_ENTER) {
            switchEditMode();
            return c;
        }
        else if(c == VI_PERIOD) {
            viMode.replicate(previousMode);
            return previousAction;
        }
        else if(c == VI_U) {
            return saveAction(CTRL_Z);
        }
        else if(c == VI_TILDE) {
            return saveAction(TILDE);
        }
        else if(c == VI_Y) {
            //if we're already in yank-mode, yank the whole line
            if(isYankMode())
                return saveAction(CTRL_SHIFT_K);
            else
                viMode.setYankMode(true);
        }

        return 0;
    }

    /**
     * Simple class to control which mode we're in.
     * Separated into a class since its needed by previousAction too.
     */
    private class ViMode {
        private boolean deleteMode = false;
        private boolean editMode = true;
        private boolean changeMode = false;
        private boolean yankMode = false;

        private boolean isInEditMode() {
            return editMode;
        }

        private void switchEditMode() {
            if(editMode)
                editMode = false;
            else {
                editMode = true;
                setChangeMode(false);
                setDeleteMode(false);
                setYankMode(false);
            }
        }

        private void setDeleteMode(boolean deleteMode) {
            this.deleteMode = deleteMode;
        }

        private boolean isDeleteMode() {
            return deleteMode;
        }

        private void setChangeMode(boolean b) {
            changeMode = b;
        }

        private boolean isChangeMode() {
            return changeMode;
        }

        private void setYankMode(boolean yankMode) {
            this.yankMode = yankMode;
        }

        private boolean isYankMode() {
            return yankMode;
        }

        private void replicate(ViMode mode) {
            deleteMode = mode.deleteMode;
            editMode = mode.editMode;
            changeMode = mode.changeMode;
            yankMode = mode.yankMode;
        }

    }

}

package jline;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class UnixViTerminal extends UnixTerminal {

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
    private static final short VI_I = 105;
    private static final short VI_SHIFT_I = 73;

    //movement
    private static final short VI_H = 104;
    private static final short VI_J = 106;
    private static final short VI_K = 107;
    private static final short VI_L = 108;
    private static final short VI_B = 98;
    private static final short VI_SHIFT_B = 66;
    private static final short VI_W = 119;
    private static final short VI_SHIFT_W = 87;
    private boolean viDeleteMode = false;
    private boolean editMode = true;

    public int readVirtualKey(InputStream in) throws IOException {
        //return super.readVirtualKey(in);
        int c = readCharacter(in);


        if (c == ARROW_START) {
            switchEditMode();
            return CTRL_B;
        }

        else if(!isInEditMode()) {
            c = enterCommandMode(c);
            while (c == 0) {
                c = enterCommandMode(readCharacter(in));
            }
            return c;
        }

        return c;
    }

     private boolean isInEditMode() {
        return editMode;
    }

    private void switchEditMode() {
        if(editMode)
            editMode = false;
        else
            editMode = true;
    }

    private void setDeleteMode(boolean deleteMode) {
        viDeleteMode = deleteMode;
    }

    private boolean isViDeleteMode() {
        return viDeleteMode;
    }

    private int enterCommandMode(int c) throws IOException {
        //int c = readCharacter(in);
//        System.out.println("in commandmode, got:"+((char) c)+"which is:"+c);
        // an extra check because of "i"
        if(isInEditMode())
            return c;

        if(isViDeleteMode()) {
            setDeleteMode(false);
            if(c == VI_W)
                return ConsoleOperations.DELETE_PREV_WORD;
            else if(c == VI_D)
                return ConsoleOperations.CLEAR_LINE;
            else if(c == VI_H)
                return ConsoleOperations.DELETE_PREV_CHAR;
            else if(c == VI_L)
                return ConsoleOperations.DELETE_NEXT_CHAR;

            //TODO: much to add
            return 0;
        }

        if(c == VI_X)
            return DELETE;
        else if(c == VI_H)
            return CTRL_B;
        else if(c == VI_L)
            return CTRL_F;
        else if(c == VI_J)
            return CTRL_N;
        else if(c == VI_K)
            return CTRL_P;
        else if(c == VI_B)
            return CTRL_G;
        else if(c == VI_SHIFT_B)
            return CTRL_G; //TODO: should improve this to go to next space
        else if(c == VI_W)
            return CTRL_O;
        //else if(c == VI_SHIFT_W)
        //    return ConsoleOperations.NEXT_SPACE_WORD;
        // wont work atm since MOVE_TO_BEG = -1
        //else if(c == VI_0)
        //    return ConsoleOperations.MOVE_TO_BEG;
        /*
        else if(c == VI_$)
            return ConsoleOperations.MOVE_TO_END;
        //else if(c == VI_P) //TODO
        //   return ConsoleOperations.PASTE;
        else if(c == VI_S) {
            switchEditMode();
            return ConsoleOperations.DELETE_NEXT_CHAR;
        }
        else if(c == VI_SHIFT_S) {
            switchEditMode();
            return ConsoleOperations.CLEAR_LINE;
        }
        else if(c == VI_A) {
            switchEditMode();
            return ConsoleOperations.NEXT_CHAR;
        }
        else if(c == VI_SHIFT_A) {
            switchEditMode();
            return ConsoleOperations.MOVE_TO_END;
        }
        else if(c == VI_I) {
            switchEditMode();
            return 0;
        }
        else if(c == VI_SHIFT_I) {
            switchEditMode();
            return ConsoleOperations.MOVE_TO_BEG;
        }
        else if(c == VI_D) {
            setDeleteMode(true);
            return 0;
        }
        */
        /*
        else if(c == VI_SHIFT_D) {
            return ConsoleOperations.
        }
        */

        return 0;
    }
}

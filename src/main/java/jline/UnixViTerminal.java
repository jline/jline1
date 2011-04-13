package jline;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple Terminal that supports vi bindings.
 *
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

    private static final short VI_ENTER = 10;
    private static final short VI_PERIOD = 46;
    private static final short VI_U = 117;

    private boolean deleteMode = false;
    private boolean editMode = true;
    private boolean changeMode = false;

    private short latestAction = 0;

    public int readVirtualKey(InputStream in) throws IOException {
        int c = readCharacter(in);

        if (isBackspaceDeleteSwitched())
            if (c == DELETE)
                c = '\b';
            else if (c == '\b')
                c = DELETE;

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

        // handle unicode characters, thanks for a patch from amyi@inf.ed.ac.uk
        if (c > 128) {
            // handle unicode characters longer than 2 bytes,
            // thanks to Marc.Herbert@continuent.com
            replayStream.setInput(c, in);
            //replayReader = new InputStreamReader(replayStream, encoding);
            c = replayReader.read();

        }

        return c;
    }

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

    private int enterCommandMode(int c) throws IOException {
        //int c = readCharacter(in);
        //System.out.println("in commandmode, got:"+((char) c)+"which is:"+c);

        // an extra check because of "i"
        if(isInEditMode())
            return c;

        if(isDeleteMode()) {
            setDeleteMode(false);
            if(c == VI_B) {
                latestAction = CTRL_W;
                return CTRL_W;
            }
            else if(c == VI_SHIFT_B) {
                latestAction = CTRL_WW;
                return CTRL_WW;
            }
            else if(c == VI_W) {
                latestAction = CTRL_M;
                return CTRL_M;
            }
            else if(c == VI_SHIFT_W) {
                latestAction = CTRL_MM;
                return CTRL_MM;
            }
            else if(c == VI_D) {
                latestAction = CTRL_SHIFT_K;
                return CTRL_SHIFT_K;
            }
            else if(c == VI_$) {
                latestAction = CTRL_K;
                return CTRL_K;
            }
            else if(c == VI_H) {
                latestAction = CTRL_H;
                return CTRL_H;
            }
            else if(c == VI_L) {
                latestAction =  DELETE;
                return DELETE;
            }
            else if(c == VI_0) {
                latestAction = CTRL_U;
                return CTRL_U;
            }

            return 0;
        }

        if(isChangeMode()) {
            setChangeMode(false);
            if(c == VI_B) {
                switchEditMode();
                return CTRL_W;
            }
            if(c == VI_SHIFT_B) {
                switchEditMode();
                return CTRL_WW;
            }
            else if(c == VI_W) {
                switchEditMode();
                return CTRL_M;
            }
            else if(c == VI_SHIFT_W) {
                switchEditMode();
                return CTRL_MM;
            }
            else if(c == VI_$) {
                switchEditMode();
                return CTRL_K;
            }
            else if(c == VI_H) {
                switchEditMode();
                return CTRL_H;
            }
            else if(c == VI_L) {
                switchEditMode();
                return DELETE;
            }
            else if(c == VI_0) {
                switchEditMode();
                return CTRL_U;
            }

            return 0;
        }

        //movement
        if(c == VI_H)
            return CTRL_B;
        else if(c == VI_L)
            return CTRL_F;
        else if(c == VI_J)
            return CTRL_N;
        else if(c == VI_K)
            return CTRL_P;
        else if(c == VI_B)
            return CTRL_X;
        //  return CTRL_G;
        else if(c == VI_SHIFT_B)
            return CTRL_SHIFT_G;
        else if(c == VI_W)
            return CTRL_O;
        else if(c == VI_SHIFT_W)
            return CTRL_SHIFT_O;
        else if(c == VI_0)
            return CTRL_A;
        else if(c == VI_$)
            return CTRL_E;

        //edit
        else if(c == VI_X) {
            latestAction = DELETE;
            return DELETE;
        }
        //else if(c == VI_P) //TODO
        //   return ConsoleOperations.PASTE;
        else if(c == VI_S) {
            switchEditMode();
            return DELETE;
        }
        else if(c == VI_SHIFT_S) {
            switchEditMode();
            return CTRL_SHIFT_K;
        }
        else if(c == VI_A) {
            switchEditMode();
            return CTRL_F;
        }
        else if(c == VI_SHIFT_A) {
            switchEditMode();
            return CTRL_E;
        }
        else if(c == VI_I) {
            switchEditMode();
            return 0;
        }
        else if(c == VI_SHIFT_I) {
            switchEditMode();
            return CTRL_A;
        }
        else if(c == VI_D) {
            setDeleteMode(true);
            return 0;
        }
        else if(c == VI_SHIFT_D) {
            latestAction = CTRL_K;
            return CTRL_K;
        }
        else if(c == VI_C) {
            setChangeMode(true);
        }
        else if(c == VI_SHIFT_C) {
            switchEditMode();
            return CTRL_K;
        }
        else if(c == VI_ENTER) {
            switchEditMode();
            return c;
        }
        else if(c == VI_PERIOD) {
            return latestAction;
        }
        else if(c == VI_U) {
            return CTRL_Z;
        }

        return 0;
    }
}

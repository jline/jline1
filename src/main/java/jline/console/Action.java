package jline.console;

/**
 * Describe if the action is a move or a delete
 *
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public interface Action {
    public static int DELETE = 1;
    public static int MOVE = 2;
    public static int YANK = 3;
    public static int CHANGE = 4;
}

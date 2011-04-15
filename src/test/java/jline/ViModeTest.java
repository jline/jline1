package jline;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class ViModeTest extends JLineTestCase {

    public ViModeTest(String test) {
        super(test);
    }

    public void testSimpleMovementAndEdit() throws Exception {
        Buffer b = new Buffer("1234");

        b.append(27) // press esc
                .append(120) //press x
                .append(10); //press enter

        assertBufferViMode("123", b, true);


        b = new Buffer("1234");
        b.append(27) // press esc
         .append(104) // press h
         .append(115) // press s
         .append("5")
         .append(10); // press enter
        assertBufferViMode("1254", b, true);


        b = new Buffer("1234");
        b.append(27) // press esc
                .append(48) //press 0
                .append(120) //press x
                .append(10); //press enter

        assertBufferViMode("234", b, true);

         b = new Buffer("1234");
        b.append(27) // press esc
                .append(48) //press 0
                .append(120) //press x
                .append(108)    // press l
                .append(97)  // press a
                .append("5") // insert 5
                .append(10); //press enter

        assertBufferViMode("2354", b, true);
    }

}

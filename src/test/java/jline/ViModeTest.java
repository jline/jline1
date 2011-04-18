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

        b.append(27) // esc
                .append(120) // x
                .append(10); // enter

        assertBufferViMode("123", b, true);


        b = new Buffer("1234");
        b.append(27) // esc
         .append(104) // h
         .append(115) // s
         .append("5")
         .append(10); // enter
        assertBufferViMode("1254", b, true);


        b = new Buffer("1234");
        b.append(27) // esc
                .append(48) // 0
                .append(120) // x
                .append(10); // enter

        assertBufferViMode("234", b, true);

         b = new Buffer("1234");
        b.append(27) // esc
                .append(48) //0
                .append(120) //x
                .append(108)    // l
                .append(97)  // a
                .append("5") // insert 5
                .append(10); //enter

        assertBufferViMode("2354", b, true);
    }

    public void testWordMovementAndEdit() throws Exception {
        Buffer b = new Buffer("foo   bar...  Foo-Bar.");
        b.append(27) // esc
         .append(66) // shift-b
         .append(100).append(119) // dw
         .append(10);
        assertBufferViMode("foo   bar...  -Bar.", b, true);

        b = new Buffer("foo   bar...  Foo-Bar.");
        b.append(27) // esc
         .append(48) // 0
         .append(87) // W
         .append(87) // W
         .append(100).append(87) // dW
         .append(10); // enter
        assertBufferViMode("foo   bar...  ", b, true);

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(27) // esc
         .append(48) // 0
         .append(119) // w
         .append(119) // w
         .append(100).append(87) // dW
         .append(10); // enter
        assertBufferViMode("foo   barFoo-Bar.", b, true);
    }

}

package jline;

import java.io.IOException;

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
         .append(100).append(98) // db
         .append(10);
        assertBufferViMode("foo   barFoo-Bar.", b, true);

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

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(27) // esc
         .append(66) // B
         .append(100).append(66) // dB
         .append(10);
        assertBufferViMode("foo   Foo-Bar.", b, true);

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(27) // esc
         .append(48) // 0
         .append(119).append(119) // ww
         .append(105) // i
         .append("-bar")
         .append(27)
         .append(66)
         .append(100).append(119) //dw
         .append(120) // x
         .append(100).append(66) //dB
         .append(10);
        assertBufferViMode("bar...   Foo-Bar.", b, true);
    }

    public void testRepeatAndEdit() throws IOException {
        Buffer b = new Buffer("/cd /home/foo; ls; cd Desktop; ls ../");
        b.append(27) // esc
         .append(48) // 0
         .append(119).append(119).append(119).append(119).append(119) // 5x w
         .append(99).append(119) // cw
         .append("bar")
         .append(27) // esc
         .append(87) // W
         .append(100).append(119) //dw
         .append(46) // .
         .append(10);
        assertBufferViMode("/cd /home/bar; cd Desktop; ls ../", b, true);

        b = new Buffer("/cd /home/foo; ls; cd Desktop; ls ../");
        b.append(27) // esc
         .append(66) // B
         .append(68) // D
         .append(66) // B
         .append(46) // .
         .append(66) // B
         .append(46) // .
         .append(10);
        assertBufferViMode("/cd /home/foo; ls; cd ", b, true);
    }

}

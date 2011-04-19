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

        b.append(ViParser.ESCAPE) // esc
         .append("x") // x
         .append(ViParser.VI_ENTER); // enter

        assertBufferViMode("123", b, true);


        b = new Buffer("1234");
        b.append(ViParser.ESCAPE) // esc
         .append("h") // h
         .append("s") // s
         .append("5")
         .append(ViParser.VI_ENTER); // enter
        assertBufferViMode("1254", b, true);


        b = new Buffer("1234");
        b.append(ViParser.ESCAPE) // esc
         .append("0") // 0
         .append("x") // x
         .append(ViParser.VI_ENTER); // enter

        assertBufferViMode("234", b, true);

         b = new Buffer("1234");
        b.append(ViParser.ESCAPE) // esc
         .append("0")
         .append("x")
         .append("l")
         .append("a")
         .append("5")
         .append(ViParser.VI_ENTER);

        assertBufferViMode("2354", b, true);
    }

    public void testWordMovementAndEdit() throws Exception {
        Buffer b = new Buffer("foo   bar...  Foo-Bar.");
        b.append(ViParser.ESCAPE)
         .append("B")
         .append("d").append("b") // db
         .append(ViParser.VI_ENTER);
        assertBufferViMode("foo   barFoo-Bar.", b, true);

        b = new Buffer("foo   bar...  Foo-Bar.");
        b.append(ViParser.ESCAPE)
         .append("0")
         .append("W")
         .append("W")
         .append("d").append("W")
         .append(ViParser.VI_ENTER);
        assertBufferViMode("foo   bar...  ", b, true);

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(ViParser.ESCAPE)
         .append("0")
         .append("w")
         .append("w")
         .append("d").append("W")
         .append(ViParser.VI_ENTER);
        assertBufferViMode("foo   barFoo-Bar.", b, true);

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(ViParser.ESCAPE)
         .append("B")
         .append("d").append("B")
         .append(ViParser.VI_ENTER);
        assertBufferViMode("foo   Foo-Bar.", b, true);

        b = new Buffer("foo   bar...   Foo-Bar.");
        b.append(ViParser.ESCAPE)
         .append("0")
         .append("w").append("w")
         .append("i")
         .append("-bar")
         .append(ViParser.ESCAPE)
         .append("B")
         .append("d").append("w") //dw
         .append("x") // x
         .append("d").append("B") //dB
         .append(ViParser.VI_ENTER);
        assertBufferViMode("bar...   Foo-Bar.", b, true);
    }

    public void testRepeatAndEdit() throws IOException {
        Buffer b = new Buffer("/cd /home/foo; ls; cd Desktop; ls ../");
        b.append(ViParser.ESCAPE)
         .append("0")
         .append("w").append("w").append("w").append("w").append("w")
         .append("c").append("w")
         .append("bar")
         .append(ViParser.ESCAPE)
         .append("W")
         .append("d").append("w")
         .append(".")
         .append(ViParser.VI_ENTER);
        assertBufferViMode("/cd /home/bar; cd Desktop; ls ../", b, true);

        b = new Buffer("/cd /home/foo; ls; cd Desktop; ls ../");
        b.append(ViParser.ESCAPE)
         .append("B")
         .append("D")
         .append("B")
         .append(".")
         .append("B")
         .append(".")
         .append(ViParser.VI_ENTER);
        assertBufferViMode("/cd /home/foo; ls; cd ", b, true);
    }

}

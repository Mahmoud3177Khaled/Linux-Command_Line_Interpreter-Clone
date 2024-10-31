package os.cli;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class echoTest {
    public ByteArrayOutputStream buffer = new ByteArrayOutputStream();

     @Test
     void simpleInput(){
        CLI cli = new CLI("c://");
        System.setOut(new PrintStream(this.buffer));
        cli.echo("Hello World!");
        assertEquals("Hello World!",buffer.toString().substring(0,buffer.toString().length()-2));
        System.setOut(System.out);
        this.buffer.reset();        
     }
   

}

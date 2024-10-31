package os.cli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class pwdTest {
    private CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        // Set initial directories for testing
        cli.currentDir = System.getProperty("user.dir"); // Set to current working directory
        cli.homeDir = System.getProperty("user.home");   // Set to home directory
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);  // Reset console output
        this.outputStream.reset();        // Clear output
    }

    @Test
    public void testPwdDefault() {
        cli.pwd("-l");
        assertEquals(cli.currentDir + "\\", outputStream.toString().trim());
    }

    @Test
    public void testPwdPhysical() {
        cli.pwd("-p");
        assertEquals(cli.currentDir + "\\", outputStream.toString().trim());
    }

    @Test
    public void testPwdHelp() {
        cli.pwd("--help");
        assertTrue(outputStream.toString().contains("Print the name of the current working directory."));
    }

    @Test
    public void testPwdRedirectToFile() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        cli.pwd("-l > " + "test.txt");
        Path filePath = Paths.get(path + "\\", "test.txt");
        assertTrue(Files.exists(filePath));
        assertEquals(path + "\\", Files.readString(filePath).trim());
    }

    @Test
    public void testPwdAppendToFile() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        Path filePath = Paths.get(path + "\\", "test.txt");


        cli.pwd("-l > " + "test.txt");
        assertTrue(Files.exists(filePath));
        assertEquals(path + "\\", Files.readString(filePath).trim());

        cli.pwd("-p >> " + "test.txt");
        String fileContent = Files.readString(filePath);
        assertTrue(fileContent.startsWith(path + "\\"));
        assertTrue(fileContent.endsWith(path + "\\" + path + "\\"));
    }

    @Test
    public void testPwdUnknownArgument() {
        cli.pwd("blahblah");
        String expectedOutput = "blahblah is an unknown argument.\n";
        String actualOutput = outputStream.toString().replace(System.lineSeparator(), "\n");
        assertEquals(expectedOutput.replace(System.lineSeparator(), "\n"), actualOutput);
    }
}

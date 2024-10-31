package os.cli; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class cdTest {
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
    public void testCdHelp() {
        cli.cd("--help");
        assertTrue(this.outputStream.toString().contains("cd: cd [DIRECTORY]"), "Help output not as expected");
    }

    @Test
    public void testCdHomeDirectory() {
        cli.cd("~");
        assertEquals(cli.homeDir, cli.currentDir, "currentDir should be set to homeDir");
    }

    @Test
    public void testCdUpDirectory() {
        String parentDir = new File(cli.currentDir).getParent();
        cli.cd("..");
        assertEquals(parentDir, cli.currentDir, "currentDir should be set to the parent directory");
    }

    @Test
    public void testCdToSpecificDirectory() {
        String testDirPath = cli.currentDir + File.separator + "testDir";
        File testDir = new File(testDirPath);
        testDir.mkdir();  // Create test directory
        
        cli.cd("testDir");
        assertEquals(testDirPath, cli.currentDir, "currentDir should be changed to the specified directory");

        testDir.delete();  // Clean up
    }

    @Test
    public void testCdInvalidDirectory() {
        String nonExistentDir = "nonExistentDir";
        cli.cd(nonExistentDir);
        assertTrue(this.outputStream.toString().contains("Directory " + nonExistentDir + " does not exist"), "Error message for non-existent directory is incorrect");
    }

    @Test
    void testTouchCreatesFile() {
        String path = Paths.get("").toAbsolutePath().toString();
        cli.cd(path);
        cli.touch("test.txt");

        File testFile = new File(path + "\\" + "test.txt");
        assertTrue(testFile.exists() && testFile.isFile());
        testFile.delete();
    }

    @Test
    void testTouchWithEmptyOperand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        cli.touch("");

        String expectedOutput = "touch: missing file operand";
        String actualOutput = outContent.toString().replace(System.lineSeparator(), "").trim();

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
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

}

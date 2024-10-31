package os.cli; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class cdTest {
    private CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        
        cli.currentDir = System.getProperty("user.dir");
        cli.homeDir = System.getProperty("user.home");   
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut); 
        this.outputStream.reset();
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
        testDir.mkdir(); 
        
        cli.cd("testDir");
        assertEquals(testDirPath, cli.currentDir, "currentDir should be changed to the specified directory");

        testDir.delete(); 
    }

    @Test
    public void testCdInvalidDirectory() {
        String nonExistentDir = "nonExistentDir";
        cli.cd(nonExistentDir);
        assertTrue(this.outputStream.toString().contains("Directory " + nonExistentDir + " does not exist"), "Error message for non-existent directory is incorrect");
    }
}

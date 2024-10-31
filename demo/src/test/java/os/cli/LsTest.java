package os.cli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LsTest {
    private CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        cli.currentDir = System.getProperty("user.dir"); // Set to current working directory
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);  // Reset console output
        this.outputStream.reset();    // Clear output
    }

    @Test
    public void testLsShowsFiles() throws IOException {
        // Prepare test directory and files
        Path testDir = Files.createTempDirectory("testDir");
        Files.createFile(testDir.resolve("file1.txt"));
        Files.createFile(testDir.resolve("file2.txt"));
        
        // Change current directory to the test directory
        cli.currentDir = testDir.toString();
        
        cli.ls(""); // Run ls command

        String output = outputStream.toString().trim();
        assertTrue(output.contains("file1.txt"));
        assertTrue(output.contains("file2.txt"));

        // Clean up
        Files.deleteIfExists(testDir.resolve("file1.txt"));
        Files.deleteIfExists(testDir.resolve("file2.txt"));
        Files.deleteIfExists(testDir);
    }

    @Test
    public void testLsWithHiddenFiles() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Files.createFile(testDir.resolve(".hiddenfile"));
        Files.createFile(testDir.resolve("visiblefile.txt"));

        cli.currentDir = testDir.toString();
        
        // Run ls without -a option
        cli.ls(""); 

        String output = outputStream.toString().trim();
        assertFalse(output.contains(".hiddenfile")); // Should not show hidden file

        // Clean up
        Files.deleteIfExists(testDir.resolve(".hiddenfile"));
        Files.deleteIfExists(testDir.resolve("visiblefile.txt"));
        Files.deleteIfExists(testDir);
    }

    @Test
    public void testLsWithAllOption() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Files.createFile(testDir.resolve(".hiddenfile"));
        Files.createFile(testDir.resolve("visiblefile.txt"));

        cli.currentDir = testDir.toString();

        // Run ls with -a option
        cli.ls("-a"); 

        String output = outputStream.toString().trim();
        assertTrue(output.contains(".hiddenfile")); // Should show hidden file
        assertTrue(output.contains("visiblefile.txt"));

        // Clean up
        Files.deleteIfExists(testDir.resolve(".hiddenfile"));
        Files.deleteIfExists(testDir.resolve("visiblefile.txt"));
        Files.deleteIfExists(testDir);
    }

    @Test
    public void testLsWithLongFormat() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Files.createFile(testDir.resolve("file1.txt"));

        cli.currentDir = testDir.toString();

        // Run ls with -l option
        cli.ls("-l"); 

        String output = outputStream.toString().trim();
        assertTrue(output.contains("file1.txt")); // Should show file name
        assertTrue(output.startsWith("rw-")); // Assuming the created file has read/write permissions

        // Clean up
        Files.deleteIfExists(testDir.resolve("file1.txt"));
        Files.deleteIfExists(testDir);
    }

    @Test
    public void testLsRecursive() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Path subDir = Files.createDirectory(testDir.resolve("subDir"));
        Files.createFile(subDir.resolve("fileInSubDir.txt"));
        Files.createFile(testDir.resolve("fileInDir.txt"));

        cli.currentDir = testDir.toString();

        // Run ls with -R option
        cli.ls("-R"); 

        String output = outputStream.toString().trim();
        assertTrue(output.contains("fileInDir.txt")); // Check main directory file
        assertTrue(output.contains("subDir:")); // Check if subdirectory is listed
        assertTrue(output.contains("fileInSubDir.txt")); // Check subdirectory file

        // Clean up
        Files.deleteIfExists(subDir.resolve("fileInSubDir.txt"));
        Files.deleteIfExists(subDir);
        Files.deleteIfExists(testDir.resolve("fileInDir.txt"));
        Files.deleteIfExists(testDir);
    }
}

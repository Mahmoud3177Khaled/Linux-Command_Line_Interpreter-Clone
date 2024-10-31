package os.cli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MvTest {
    private CLI cli;  // Instance of the CLI class
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;  // Store original System.out

    @BeforeEach
    public void setUp() {
        this.cli = new CLI("C:\\");  // Initialize CLI with a dummy path
        System.setOut(new PrintStream(this.outputStream));  // Redirect output to capture
        cli.currentDir = System.getProperty("user.dir"); // Set to current working directory
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);  // Reset console output to original
        this.outputStream.reset();    // Clear captured output
    }

    @Test
    public void testMoveFileToDirectory() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Path fileToMove = Files.createFile(testDir.resolve("fileToMove.txt"));
        
        cli.currentDir = testDir.toString();  // Set current directory to test directory

        // Run mv command to move the file
        cli.mv("fileToMove.txt " + testDir.toString());

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Moved"), "Output should indicate file was moved.");

        // Clean up
        Files.deleteIfExists(fileToMove); // Clean up the test file
        Files.deleteIfExists(testDir); // Clean up the test directory
    }

    @Test
    public void testMoveFileToNonExistentDirectory() {
        cli.mv("nonExistentFile.txt nonExistentDir");
        
        String output = outputStream.toString().trim();
        assertTrue(output.contains("Usage: mv"), "Output should indicate incorrect usage.");
    }

    @Test
    public void testRenameFile() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Path fileToRename = Files.createFile(testDir.resolve("oldName.txt"));
        
        cli.currentDir = testDir.toString();  // Set current directory to test directory

        // Run mv command to rename the file
        cli.mv("oldName.txt newName.txt");

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Renamed"), "Output should indicate file was renamed.");

        // Clean up
        Files.deleteIfExists(testDir.resolve("newName.txt")); // Clean up renamed file
        Files.deleteIfExists(testDir.resolve("oldName.txt")); // Clean up original file
        Files.deleteIfExists(testDir); // Clean up the test directory
    }

    @Test
    public void testMoveFileWithOverwriteConfirmation() throws IOException {
        Path testDir = Files.createTempDirectory("testDir");
        Path fileToMove = Files.createFile(testDir.resolve("fileToMove.txt"));
        Path existingFile = Files.createFile(testDir.resolve("existingFile.txt"));

        cli.currentDir = testDir.toString();  // Set current directory to test directory

        // Simulating user input for confirmation
        System.setIn(new java.io.ByteArrayInputStream("y\n".getBytes()));

        // Run mv command to move the file, which should trigger overwrite confirmation
        cli.mv("fileToMove.txt existingFile.txt");

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Moved"), "Output should indicate file was moved.");

        // Clean up
        Files.deleteIfExists(existingFile); // Clean up existing file
        Files.deleteIfExists(fileToMove); // Clean up moved file
        Files.deleteIfExists(testDir); // Clean up the test directory
    }

    @Test
    public void testMoveNonExistentFile() throws IOException {
        cli.mv("nonExistentFile.txt targetDir");

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Error: File nonExistentFile.txt does not exist"), "Output should indicate that the file does not exist.");
    }
}

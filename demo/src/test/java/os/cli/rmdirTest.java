package os.cli; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class rmdirTest {
    CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOutput = System.out;

    @BeforeEach
    public void setup() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));

        cli.currentDir = System.getProperty("user.dir");
        cli.homeDir = System.getProperty("user.home");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(this.originalOutput);
        this.outputStream.reset();
    }

    @Test
    public void rmdirHelp() {
        cli.rmdir("--help");
        assertTrue(this.outputStream.toString().contains("rmdir [OPTION]... DIRECTORY..."),
         "should print the help text");
    }

    @Test
    public void rmdirVersion() {
        cli.rmdir("--version");
        assertTrue(this.outputStream.toString().contains("rmdir (GNU coreutils)"),
         "should contain the version text");
    }

    @Test
    public void rmdirOnExistingEmptyFolder() {
        File FolderToRemove = new File(cli.currentDir, "AnExistingfolder");
        FolderToRemove.mkdir();

        cli.rmdir("AnExistingfolder");
        assertTrue(!FolderToRemove.exists(), "Exisitng Folder should now be deleted");
    }
    
    @Test
    public void rmdirOnANonExistingFolder() {
        File FolderToRemove = new File(cli.currentDir, "ANonExistingfolder");

        cli.rmdir("ANonExistingfolder");
        assertTrue(this.outputStream.toString().contains("Error: Folder does not exists"), 
        "Error is output as the folder doesn't exist");
    }

    @Test
    public void rmdirOnAFileNotAFolder() {
        File FileNotFolderToRemove = new File(cli.currentDir, "AFileNotAFolder");
        try {
            FileNotFolderToRemove.createNewFile();

            cli.rmdir("AFileNotAFolder");
            assertTrue(this.outputStream.toString().contains("Error: Please provide a folder"),
            "The cli refuses as rmdir deletes empty FOLDERS only");

            FileNotFolderToRemove.delete();
        } catch (IOException e) {
           
        }
    }

    @Test
    public void rmIgnoreOptionTrueOnNonEmptyFolder() {
        try {
            File NonEmptyFolderToDelete = new File(cli.currentDir, "NonEmptyFolderToDelete");
            NonEmptyFolderToDelete.mkdir();
            File Afile = new File(NonEmptyFolderToDelete.getPath(), "Afile.txt");
            Afile.createNewFile();

            cli.rmdir("--ignore-fail-on-non-empty NonEmptyFolderToDelete");
            assertTrue(this.outputStream.toString().isEmpty() && NonEmptyFolderToDelete.exists(), 
            "no error is displayed and the folder is not deleted either");

            Afile.delete();
            NonEmptyFolderToDelete.delete();

        } catch (IOException e) {
            assertTrue(false);
        } 
    }

    @Test
    public void rmIgnoreOptionTrueOnEmptyFolder() {
            File EmptyFolderToDelete = new File(cli.currentDir, "EmptyFolderToDelete");
            EmptyFolderToDelete.mkdir();

            cli.rmdir("--ignore-fail-on-non-empty EmptyFolderToDelete");
            assertTrue(this.outputStream.toString().isEmpty() && !EmptyFolderToDelete.exists(), 
            "no error is displayed and the folder is not deleted either");
    }

    @Test
    public void rmdirVerboseOptionTrueOnAnEmptyFolder() {
        File EmptyFolderToDelete = new File(cli.currentDir, "EmptyFolderToDelete");
        EmptyFolderToDelete.mkdir();

        cli.rmdir("-v EmptyFolderToDelete");
        assertTrue(this.outputStream.toString().contains("Deleted Folder") && !EmptyFolderToDelete.exists(),
        "folder is deleted as it's empty and a message is provided");
    }

    @Test
    public void rmdirVerboseOptionTrueOnANonEmptyFolder() {
        try {
            File NonEmptyFolderToDelete = new File(cli.currentDir, "NonEmptyFolderToDelete");
            NonEmptyFolderToDelete.mkdir();
            File Afile = new File(NonEmptyFolderToDelete.getPath(), "Afile.txt");
            Afile.createNewFile();

            cli.rmdir("-v NonEmptyFolderToDelete");
            assertTrue(this.outputStream.toString().contains("Error: Please provide an empty folder") && NonEmptyFolderToDelete.exists(),
            "folder is not deleted as it's empty and a message is provided");

            Afile.delete();
            NonEmptyFolderToDelete.delete();

        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void rmdirParentOptionTrue() {
        File FolderWithOneEmptyFolderInItOnly = new File(cli.currentDir, "FolderWithOneEmptyFolderInItOnly");
        FolderWithOneEmptyFolderInItOnly.mkdir();
        File EmptyFolderInFolderWithItOnly = new File(FolderWithOneEmptyFolderInItOnly.getPath(), "EmptyFolderInFolderWithItOnly");
        EmptyFolderInFolderWithItOnly.mkdir();

        cli.rmdir("-p FolderWithOneEmptyFolderInItOnly/EmptyFolderInFolderWithItOnly");
        assertTrue(!FolderWithOneEmptyFolderInItOnly.exists() && !EmptyFolderInFolderWithItOnly.exists() &&
        this.outputStream.toString().isEmpty());
    }

    @Test
    public void rmdirParentOptionTrueVervoseOptionTrue() {
        File FolderWithOneEmptyFolderInItOnly = new File(cli.currentDir, "FolderWithOneEmptyFolderInItOnly");
        FolderWithOneEmptyFolderInItOnly.mkdir();
        File EmptyFolderInFolderWithItOnly = new File(FolderWithOneEmptyFolderInItOnly.getPath(), "EmptyFolderInFolderWithItOnly");
        EmptyFolderInFolderWithItOnly.mkdir();

        cli.rmdir("-p -v FolderWithOneEmptyFolderInItOnly/EmptyFolderInFolderWithItOnly");
        assertTrue(!FolderWithOneEmptyFolderInItOnly.exists() && !EmptyFolderInFolderWithItOnly.exists() &&
        this.outputStream.toString().contains("Deleted Folder "));
    }
}

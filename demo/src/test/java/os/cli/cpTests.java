package os.cli; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class cpTests {
    CLI cli;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originaloutput = System.out;
    private final InputStream originalinput = System.in;

    @BeforeEach
    public void setup() {
        this.cli = new CLI("C:\\");
        System.setOut(new PrintStream(this.outputStream));
        // System.setIn(this.originalinput);

        cli.currentDir = System.getProperty("user.dir");
        cli.homeDir = System.getProperty("user.home");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(this.originaloutput);
        System.setIn(this.originalinput);
        this.outputStream.reset();
    }

    @Test
    public void help() {
        String simulatedInput = "y";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        cli.cp("--help", scanner);

        assertTrue(this.outputStream.toString().contains("cp [OPTION]..."));
    }

    @Test
    public void version() {
        String simulatedInput = "y";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        cli.cp("--version", scanner);

        assertTrue(this.outputStream.toString().contains("cp (GNU coreutils)"));
    }
    

    @Test
    public void cpFileToFile() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();

            cli.cp("srcfile.txt destfile.txt", scanner);

            assertTrue(destfile.exists());
            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (IOException ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileTointoDir() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destdir = new File(cli.currentDir, "destdir");
            File destfile = new File(destdir.getPath(), "destfile.txt");

            srcfile.createNewFile();
            destdir.mkdir();

            cli.cp("srcfile.txt destdir/destfile.txt", scanner);

            assertTrue(destfile.exists());
            scanner.close();
            srcfile.delete();
            destfile.delete();
            destdir.delete();


        } catch (IOException ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpManyFilesTointoDir() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile1 = new File(cli.currentDir, "srcfile1.txt");
            File srcfile2 = new File(cli.currentDir, "srcfile2.txt");
            File srcfile3 = new File(cli.currentDir, "srcfile3.txt");
            File destdir = new File(cli.currentDir, "destdir");
            File destfile1 = new File(destdir.getPath(), "srcfile1.txt");
            File destfile2 = new File(destdir.getPath(), "srcfile2.txt");
            File destfile3 = new File(destdir.getPath(), "srcfile3.txt");

            srcfile1.createNewFile();
            srcfile2.createNewFile();
            srcfile3.createNewFile();
            destdir.mkdir();

            cli.cp("srcfile1.txt srcfile2.txt srcfile3.txt destdir", scanner);

            assertTrue(destfile1.exists());
            scanner.close();
            srcfile1.delete();
            srcfile2.delete();
            srcfile3.delete();
            destfile1.delete();
            destfile2.delete();
            destfile3.delete();
            destdir.delete();


        } catch (IOException ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpdir() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcdir = new File(cli.currentDir, "srcdir");
            File destdir = new File(cli.currentDir, "destdir");

            srcdir.mkdir();

            cli.cp("srcdir destdir", scanner);

            assertTrue(destdir.exists());

            scanner.close();
            srcdir.delete();
            destdir.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFulldirToAnother() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcdir = new File(cli.currentDir, "srcdir");
            File srcfile1 = new File(srcdir.getPath(), "srcfile1.txt");
            File srcfile2 = new File(srcdir.getPath(), "srcfile2.txt");
            File srcfile3 = new File(srcdir.getPath(), "srcfile3.txt");

            File destdir = new File(cli.currentDir, "destdir");
            File destfile1 = new File(destdir.getPath(), "srcfile1.txt");
            File destfile2 = new File(destdir.getPath(), "srcfile2.txt");
            File destfile3 = new File(destdir.getPath(), "srcfile3.txt");

            srcdir.mkdir();
            srcfile1.createNewFile();
            srcfile2.createNewFile();
            srcfile3.createNewFile();
            destdir.mkdir();

            cli.cp("srcdir destdir", scanner);

            assertTrue(destfile1.exists() && destfile2.exists() && destfile3.exists());

            scanner.close();
            srcfile1.delete();
            srcfile2.delete();
            srcfile3.delete();
            srcdir.delete();
            destfile1.delete();
            destfile2.delete();
            destfile3.delete();
            destdir.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    public void cpFileWithForceOptiontrueVerboseOptionTrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-f -v srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().contains("Copyied"));

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileWithNoClobberOptiontrueVerboseOptionTrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-n -v srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().contains("skipped"));

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileWithForceOptiontrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-f srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().isEmpty());

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileWithNoClobberOptiontrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-n srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().isEmpty());

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileWithIteractionOptiontrueVerboseOptionTrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-i -v srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().contains("Overide?"));

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }

    @Test
    public void cpFileWithIteractionOptiontrue() {
        try {
            String simulatedInput = "y";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File srcfile = new File(cli.currentDir, "srcfile.txt");
            File destfile = new File(cli.currentDir, "destfile.txt");

            srcfile.createNewFile();
            destfile.createNewFile();

            cli.cp("-i srcfile.txt destfile.txt", scanner);

            assertTrue(this.outputStream.toString().contains("Overide?"));

            scanner.close();
            srcfile.delete();
            destfile.delete();


        } catch (Exception ex) {
            assertTrue(false);
        }

    }


}
package os.cli; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ConvertToTryWithResources")
public class catTests {
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
    public void helpOption() {
        String simulatedInput = "Outputfile.txt";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        cli.cat("--help", scanner);
        assertTrue(this.outputStream.toString().contains("cat [OPTION]... [FILE]..."),
         "displayes help text");
        scanner.close();
    }

    @Test
    public void versionOption() {
        String simulatedInput = "Outputfile.txt";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        cli.cat("--version", scanner);
        assertTrue(this.outputStream.toString().contains("cat (GNU coreutils)"),
        "displays version text");
        scanner.close();
    }

    @Test
    public void catPrintFile() {
        try {
            String simulatedInput = "1234";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            String txt = "1234\\n";
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");
            readFile.createNewFile();
            FileWriter writer = new FileWriter(readFile);
            writer.write(txt);
            writer.close();

            cli.cat("readFile.txt", scanner);
            assertTrue(this.outputStream.toString().contains(txt));

            scanner.close();
            readFile.delete();
        } catch (IOException e) {
            assertTrue(false);
        }

    }

    @Test
    public void catPrintFileWithLineNums() {
        try {
            String simulatedInput = "1234";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            String txt = "1234\\n";
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");
            readFile.createNewFile();
            FileWriter writer = new FileWriter(readFile);
            writer.write(txt);
            writer.close();

            cli.cat("-n readFile.txt", scanner);
            assertTrue(this.outputStream.toString().contains("- " + txt));

            scanner.close();
            readFile.delete();
        } catch (IOException e) {
            assertTrue(false);
        }

    }

    @Test
    public void catWriteToFileOp() {
        try {
            String simulatedInput = "12345";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");

            cli.cat("> readFile.txt", scanner);

            Scanner filescanner = new Scanner(readFile);
            String fileText = filescanner.nextLine();
            assertEquals(simulatedInput, fileText);

            scanner.close();
            filescanner.close();
            readFile.delete();
        } catch (IOException e) {
            assertTrue(false);
        }

    }

    @Test
    public void catAppendToFileOp() {
        try {
            String simulatedInput = "12345";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");
            readFile.createNewFile();

            Scanner filescanner = new Scanner(readFile);
            String fileText = "";
            while (filescanner.hasNextLine()) { 
                fileText += filescanner.nextLine();
            }
            filescanner.close();

            cli.cat(">> readFile.txt", scanner);

            Scanner filescanner2 = new Scanner(readFile);
            String fileText2 = filescanner2.nextLine();
            filescanner2.close();

            assertEquals(fileText + simulatedInput, fileText2);

            scanner.close();
            readFile.delete();
        } catch (IOException e) {
            assertTrue(false);
        }

    }

    @Test
    public void catAppendToFileOpFromFile() {
        try {
            String simulatedInput = "12345";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");
            File writeFile = new File(cli.currentDir, "writeFile.txt");

            readFile.createNewFile();
            writeFile.createNewFile();

            FileWriter writter1 = new FileWriter(readFile);
            writter1.write(simulatedInput);
            writter1.close();

            FileWriter writter2 = new FileWriter(writeFile);
            writter2.write(simulatedInput);
            writter2.close();


            Scanner filescanner = new Scanner(readFile);
            String fileText = filescanner.nextLine();
            filescanner.close();

            Scanner filescanner2 = new Scanner(writeFile);
            String fileText2 = filescanner2.nextLine();
            filescanner2.close();


            cli.cat("readFile.txt >> writefile.txt", scanner);

            Scanner filescanner3 = new Scanner(writeFile);
            String fileText3 = "";
            while(filescanner3.hasNextLine()) {
                fileText3 += filescanner3.nextLine();
            }
            filescanner3.close();


            assertEquals(fileText2 + fileText, fileText3);
            scanner.close();
            readFile.delete();
            writeFile.delete();

        } catch (IOException e) {
            assertTrue(false);
            // assertEquals("", file);
        }


    }
    @Test
    public void catWriteToFileOpFromFile() {
        try {
            String simulatedInput = "123456";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            File readFile = new File(cli.currentDir, "readFile.txt");
            File writeFile = new File(cli.currentDir, "writeFile.txt");

            readFile.createNewFile();
            writeFile.createNewFile();

            FileWriter writter1 = new FileWriter(readFile);
            writter1.write(simulatedInput);
            writter1.close();


            Scanner filescanner = new Scanner(readFile);
            String fileText = filescanner.nextLine();
            filescanner.close();

            cli.cat("readFile.txt > writefile.txt", scanner);

            Scanner filescanner3 = new Scanner(writeFile);
            String fileText3 = "";
            while(filescanner3.hasNextLine()) {
                fileText3 += filescanner3.nextLine();
            }
            filescanner3.close();


            assertEquals(fileText, fileText3);
            scanner.close();
            readFile.delete();
            writeFile.delete();

        } catch (IOException e) {
            assertTrue(false);
            // assertEquals("", file);
        }

    }
    
}

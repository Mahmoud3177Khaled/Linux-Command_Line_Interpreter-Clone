package os.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {

            CLI cli = new CLI("C:\\");
            boolean run = true;
            String arg;
            String command;

            while (run) {
                System.out.print(cli.getCurrentDir() + ">");

                command = input.next();
                arg = input.nextLine().trim();

                switch (command) {
                    case "pwd" ->
                            cli.pwd(arg);
                    case "cd" ->
                            cli.cd(arg);
                    case "mkdir" ->
                            cli.mkdir(arg);
                    case "touch" ->
                            cli.touch(arg);
                    case "mv" ->
                            cli.mv(arg);
                    case "rm" ->
                            cli.rm(arg);
                    case "echo" ->
                            cli.echo(arg);
                    case "man" ->
                            cli.man(arg);
                    case "rmdir" ->
                            cli.rmdir(arg);
                    case "cat" ->
                            cli.cat(arg, input);
                    case "ls" ->
                            cli.ls(arg);
                    case "uname" ->
                            cli.uname(arg);
                    case "cp" ->
                            cli.cp(arg, input);
                    // case "<" ->
                    //         cli.inputOp(arg);
                    case ">" ->
                            cli.redirectOutput(arg);
                    case "more" ->
                            cli.more(arg);
                    case "users" ->
                            cli.users();
                    case "clear" ->
                            cli.clear();
                    case "exit" -> {
                        return;
                    }

                    default ->
                            cli.UndefinedInput(command);
                }
            }
        }
    }
}

class CLI {
    private String currentDir;
    private String homeDir;

    public CLI(String currentDir) {
        this.currentDir = currentDir;
        this.homeDir = currentDir;
    }

    public String getCurrentDir() {
        return this.currentDir;
    }

    public void UndefinedInput(String com) {
        System.out.print(com + " is not a recognized command,");
        System.out.println(" please make sure you typed a legal command or try again.");
    }

    private String[] proccess_args(String com) {
        String[] proccessed_args = com.split(" ");
        return proccessed_args;
    }

    // 
    public void pwd(String com) {  //20220027
        try {
            String output = null;
            boolean redirectToFile = false;
            boolean appendMode = false;
            String fileName = null;

            if (com.contains(">")) {
                redirectToFile = true;
                String[] parts = com.split(">");
                com = parts[0].trim();
                fileName = parts[1].trim();
            } else if (com.contains(">>")) {
                redirectToFile = true;
                appendMode = true;
                String[] parts = com.split(">>");
                com = parts[0].trim();
                fileName = parts[1].trim();
            }

            if (com.isEmpty() || com.equalsIgnoreCase("-l")) {
                Path currentDirectoryPath = Paths.get(this.currentDir);
                output = currentDirectoryPath.toAbsolutePath().toString() + "\\";
            } else if (com.equalsIgnoreCase("-p")) {
                Path currentDirectoryPath = Paths.get(this.currentDir);
                output = currentDirectoryPath.toString() + "\\";
            } else if (com.equals("--help")) {
                output = "pwd: pwd [-LP]\n"
                        + "    Print the name of the current working directory.\n\n"
                        + "    Options:\n"
                        + "      -L        print the value of $PWD if it names the current working\n"
                        + "                directory\n"
                        + "      -P        print the physical directory, without any symbolic links\n\n"
                        + "    By default, `pwd' behaves as if `-L' were specified.\n\n"
                        + "    Exit Status:\n"
                        + "    Returns 0 unless an invalid option is given or the current directory\n"
                        + "    cannot be read.\n";
            } else {
                output = com + " is an unknown argument.\n";
            }

            if (redirectToFile) {
                FileWriter writer = new FileWriter(this.currentDir + "\\" + fileName, appendMode);
                writer.write(output);
                writer.close();
            } else {
                System.out.println(output);
            }

        } catch (IOException e) {
            System.err.println("File operation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    public void who(String com) {
        String[] args = proccess_args(com);
        boolean quietMode = args.length > 0 && "-q".equals(args[0]);
    
        try {
            // Using PowerShell to get the logged-in username
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", 
                "-Command", 
                "Get-WmiObject -Class Win32_ComputerSystem | Select-Object -ExpandProperty UserName");
            Process process = processBuilder.start();
    
        
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder users = new StringBuilder();
    
                while ((line = reader.readLine()) != null) {
                    String username = line.trim(); 
                    if (!quietMode) {
                        System.out.println(username); 
                    }
                    users.append(username).append(" "); 
                }
    
                
                if (quietMode) {
                    System.out.println(users.toString().trim()); 
                    System.out.println("Total users: " + users.toString().trim().split(" ").length); 
                }
            }
    
            process.waitFor(); 
        } catch (IOException | InterruptedException e) {
            System.out.println("Error executing who command: " + e.getMessage());
        }
    }
    
    public void ls(String com) { //20220028
        String[] MyArgs = proccess_args(com);
    
        boolean showAll = false;    
        boolean longFormat = false;  
        boolean humanReadable = false; 
        boolean recursive = false;    
        boolean sortByTime = false;   
        boolean reverseOrder = false; 
        boolean sortBySize = false;   
    
        for (String param : MyArgs) {
            switch (param) {
                case "-a": showAll = true; break;
                case "-l": longFormat = true; break;
                case "-h": humanReadable = true; break;
                case "-R": recursive = true; break;
                case "-t": sortByTime = true; break;
                case "-r": reverseOrder = true; break;
                case "-S": sortBySize = true; break;
            }
        }
    
        File dir = new File(this.currentDir);
        File[] files = dir.listFiles();
    
        if (files == null) {
            System.out.println("Error: Could not access directory.");
            return;
        }
    
        if (!showAll) {
            files = Arrays.stream(files)
                    .filter(file -> !file.getName().startsWith("."))
                    .toArray(File[]::new);
        }
    
        if (sortByTime) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        } else if (sortBySize) {
            Arrays.sort(files, Comparator.comparingLong(File::length));
        }
    
        if (reverseOrder) {
            Collections.reverse(Arrays.asList(files));
        }
    
        for (File file : files) {
            String output = file.getName();
            if (longFormat) {
                output = getLongFormatString(file, humanReadable);
            }
            System.out.println(output);
        }
    
        if (recursive) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("\n" + file.getName() + ":");
                    CLI subCLI = new CLI(file.getAbsolutePath());
                    subCLI.ls("");
                }
            }
        }
    }
    
    private String getLongFormatString(File file, boolean humanReadable) {
        StringBuilder sb = new StringBuilder();
        sb.append(file.canRead() ? "r" : "-");
        sb.append(file.canWrite() ? "w" : "-");
        sb.append(file.canExecute() ? "x" : "-");
        sb.append(" ");
        sb.append(file.isDirectory() ? "d" : "-");
        sb.append(" ");
        sb.append(getSizeString(file.length(), humanReadable));
        sb.append(" ");
        sb.append(file.getName());
        return sb.toString();
    }
    
    private String getSizeString(long size, boolean humanReadable) {
        if (humanReadable) {
            if (size < 1024) return size + " B";
            else if (size < 1048576) return (size / 1024) + " KB";
            else if (size < 1073741824) return (size / 1048576) + " MB";
            else return (size / 1073741824) + " GB";
        }
        return String.valueOf(size);
    }

    public void more(String com) {
        try (BufferedReader br = new BufferedReader(new FileReader(this.currentDir + "\\" + com))) {
            String line;
            int countLines = 0;
            while ((line = br.readLine()) != null) {
                countLines++;
                if (countLines > 10) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Continue printing? (Y/N)");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("y")) {
                        countLines = 0;
                        System.out.println(line);
                        continue;
                    } else if (input.equalsIgnoreCase("n")) {
                        return;
                    }
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
    
    public void less(String com) {
        String[] args = proccess_args(com);
        if (args.length < 1) {
            System.out.println("Usage: less <filename>");
            return;
        }

        String filename = args[0];
        File file = new File(filename);

        if (!file.exists() || !file.isFile()) {
            System.out.println("Error: File does not exist or is not a regular file.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             Scanner scanner = new Scanner(System.in)) {

            String line;
            int lineCount = 0;
            int pageSize = 20; 
            StringBuilder pageContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                pageContent.append(line).append(System.lineSeparator());
                lineCount++;

             
                if (lineCount == pageSize) {
                    System.out.print(pageContent.toString());
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine(); // Wait for user to press Enter
                    pageContent.setLength(0); // Clear the page content
                    lineCount = 0; // Reset line count for the next page
                }
            }

            if (pageContent.length() > 0) {
                System.out.print(pageContent.toString());
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    // --------------------------- # philo karam 20220246 # --------------------------- //
    private void createParentDirectory(String path) {
        File f, pf;
        f = new File(path);
        String p = f.getParent();
        pf = new File(p);
        if (!pf.exists()) {
            createParentDirectory(p);
        }
        f.mkdir();
    }
    public String makeAbsolutePath(String path){
        if ((!path.contains("\\") && !path.contains("/"))|| (path.charAt(1) != ':') ){
            path = currentDir + "\\"+ path;
        } 
        return path;
    } 
   //--------------------------------------------------------------------------------------------------- 
    private void removeAllInADir(String path,boolean iOption ,boolean verboseOption){
        File f = new File(path),child;
        Scanner in = new Scanner(System.in);
        String remove;
        if(f.listFiles() == null ||f.listFiles().length == 0){
            if(iOption){
                System.out.println("rm: remove directory \'" + path + "\'?");
                remove = in.next();
                if(remove.equals("y")){
                    f.delete();
                    if (verboseOption) {
                        System.out.println("removed \'" + path + "\' ");
                    }
                }
            }else{
                f.delete();
                if (verboseOption) {
                    System.out.println("removed \'" + path + "\' ");
                }
            }
            return;
        }

            for(int i = 0 ; i < f.listFiles().length ;i++){
                if (f.listFiles()[i].isFile()){
                    child = f.listFiles()[i];
                    if(iOption){
                        System.out.print("rm: remove regular file \\'" + child.getAbsolutePath() + "\\'(y/n)?");
                        remove = in.next();
                        if(remove.equals("y")){
                            child.delete();
                            if (verboseOption) {
                                System.out.println("removed \'" + child.getAbsolutePath() + "\' ");
                            }
                        }
                    }
                    else{
                        child.delete();
                        if (verboseOption) {
                            System.out.println("removed \'" + child.getAbsolutePath() + "\' ");
                        }
                    }
                }
                else{
                    if(f.listFiles() == null ||f.listFiles().length == 0){
                        System.out.println(path);
                        if(iOption){
                            System.out.println("rm: remove directory \'" + path + "\'?");
                            remove = in.next();
                            if(remove.equals("y")){
                                f.delete();
                                if (verboseOption) {
                                    System.out.println("removed \'" + path + "\' ");
                                }
                            }
                        }else{
                            f.delete();
                            if (verboseOption) {
                                System.out.println("removed \'" + path + "\' ");
                            }
                        }
                    }
                    else{

                        removeAllInADir(f.listFiles()[i].getAbsolutePath(), iOption, verboseOption);
                        i--;
                    }
                }
        }
    }
    //-------------------------------------------------------------------------------------------------

    public void mkdir(String com) {

        /* 1- split command to options and paths  */
        ArrayList<String> paths = new ArrayList<>();
        String path = "";
        String option = "";
        int size = 0;
        boolean parentOption = false, verboseOption = false;
        for (int i = 0; i < com.length(); i++) {

            if (i < com.length() - 1 && com.charAt(i) == '-' && com.charAt(i + 1) == '-') {
                while (i < com.length() && com.charAt(i) != ' ') {
                    option += com.charAt(i);
                    i++;
                }
                if (option.equals("--parents")) {
                    parentOption = true;
                } else if (option.equals("--verbose")) {
                    verboseOption = true;
                } else if (option.equals("--help")) {
                    System.out.println("""
                        Usage: mkdir [OPTION]... DIRECTORY...\r
                        \r
                        Create the DIRECTORY(ies), if they do not already exist.\r
                        \r
                        Mandatory arguments to long options are mandatory for short options too.\r
                        -p, --parents        make parent directories as needed\r
                        -v, --verbose        print a message for each created directory\r
                            --help           display this help and exit\r
                            --version        output version information and exit\r
                            \r
                            Report mkdir bugs to bug-coreutils@gnu.org\r
                            GNU coreutils home page: <http://www.gnu.org/software/coreutils/>\r
                            General help using GNU software: <http://www.gnu.org/gethelp/>\r
                            """
                    );
                    return;
                } else if (option.equals("--version")) {
                    System.out.println("""
                                        mkdir (GNU coreutils) 8.32\r
                                        Copyright (C) 2020 Free Software Foundation, Inc.\r
                                        License GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>.\r
                                        This is free software: you are free to change and redistribute it.\r
                                        There is NO WARRANTY, to the extent permitted by law.\r
                                        \r
                                        Written by Philopateer Karam.\r
                                        """
                    );
                    return;
                } else {
                    System.out.println("mkdir: unrecognized option\'" + option + "\'");
                    System.out.println("Try \'mkdir --help\' for more information.");
                    return;
                }
                option = "";
            } else if (i < com.length() - 1 && com.charAt(i) == '-' && Character.isAlphabetic(com.charAt(i + 1))) {
                if (com.charAt(i + 1) == 'p') {
                    parentOption = true;
                }
                else if (com.charAt(i + 1) == 'v') {
                    verboseOption = true;
                }
                else {
                    System.out.println("mkdir: unrecognized option\'" + option + "\'");
                    System.out.println("Try \'mkdir --help\' for more information.");
                    return;
                }
                i++;
            } else if (com.charAt(i) == ' ') {
                if (size != 0) {
                    paths.add(path);
                    path = "";
                    size = 0;
                }
            } else {
                path += com.charAt(i);
                size++;
            }
        }
        if (size != 0) {
            paths.add(path);
    }
    /* 2-chick the paths is correct and create directories*/
    File f, pf;
    String check_path;
    if (!parentOption) {
            for (int i = 0; i < paths.size(); i++) {
                check_path = makeAbsolutePath(paths.get(i));
                f = new File(check_path);
                String p = f.getParent();
                pf = new File(p);
                if (!pf.exists()) {
                    System.out.println("mkdir: cannot create directory \'" + paths.get(i) + "\': No such file or directory");

                }else{
                    f.mkdir();
                    if (verboseOption) {
                        System.out.println("mkdir: created directory \'" + paths.get(i) + "\' ");
                    }
                }
            }
    }
    else{
            for (int i = 0; i < paths.size(); i++) {
            check_path = makeAbsolutePath(paths.get(i));
            createParentDirectory(check_path);
            if (verboseOption) {
                System.out.println("mkdir: created directory \'" + paths.get(i) + "\' ");
            }
        }
    }
}

//------------------------------------------------------------------------------------------------------
public void echo(String com){

        /* 1- split command to options and paths  */
        String text = "";
        String option = "";
        boolean newline = true, enableEscapeCarcters = false;
        for (int i = 0; i < com.length(); i++) {

            if (i < com.length() - 1 && com.charAt(i) == '-' && com.charAt(i + 1) == '-') {
                while (i < com.length() && com.charAt(i) != ' ') {
                    option += com.charAt(i);
                    i++;
                }
                if (option.equals("--help")) {
                    System.out.println("""
                        echo: echo [OPTION]... [STRING]...
                        Output the STRING(s) to standard output.

                        -n         do not output the trailing newline
                        -e         enable interpretation of backslash escapes
                        -E         disable interpretation of backslash escapes (default)
                        --help     display this help message and exit
                        --version  output version information and exit
                            """
                    );
                    return;
                } else if (option.equals("--version")) {
                    System.out.println("""
                                    echo (GNU coreutils) 8.32
                                    Copyright (C) 2020 Free Software Foundation, Inc.
                                    License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>
                                    This is free software: you are free to change and redistribute it.
                                    There is NO WARRANTY, to the extent permitted by law.
                                    Written by Philopateer Karam.\r
                                        """
                    );
                    return;
                } else {
                    System.out.println("echo: invalid option \'" + option + "\'");
                    System.out.println("Try \'echo --help\' for more information.");
                    return;
                }
            } else if (i < com.length() - 1 && com.charAt(i) == '-' && Character.isAlphabetic(com.charAt(i + 1))) {
                if (com.charAt(i + 1) == 'n') {
                    newline = false;
                }
                else if (com.charAt(i + 1) == 'E') {
                    enableEscapeCarcters = false;
                }
                else if (com.charAt(i + 1) == 'e') {
                    enableEscapeCarcters = true;
                }
                else {
                    System.out.println("echo: invalid option \'" + option + "\'");
                    System.out.println("Try \'echo --help\' for more information.");
                    return;
                }
                i++;
            }
            else if(com.charAt(i) == '*'){
                ls(com);
                return;
            }
            else if (i < com.length() - 1 && (com.charAt(i) != '-' )&& (com.charAt(i) != ' ' )) {
                for (int j = i; j < com.length(); j++) {
                    text += com.charAt(j);
                }
                break;
            }
            } 
            /* 2-print the input*/
            if(!enableEscapeCarcters){
                if (newline) {
                    System.out.println(text);
                }
                else{
                    System.out.print(text);
                }
            }else{
                ArrayList<String> lines = new ArrayList<>();
                String newText ="";
                for (int i = 0; i < text.length(); i++) {
                    if(text.charAt(i) == '\\' && i+1 <text.length()){
                        if(text.charAt(i+1) == 'n' || text.charAt(i+1) == 'f' ){
                                lines.add(newText);
                                newText = "";
                                i++;
                        }else if (text.charAt(i+1) == '\\') {
                            newText += '\\';
                            i++;

                        }else if (text.charAt(i+1) == '\'') {
                            newText += '\'';
                            i++;

                        }else if (text.charAt(i+1) == '\"') {
                            newText += '\"';
                            i++;

                        }else if (text.charAt(i+1) == 't') {
                            newText += "    ";
                            i++;

                        }else if (text.charAt(i+1) == 'b') {
                            int j = newText.length()-1;
                            for (; j >= 0;j--) {
                                if(newText.charAt(j) != ' '){
                                    break;
                                }
                            }
                            if(j != newText.length()-1){
                                newText = newText.substring(0, j+1);
                            }
                            for ( i = i+2; i < text.length(); i++) {
                                if(text.charAt(i) != ' '){
                                    i--;
                                    break;
                                }
                            }

                        }else if (text.charAt(i+1) == 'c') {
                           newline = false;
                            break;
                        }
                        else if (text.charAt(i+1) == 'r') {
                            newText ="";
                            i++;
                        }
                        else if (text.charAt(i+1) == 'v') {
                            int size = newText.length();
                            lines.add(newText);
                            newText ="";
                            while (size > 0) {
                                newText += ' ';
                                size--;
                            }
                            i++;
                        }
                        else if (text.charAt(i+1) == '0') {
                            i++;
                        }
                    }
                    else{
                        newText += text.charAt(i);
                    }  
                }
                if(newText.length() != 0){
                    lines.add(newText);
                }
                if (newline) {
                    for(int j = 0 ; j <lines.size();j++){
                        System.out.println(lines.get(j));
                    }
                }
                else{
                    for(int j = 0 ; j <lines.size()-1;j++){
                        System.out.println(lines.get(j));
                    }
                    System.out.print(lines.get(lines.size()-1));
                }
            }


        }
//----------------------------------------------------------------------------------------------------
public void man(String com){
    com = com.trim();
    boolean fOption = false;
    if (com.charAt(0) == '-'){
        if(com.charAt(1) == 'f'){
            fOption = true;
            com = com.substring(2,com.length());
        }
        else if(com.charAt(1) == '-'){
            if(com.contains("--help")){
                System.out.println("""
 Usage: man [OPTION]... [PAGE]...
Display the manual page for the specified command or topic.

Options:
  -f, --whatis      Display a short description of the specified command.
  --help            Display this help message and exit.
  --version         Output version information and exit.                       
                        """);
                        return;
            }
            else if(com.contains("--version")){
                System.out.println("""
man (GNU man) 2.9.3
Copyright (C) 2020 Free Software Foundation, Inc.
License GPLv3+: GNU General Public License v3 or later <https://gnu.org/licenses/gpl.html>.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

Written by Philopateer Karam.                       
                        """);
                        return;
            }
            else if(com.contains("--whatis")){
                fOption = true;
                com = com.substring(8,com.length());
            }
        }
    }
    com = com.trim();
if(fOption){
    if(com.equals("pwd")){
        System.out.println("pwd - print name of current working directory.");
    }else if(com.equals("touch")){
        System.out.println("touch - change file timestamps");
    }else if(com.equals("clear")){
        System.out.println("clear - clear the terminal screen");
    }else if(com.equals("users")){
        System.out.println("users - print the usernames of users currently logged in to the current host.");
    }else if(com.equals("more")){
        System.out.println("more - file perusal filter for viewing text one screen at a time");
    }
    else if(com.equals("cd")){
        System.out.println("cd - Change the shell working directory.");
    }
    else if(com.equals("mv")){
        System.out.println("mv - move (rename) files.");
    }
    else if(com.equals("date")){
        System.out.println("date - print or set the system date and time");
    }
    else if(com.equals("who")){
        System.out.println("who - show who is logged on");
    }
    else if(com.equals("less")){
        System.out.println("less - opposite of more.");
    }
    else if(com.equals("mkdir")){
        System.out.println("mkdir - make directories.");
    }else if(com.equals("rm")){
        System.out.println("rm - remove files or directories.");
    }else if(com.equals("echo")){
        System.out.println("echo - display a line of text.");
    }else if(com.equals("man")){
        System.out.println("man - an interface to the system reference manuals.");
    }else if(com.equals("rmdir")){
        System.out.println("rmdir - remove empty directories.");
    }else if(com.equals("cat")){
        System.out.println("cat - concatenate and display files.");
    }else if(com.equals("ls")){
        System.out.println("ls - list directory contents.");
    }else if(com.equals("uname")){
        System.out.println("uname - print system information.");
    }else if(com.equals("cp")){
        System.out.println("cp - copy files and directories.");
    }else {
        System.out.println("man: no entry for "+com);
    }
}
else{
    if(com.equals("pwd")){
        System.out.println("NAME\n" + //
                        "       pwd - print name of current working directory\n" + //
                        "\n" + //
                        "SYNOPSIS\n" + //
                        "       pwd [OPTION]\n" + //
                        "\n" + //
                        "DESCRIPTION\n" + //
                        "       The pwd command prints the full filename of the current working directory.\n" + //
                        "\n" + //
                        "OPTIONS\n" + //
                        "       -L, --logical\n" + //
                        "              use PWD from environment, even if it contains symlinks\n" + //
                        "\n" + //
                        "       -P, --physical\n" + //
                        "              avoid all symlinks; print the actual physical directory\n" + //
                        "\n" + //
                        "       --help\n" + //
                        "              display this help and exit\n" + //
                        "\n" + //
                        "       --version\n" + //
                        "              output version information and exit");
    }else if(com.equals("touch")){
        System.out.println("""
                           NAME
                                  touch - change file timestamps
                           
                           SYNOPSIS
                                  touch [OPTION]... FILE...
                           
                           DESCRIPTION
                                  The  touch command updates the access and modification timestamps of the specified FILEs.  If FILE does not exist, and if the -c option is not given, an empty file is created.
                           
                           OPTIONS
                                  -a, --time=atime
                                         change only the access time
                           
                                  -m, --time=mtime
                                         change only the modification time
                           
                                  -c, --no-create
                                         do not create any files
                           
                                  -d, --date=STRING
                                         parse STRING and use it instead of the current time
                           
                                  -r, --reference=FILE
                                         use this file's times instead of the current time
                           
                                  --help
                                         display this help and exit
                           
                                  --version
                                        output version information and exit""" 
        );
    }else if(com.equals("clear")){
        System.out.println("""
    NAME
       clear - clear the terminal screen
    
    SYNOPSIS
       clear
    
    DESCRIPTION
       clear clears your screen if this is possible, including its scrollback buffer (if the extended "E3" capability is defined).  clear looks in the environment for the terminal type and then in the terminfo database to determine how to clear the screen.
    
       clear ignores any command-line parameters that may be present.           
                """);
    }else if(com.equals("users")){
        System.out.println("""
    NAME
       users - print the usernames of users currently logged in to the current host
    
    SYNOPSIS
       users [OPTION]... [FILE]
    
    DESCRIPTION
       Output who is currently logged in according to FILE.  If FILE is not specified, use /var/run/utmp.  /var/log/wtmp as FILE is common.
    
    OPTIONS
       -h, --help
              display this help and exit
    
       --version
              output version information and exit               
                """);
    }else if(com.equals("more")){
        System.out.println("""
    NAME
       more - file perusal filter for viewing text one screen at a time
    
    SYNOPSIS
       more [options] [file...]
    
    DESCRIPTION
       more is a filter for paging through text one screenful at a time. This command is mostly used for viewing large files or command output.
    
    OPTIONS
       -d     Prompt with [Press space to continue, 'q' to quit.] and display "[Press h for instructions.]" at the bottom of each screen.
    
       -f     Count logical, rather than screen lines (i.e., long lines are not wrapped).
    
       -p     Clear the screen before displaying each page.
    
       -c     Do not scroll. Instead, paint each page directly onto the screen.
    
       -s     Squeeze multiple blank lines into one.
    
       -u     Suppress underlining.
    
       +n     Start displaying each file at line n.
    
       /pattern
              Search for the specified pattern in the text.
    
       :n     Go to the next file.
    
       :p     Go to the previous file.
    
       :q or q
              Quit.
    
                """);
    }
    else if(com.equals("cd")){
        System.out.println("""
    cd: cd [-L|-P] [directory]
    Change the shell working directory.
    
    Change the current directory to DIR. The default DIR is the value of the HOME shell variable.
    
    Options:
      -L  force symbolic links to be followed: resolve symbolic links in DIR
          after processing instances of `..'
      -P  use the physical directory structure without following symbolic links
          (e.g., if in /tmp and /tmp is a symlink to /var/tmp, `cd -P ..` will
          go to /var, whereas `cd ..` will go to /tmp)
      -e  if the -P option is supplied, and the current working directory
          cannot be determined successfully, exit with a non-zero status
                
                """);
    }
    else if(com.equals("mv")){
        System.out.println("""
    NAME
       mv - move (rename) files
    
    SYNOPSIS
       mv [OPTION]... SOURCE... DIRECTORY
       mv [OPTION]... SOURCE... DEST
    
    DESCRIPTION
       Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.
    
    OPTIONS
       -f, --force
              Do not prompt before overwriting.
    
       -i, --interactive
              Prompt before overwriting (overrides --force).
    
       -n, --no-clobber
              Do not overwrite an existing file.
    
       -u, --update
              Move only when the SOURCE file is newer than the destination file or when the destination file is missing.
    
       -v, --verbose
              Explain what is being done.
    
       --backup[=CONTROL]
              Make a backup of each existing destination file.
    
       --suffix=SUFFIX
              Override the usual backup suffix.
    
       -t, --target-directory=DIRECTORY
              Move all SOURCE arguments into DIRECTORY.
    
       -T, --no-target-directory
              Treat DEST as a normal file.
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.               
                """);
    }
    else if(com.equals("date")){
        System.out.println("""
    NAME
       date - print or set the system date and time
    
    SYNOPSIS
       date [OPTION]... [+FORMAT]
       date [-u|--utc|--universal] [MMDDhhmm[[CC]YY][.ss]]
    
    DESCRIPTION
       Display the current time in the given FORMAT, or set the system date.
    
       With no options, `date` displays the current date and time.
    
    OPTIONS
       -d, --date=STRING
              Display time described by STRING, not 'now'.
    
       -f, --file=DATEFILE
              Like --date, but interprets each line of DATEFILE as a date.
    
       -I[TIMESPEC], --iso-8601[=TIMESPEC]
              Output date/time in ISO 8601 format. TIMESPEC='auto', 'date', 'hours', 'minutes', or 'seconds' for specific formats.
    
       -r, --reference=FILE
              Display the last modification time of FILE.
    
       -R, --rfc-2822
              Output date and time in RFC 2822 format.
    
       -s, --set=STRING
              Set time described by STRING.
    
       -u, --utc, --universal
              Display or set the date in UTC (Coordinated Universal Time).
    
       --help Display help and exit.
    
       --version
              Output version information and exit.              
                """);
    }
    else if(com.equals("who")){
        System.out.println("""
    NAME
       who - show who is logged on
    
    SYNOPSIS
       who [OPTION]... [FILE] [am i]
    
    DESCRIPTION
       Print information about users who are currently logged in.
    
    OPTIONS
       -a, --all
              Same as -b -d --login -p -r -t -T -u.
    
       -b, --boot
              Time of last system boot.
    
       -d, --dead
              Print dead processes.
    
       -H, --heading
              Print a line of column headings.
    
       -l, --login
              Print system login processes.
    
       -m     Same as `who am i'.
    
       -p, --process
              Print active processes spawned by init.
    
       -q, --count
              All login names and number of users logged on.
    
       -r, --runlevel
              Print the current runlevel.
    
       -s     Ignored; included for compatibility.
    
       -t, --time
              Print last system clock change.
    
       -T, -w, --mesg
              Add user message status as +, - or ?.
    
       -u, --users
              List users logged in.
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.
               
                """);
    }
    else if(com.equals("less")){
        System.out.println("""
     NAME
       less - opposite of more
    
    SYNOPSIS
       less [options] [file...]
    
    DESCRIPTION
       Less is a program similar to more, but which allows backward movement in the file as well as forward movement. Also, less does not have to read the entire file before starting, so with large files, it starts faster than text editors like vi.
    
    OPTIONS
       -N, --LINE-NUMBERS
              Display line numbers.
    
       -G, --HIGHLIGHT-NUMBER
              Disable highlighting of search matches.
    
       -i, --ignore-case
              Ignore case in all searches.
    
       -m, --long-prompt
              Show a more detailed prompt.
    
       -p pattern
              Start at the first occurrence of pattern.
    
       -q, --quiet or --silent
              Suppress terminal bell sounds.
    
       -R, --RAW-CONTROL-CHARS
              Display raw control characters.
    
       -S, --chop-long-lines
              Do not wrap long lines.
    
       -X, --no-init
              Do not clear the screen when exiting.
    
       -? or --help
              Display help and exit.
    
    COMMANDS
       Within less, the following commands are useful:
          Space        Scroll forward one page.
          b            Scroll backward one page.
          Enter        Scroll forward one line.
          /pattern     Search for a pattern.
          n            Repeat the previous search.
          q            Quit.           
                """);
    }
    else if(com.equals("mkdir")){
        System.out.println("""
     NAME
       mkdir - make directories
    
    SYNOPSIS
       mkdir [OPTION]... DIRECTORY...
    
    DESCRIPTION
       Create the DIRECTORY(ies), if they do not already exist.
    
       The options below modify the behavior of `mkdir`.
    
    OPTIONS   
       -p, --parents
              No error if existing, make parent directories as needed.
    
       -v, --verbose
              Print a message for each created directory.
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.
           
                """);
    }else if(com.equals("rm")){
        System.out.println("""
    NAME
       rm - remove files or directories
    
    SYNOPSIS
       rm [OPTION]... FILE...
    
    DESCRIPTION
       Remove (unlink) the FILE(s).
    
       Options:  
       -i, --interactive
              Prompt before every removal.
     
       -r, -R, --recursive
              Remove directories and their contents recursively.
    
       -d, --dir
              Remove empty directories.
    
       --help
              Display this help and exit.
    
       --version
              Output version information and exit.                
                """);
    }else if(com.equals("echo")){
        System.out.println("""
    NAME
       echo - display a line of text
    
    SYNOPSIS
       echo [OPTION]... [STRING]...
    
    DESCRIPTION
       Echo the STRING(s) to standard output.
    
    OPTIONS
       -n     Do not output the trailing newline.
    
       -e     Enable interpretation of backslash escapes.
    
       -E     Disable interpretation of backslash escapes (default).
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.
    
    BACKSLASH ESCAPES
       The following sequences are recognized only if the -e option is used:
           \\a     alert (bell)
           \\b     backspace
           \\c     suppress trailing newline
           \\f     form feed
           \\n     new line
           \\r     carriage return
           \\t     horizontal tab
           \\v     vertical tab               
                """);
    }else if(com.equals("man")){
        System.out.println("""
    NAME
       man - an interface to the system reference manuals
    
    SYNOPSIS
       man [OPTION]... [SECTION] PAGE...
    
    DESCRIPTION
       Man is the system's manual pager.  Each page contains documentation about a command,
       utility, or function.
    
    OPTIONS    
       -f, --whatis
              Display a short description of the specified command.

       --help
              Display this help and exit.
    
       --version
              Output version information and exit.
               
                """);
    }else if(com.equals("rmdir")){
        System.out.println("""
    NAME
       rmdir - remove empty directories
    
    SYNOPSIS
       rmdir [OPTION]... DIRECTORY...
    
    DESCRIPTION
       Remove the DIRECTORY(ies), if they are empty.
    
    OPTIONS
       -p, --parents
              Remove DIRECTORY and its ancestors; e.g., 'rmdir -p a/b/c' will remove 'c', 'b', and 'a' if they are all empty.
    
       --help
              Display this help and exit.
    
       --version
              Output version information and exit.               
                """);
    }else if(com.equals("cat")){
        System.out.println("""
    NAME
       cat - concatenate and display files
    
    SYNOPSIS
       cat [OPTION]... [FILE]...
    
    DESCRIPTION
       Concatenate FILE(s) to standard output.
    
    OPTIONS
       -A, --show-all
              Equivalent to -vET.
    
       -b, --number-nonblank
              Number non-empty output lines, overrides -n.
    
       -e     Equivalent to -vE.
    
       -E, --show-ends
              Display $ at the end of each line.
    
       -n, --number
              Number all output lines.
    
       -s, --squeeze-blank
              Suppress repeated empty output lines.
    
       -T, --show-tabs
              Display TAB characters as ^I.
    
       -v, --show-nonprinting
              Use ^ and M- notation to represent non-printing characters.
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.
                
                """);
    }else if(com.equals("ls")){
        System.out.println("""
    NAME
       ls - list directory contents
    
    SYNOPSIS
       ls [OPTION]... [FILE]...
    
    DESCRIPTION
       List information about the FILEs (the current directory by default).
    
    OPTIONS
       -a, --all
              Do not ignore entries starting with .
    
       -l     Use a long listing format.
    
       -h, --human-readable
              With -l, print sizes in human readable format (e.g., 1K 234M 2G).
    
       -R, --recursive
              List subdirectories recursively.
    
       -t     Sort by time modified (most recent first).
    
       -r     Reverse order while sorting.
    
       -S     Sort by file size.
    
       -d, --directory
              List directories themselves, not their contents.
    
       --help Display this help and exit.
    
       --version
              Output version information and exit.                
                """);
    }else if(com.equals("uname")){
        System.out.println("""
    NAME
       uname - print system information
    
    SYNOPSIS
       uname [OPTION]...
    
    DESCRIPTION
       Print certain system information.  The options are as follows:
    
    OPTIONS
       -a, --all
              Print all information.
    
       -s, --kernel-name
              Print the kernel name.
    
       -n, --nodename
              Print the network node hostname.
    
       -r, --kernel-release
              Print the kernel release.
    
       -v, --kernel-version
              Print the kernel version.
    
       -m, --machine
              Print the machine hardware name.
    
       -p, --processor
              Print the processor type (deprecated).
    
       -i, --hardware-platform
              Print the hardware platform (deprecated).
    
       -o, --operating-system
              Print the operating system.
    
       --help
              Display this help and exit.
    
       --version
              Output version information and exit.               
                """);
    }else if(com.equals("cp")){
        System.out.println("""
    NAME
       cp - copy files and directories
    
    SYNOPSIS
       cp [OPTION]... SOURCE... DEST
    
    DESCRIPTION
       Copy SOURCE to DEST, or multiple SOURCE(s) to a DIRECTORY.
    
    OPTIONS
       -a, --archive
              Preserve as much as possible of the original file attributes.
    
       -f, --force
              Remove existing destination files without prompting.
    
       -i, --interactive
              Prompt before overwrite.
    
       -r, -R, --recursive
              Copy directories recursively.
    
       -u, --update
              Copy only when the SOURCE file is newer than the destination file or when the destination file is missing.
    
       -v, --verbose
              Explain what is being done.
    
       -n, --no-clobber
              Do not overwrite an existing file.
    
       --help
              Display this help and exit.
    
       --version
              Output version information and exit.
               
                """);
    }else {
        System.out.println("man: no entry for "+com);
    }    
}

}    

//----------------------------------------------------------------------------------------------------
public void rm(String com) { 

        /* 1- split command to options and paths  */
        ArrayList<String> paths = new ArrayList<>();
        String path = "";
        String option = "";
        int size = 0;
        boolean recursive = false, verboseOption = false,dirOption = false,iOption=false;
        for (int i = 0; i < com.length(); i++) {
            if (i < com.length() - 1 && com.charAt(i) == '-' && com.charAt(i + 1) == '-') {
                while (i < com.length() && com.charAt(i) != ' ') {
                    option += com.charAt(i);
                    i++;
                }
                if (option.equals("--recursive")) {
                    recursive = true;
                }
                else if (option.equals("--verbose")) {
                    verboseOption = true;
                }
                else if (option.equals("--dir")) {
                    dirOption = true;
                }else if (option.equals("--help")) {
                    System.out.println("""
                                    Usage: rm [OPTION]... [FILE]...
                                    Remove (unlink) the FILE(s).
                                    
                                    -i                      prompt before every removal
                                    r, -R, --recursive     remove directories and their contents recursively
                                    -d, --dir               remove empty directories
                                    --help     display this help and exit
                                    --version  output version information and exit""" 
                    );
                    return;
                }else if (option.equals("--version")) {
                    System.out.println("""
                                    rm (GNU coreutils) 8.32
                                    Copyright (C) 2020 Free Software Foundation, Inc.
                                    License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>
                                    This is free software: you are free to change and redistribute it.
                                    There is NO WARRANTY, to the extent permitted by law.

                                    Written by Philopateer Karam.""" 
                    );
                    return;
                }else {
                    System.out.println("rm: unrecognized option\'" + option + "\'");
                    System.out.println("Try \'rm --help\' for more information.");
                    return;
                }
                option = "";
            } else if (i < com.length() - 1 && com.charAt(i) == '-' && Character.isAlphabetic(com.charAt(i + 1))) {
                if (com.charAt(i + 1) == 'r') {
                    recursive = true;
                }
                else if (com.charAt(i + 1) == 'v') {
                    verboseOption = true;
                }
                else if (com.charAt(i + 1) == 'd') {
                    dirOption = true;
                }else if (com.charAt(i + 1) == 'i') {
                    iOption = true;
                }else {
                    System.out.println("rm: unrecognized option\'" + option + "\'");
                    System.out.println("Try \'rm --help\' for more information.");
                    return;
                }
                i++;
            } else if (com.charAt(i) == ' ') {
                if (size != 0) {
                    paths.add(path);
                    path = "";
                    size = 0;
                }
            } else {
                path += com.charAt(i);
                size++;
            }
        }
        if (size != 0) {
            paths.add(path);
        }

 /* 2-chick the paths is correct and remove it*/
    File f;
    String check_path;
    Scanner in = new Scanner(System.in);
    String remove;
            for (int i = 0; i < paths.size(); i++) {
                check_path = makeAbsolutePath(paths.get(i));
                f = new File(check_path);
                if (!f.exists()) {
                    System.out.println("rm: cannot remove \'" + paths.get(i) + "\': No such file or directory");
                }else{
                    if(f.isFile()){
                        if(iOption){
                            System.out.print("rm: remove regular file \\'" + paths.get(i) + "\\'(y/n)?");
                            remove = in.next();
                            if(remove.equals("y")){
                                f.delete();
                                if (verboseOption) {
                                    System.out.println("removed \'" + paths.get(i) + "\' ");
                                }
                            }
                        }
                        else{
                            f.delete();
                            if (verboseOption) {
                                System.out.println("removed \'" + paths.get(i) + "\' ");
                            }
                        }
                    }
                    else{ //is directory
                        String[] list = f.list();
                        if(dirOption == true && recursive == false){
                            if(list == null || list.length == 0){
                                if(iOption){
                                    System.out.println("rm: remove directory \'" + paths.get(i) + "\'?");
                                    remove = in.next();
                                    if(remove.equals("y")){
                                        f.delete();
                                        if (verboseOption) {
                                            System.out.println("removed \'" + paths.get(i) + "\' ");
                                        }
                                    }
                                }else{
                                    f.delete();
                                    if (verboseOption) {
                                        System.out.println("removed \'" + paths.get(i) + "\' ");
                                    }
                                }
                            }else{
                                if(verboseOption){
                                    System.out.println("rm: cannot remove \'" + paths.get(i) + "\': Directory not empty");
                                }
                            }
                        }
                        else if (recursive == true) {
                            if(list == null || list.length == 0){
                                if(iOption){
                                    System.out.println("rm: remove directory \'" + paths.get(i) + "\'?");
                                    remove = in.next();
                                    if(remove.equals("y")){
                                        f.delete();
                                        if (verboseOption) {
                                            System.out.println("removed \'" + paths.get(i) + "\' ");
                                        }
                                    }
                                }
                            }else{
                                removeAllInADir(currentDir+"\\"+paths.get(i),iOption,verboseOption);
                                if(iOption){
                                    System.out.println("rm: remove directory \'" + paths.get(i) + "\'?");
                                    remove = in.next();
                                    if(remove.equals("y")){
                                        f.delete();
                                        if (verboseOption) {
                                            System.out.println("removed \'" + paths.get(i) + "\' ");
                                        }
                                    }
                                }else{
                                    f.delete();
                                    if (verboseOption) {
                                        System.out.println("removed \'" + paths.get(i) + "\' ");
                                    }
                                }
                            }
                        }
                        else{
                            if (verboseOption) {
                                System.out.println("rm: cannot remove \'" + paths.get(i) + "\': Is a directory");
                            } 
                        }
                    }
                }
            }
}

//---------------------------------------------------------------------------------------------------------------
public void touch(String com) { // 20220027
        try {
            if (com.isEmpty()) {
                System.out.println("touch: missing file operand");
            } else if (com.equalsIgnoreCase("--help")) {
                System.out.println("touch FILE..." + "\n");
            } else {
                File file = new File(this.currentDir, com);
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public void mv(String com) { // 20220028
        System.out.println("mv called");
        String[] args = proccess_args(com);
        String targetDirectory = null;

        if (args.length > 0) {
            File potentialDir = new File(this.currentDir, args[args.length - 1]);
            if (potentialDir.isDirectory() && potentialDir.exists()) {
                targetDirectory = potentialDir.getAbsolutePath();
            }
        }

        if (targetDirectory != null) {
            List<File> filesToMove = new ArrayList<>();

            for (int i = 0; i < args.length - 1; i++) {
                File fileToMove = new File(this.currentDir, args[i]);
                if (fileToMove.exists()) {
                    filesToMove.add(fileToMove);
                } else {
                    System.out.println("Error: File " + args[i] + " does not exist.");
                }
            }

            for (File file : filesToMove) {
                File destinationFile = new File(targetDirectory, file.getName());
                if (destinationFile.exists()) {
                    System.out.print("File " + destinationFile.getName() + " exists. Overwrite? (y/n): ");
                    try (Scanner scanner = new Scanner(System.in)) {
                        String response = scanner.nextLine();
                        if (!response.equalsIgnoreCase("y")) {
                            System.out.println("Skipping " + file.getName());
                            continue;
                        }
                    }
                }

                try {
                    Files.move(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved " + file.getName() + " to " + targetDirectory);
                } catch (IOException e) {
                    System.out.println("Error moving " + file.getName() + ": " + e.getMessage());
                }
            }
        } else {
            if (args.length == 2) {
                File fileToRename = new File(this.currentDir, args[0]);
                File newFileName = new File(this.currentDir, args[1]);

                if (fileToRename.exists()) {
                    if (newFileName.exists()) {
                        System.out.print("File " + newFileName.getName() + " exists. Overwrite? (y/n): ");
                        try (Scanner scanner = new Scanner(System.in)) {
                            String response = scanner.nextLine();
                            if (!response.equalsIgnoreCase("y")) {
                                System.out.println("Skipping rename of " + fileToRename.getName());
                                return;
                            }
                        }
                    }

                    try {
                        Files.move(fileToRename.toPath(), newFileName.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Renamed " + fileToRename.getName() + " to " + newFileName.getName());
                    } catch (IOException e) {
                        System.out.println("Error renaming file: " + e.getMessage());
                    }
                } else {
                    System.out.println("Error: File " + args[0] + " does not exist.");
                }
            } else {
                System.out.println("Usage: mv [source] [destination_directory] or [source] [new_name]");
            }
        }
    }

    public void redirectOutput(String com) {
        try {
            if (com.isEmpty()) {
                System.out.println("missing file operand");
            } else {
                File file = new File(this.currentDir, com);
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    
    public void users() {
        String command = System.getProperty("os.name").toLowerCase().contains("win") ?
                "query user" : "who";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Currently logged in users:");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        // Couldn't think of a way better than this since this is a simulated IDE terminal, not an actual terminal.
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    // --------------------------- # Mahmoud Khaled 20220317 # --------------------------- //

    public void cd(String com) {
        if("--help".equals(com)) {
            System.out.println("""
                cd: cd [DIRECTORY]\r
                Change the shell working directory.\r
                \r
                    Change the current directory to DIRECTORY.  The variable $HOME is\r
                    the default DIRECTORY.  The environment variable CDPATH defines\r
                    the search path for the directory.  A null directory argument\r
                    is the same as `cd $HOME'.\r
                \r
                Options:\r
                    -P    use the physical directory structure instead of the logical\r
                        one, resolving symbolic links\r
                    -L    use the logical directory structure (default)\r
                \r
                For more information, see the Bash manual.\r
                """ //
            );

        } else if ("~".equals(com)) {
            this.currentDir = this.homeDir;

        } else if ("..".equals(com)) {
            File newdir = new File(this.currentDir).getParentFile();
            if (newdir != null) {
                this.currentDir = newdir.getAbsolutePath();
            }

        } else {
            File newdir = new File(this.currentDir, com);

            if(com.charAt(1) == ':') {
                newdir = new File(com);
            }

            if (newdir.isDirectory() && newdir.exists()) {
                this.currentDir = newdir.getAbsolutePath();

            } else {
                System.out.println("Directory " + com + " does not exists in " + this.currentDir);
            }

        }

    }

    public void rmdir(String com) {                          //20220317
        String[] Folders = proccess_args(com);

        HashMap<String, Integer> options = new HashMap<>();

        options.put("--ignore-fail-on-non-empty", 0);
        options.put("-p", 0);
        options.put("-v", 0);
        options.put("--help", 0);
        options.put("--version", 0);

        for (String arg : Folders) {
            options.put(arg, 1);
        }

        if(options.get("--help") == 1) {
            System.out.println("""
                Usage: rmdir [OPTION]... DIRECTORY...\r
                Remove the DIRECTORY(ies), if they are empty.\r
                \r
                        --ignore-fail-on-non-empty\r
                                ignore each failure that is solely because a directory\r
                                is non-empty\r
                    -p, --parents  remove DIRECTORY and its ancestors; e.g., 'rmdir -p a/b/c' is\r
                                similar to 'rmdir a/b/c a/b a'\r
                    -v, --verbose  output a diagnostic for every directory processed\r
                        --help     display this help and exit\r
                        --version  output version information and exit\r
                \r
                GNU coreutils online help: <https://www.gnu.org/software/coreutils/>\r
                Report rmdir translation bugs to <https://translationproject.org/team/>\r
                Full documentation at: <https://www.gnu.org/software/coreutils/rmdir>\r
                or available locally via: info '(coreutils) rmdir invocation'\r
                """ //
            );
            return;
        }

        if(options.get("--version") == 1) {
            System.out.println("""
                rmdir (GNU coreutils) 2.0\r
                Copyright (C) YEAR Free Software Foundation, Inc.\r
                License GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>.\r
                This is free software: you are free to change and redistribute it.\r
                There is NO WARRANTY, to the extent permitted by law.\r
                \r
                Written by Mahmoud Khaled.\r
                """ //
            );
            return;
        }


        for (String Folder : Folders) {
            if(Folder.charAt(0) == '-') {
                if (!Folder.equals("--ignore-fail-on-non-empty") && 
                !Folder.equals("-p") && 
                !Folder.equals("-v") && 
                !Folder.equals("--help") && 
                !Folder.equals("--version")) { 
                    System.out.println("Error: '" + Folder + "' is not a recognized option");
                }
                continue;
            }

            File folderToDelete = new File(this.currentDir, Folder);

            if(Folder.charAt(1) == ':') {
                folderToDelete = new File(Folder);
            }

            if (!folderToDelete.exists()) {
                System.out.println(folderToDelete.getPath());
                System.out.println("Error: Folder does not exists.");
            } else if (!folderToDelete.isDirectory()) {
                System.out.println("Error: Please provide a folder.");
            } else if (folderToDelete.listFiles().length != 0) {
                if(options.get("--ignore-fail-on-non-empty") == 0) {
                    System.out.println("Error: Please provide an empty folder.");
                }
            } else {
                if (folderToDelete.delete()) {
                    File FolderParent = new File(folderToDelete.getParent());
                    if(options.get("-v") == 1) {
                        System.out.println("Deleted Folder '" + folderToDelete.getName() + "' successfully");
                    }
                    if (options.get("-p") == 1 && FolderParent.listFiles().length == 0) {
                        FolderParent.delete(); 
                        if(options.get("-v") == 1) {
                            System.out.println("Deleted Folder '" + FolderParent.getName() + "' successfully");
                        }   
                    }
                } else {
                    System.out.println("Error: Failed to delete folder");
                }
            }

            
        }

    }

    public void cat(String com, Scanner input) {                           //20220317
        String[] MyArgs = proccess_args(com);

        HashMap<String, Integer> options = new HashMap<>();

        options.put(">", 0);
        options.put(">>", 0);
        options.put("-n", 0);
        options.put("--help", 0);
        options.put("--version", 0);

        for (String arg : MyArgs) {
            options.put(arg, 1);
        }

        for (String file : MyArgs) {

            if(file.equals(">") || file.equals(">>") || file.equals("-n")) {
                continue;
            }
            int lineNum = 0;
            
            File FileToPrint = new File(this.currentDir, file);
            
            if (file.charAt(1) == ':') {
                FileToPrint = new File(file);
            }

            if (options.get("--help") == 1) {
                System.out.println("""
                    Usage: cat [OPTION]... [FILE]...\r
                    Concatenate FILE(s) to standard output.\r
                    \r
                        -A, --show-all           equivalent to -vET\r
                        -b, --number-nonblank    number nonempty output lines, overrides -n\r
                        -e                       equivalent to -vE\r
                        -E, --show-ends          display $ at end of each line\r
                        -n, --number             number all output lines\r
                        -s, --squeeze-blank      suppress repeated empty output lines\r
                        -T, --show-tabs          display TAB characters as ^I\r
                        -v, --show-nonprinting   use ^ and M- notation, except for LFD and TAB\r
                            --help               display this help and exit\r
                            --version            output version information and exit\r
                    \r
                    Examples:\r
                        cat f - g  Output f's contents, then standard input, then g's contents.\r
                        cat        Copy standard input to standard output.\r
                    \r
                    GNU coreutils online help: <https://www.gnu.org/software/coreutils/>\r
                    Full documentation at: <https://www.gnu.org/software/coreutils/cat>\r
                    or available locally via: info '(coreutils) cat invocation'\r
                    """ //
                );
                return;
            }

            if (options.get("--version") == 1) {
                System.out.println("""
                    cat (GNU coreutils) 2.0\r
                    Copyright (C) YEAR Free Software Foundation, Inc.\r
                    License GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>.\r
                    This is free software: you are free to change and redistribute it.\r
                    There is NO WARRANTY, to the extent permitted by law.\r
                    \r
                    Written by Mahmoud Khaled.\r
                    """ //
                );
                return;
            }

            if (options.get(">") == 1) {
                String inputText = input.nextLine();
                try {
                    FileWriter writer = new FileWriter(FileToPrint);
                    writer.write(inputText);
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("Error: Failed to create file");
                }
                continue;
            }

            if (options.get(">>") == 1) {
                String fileText = "";
                
                try {
                    Scanner fileReader = new Scanner(FileToPrint);
                    while(fileReader.hasNextLine()) {
                        fileText += fileReader.nextLine();
                    }
                    fileText += input.nextLine();
                    fileReader.close();

                    FileWriter writer = new FileWriter(FileToPrint);
                    writer.write(fileText);
                    writer.close();

                } catch (IOException ex) {
                    System.out.println("Error: Failed to create file");
                }
                continue;
            }

            if (!FileToPrint.exists()) {
                System.out.println("Error: File does not exists.");
            } else if (!FileToPrint.isFile()) {
                System.out.println("Error: Please provide a normal folder.");
            } else {
                try {
                    try (Scanner scanner = new Scanner(FileToPrint)) {
                        while (scanner.hasNextLine()) {
                            ++lineNum;
                            System.err.println(lineNum + "- " + scanner.nextLine());
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error: Unable to read from file.");
                }
            }
        }

    }

    public void uname(String com) {                           //20220317
        String[] MyArgs = proccess_args(com);
        
        for (String MyArg : MyArgs) {
            if (MyArg.equals("-s")) {
                System.out.print(System.getProperty("os.name") + " ");
            }
            if (MyArg.equals("-r")) {
                System.out.print(System.getProperty("os.version") + " ");
            }
            if (MyArg.equals("-m")) {
                System.out.print(System.getProperty("os.arch") + " ");
            }
            if (MyArg.equals("-n")) {
                try {
                    System.out.print(java.net.InetAddress.getLocalHost().getHostName() + " ");
                } catch (UnknownHostException ex) {
                    System.out.print("Failed to retrive info");
                }
            }
            if(!MyArg.equals("-s") && !MyArg.equals("-r") && !MyArg.equals("-m") && !MyArg.equals("-n")) {
                System.out.print( MyArg + " is not recognized");
            }
            System.out.println();
        }

    }

    public void cp(String com, Scanner inputChoice) {                        //20220317
        
        String[] parameters = proccess_args(com);

        HashMap<String, Integer> options = new HashMap<>();

        options.put("-f", 0);
        options.put("--force", 0);
        options.put("-i", 0);
        options.put("--iteractive", 0);
        options.put("-n", 0);
        options.put("--no-clobber", 0);

        options.put("-v", 0);
        options.put("--verbose", 0);

        options.put("--help", 0);
        options.put("--version", 0);

        String nextCommArgs = "";

        for (String arg : parameters) {
            options.put(arg, 1);
            if (arg.charAt(0) == '-') {
                nextCommArgs += arg + " ";
            }
        }

        if (options.get("--help") == 1) {
            System.out.println("""
                Usage: cp [OPTION]... [-T] SOURCE DEST\r
                    or:  cp [OPTION]... SOURCE... DIRECTORY\r
                    or:  cp [OPTION]... -t DIRECTORY SOURCE...\r
                Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.\r
                \r
                                         same as --no-dereference --preserve=links\r
                    -f, --force                  if an existing destination file cannot be\r
                                                opened, remove it and try again\r
                    -i, --interactive            prompt before overwrite\r
            
                    -n, --no-clobber             do not overwrite an existing file\r
                    
                    -v, --verbose                explain what is being done\r
                        --help                   display this help and exit\r
                        --version                output version information and exit\r
                \r
                By default, sparse SOURCE files are detected by a crude heuristic and the\r
                corresponding DEST file is made sparse as well.  That is the behavior\r
                selected by --sparse=auto.  Specify --sparse=always to create a sparse DEST\r
                file whenever the SOURCE file contains a long enough sequence of zero bytes.\r
                Use --sparse=never to inhibit creation of sparse files.\r
                \r
                The backup suffix is '~', unless set with --suffix or SIMPLE_BACKUP_SUFFIX.\r
                The version control method may be selected via the --backup option or through\r
                the VERSION_CONTROL environment variable.  Here are the values:\r
                \r
                    none, off       never make backups (even if --backup is given)\r
                    numbered, t     make numbered backups\r
                    existing, nil   numbered if numbered backups exist, simple otherwise\r
                    simple, never   always make simple backups\r
                \r
                GNU coreutils online help: <https://www.gnu.org/software/coreutils/>\r
                Full documentation at: <https://www.gnu.org/software/coreutils/cp>\r
                or available locally via: info '(coreutils) cp invocation'\r
                """
            );
            return;
        }

        if (options.get("--version") == 1) {
            System.out.println("""
                cp (GNU coreutils) 3.0\r
                Copyright (C) YEAR Free Software Foundation, Inc.\r
                License GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>.\r
                This is free software: you are free to change and redistribute it.\r
                There is NO WARRANTY, to the extent permitted by law.\r
                \r
                Written by Mahmoud Khaled.\r
                """
            );
            return;
        }
        

        for (String parameter : parameters) {

            if(parameter.equals("-f") || parameter.equals("--force") || parameter.equals("-i") ||
                parameter.equals("--iteractive") ||parameter.equals("-n") ||parameter.equals("--no-clobber") || 
                parameter.equals("-v") ||parameter.equals("--verbose") ||parameter.equals("--help") ||
                parameter.equals("--version") || parameter.equals(parameters[parameters.length-1])) {
                continue;
            }
            
            File OgfileToCopy = new File(this.currentDir, parameter);
            File fileToCopy = new File(this.currentDir, parameters[parameters.length-1]);
            
            int destType = 1; // 0 --> file 1 --> folder
            int srcType = 1; // 0 --> file 1 --> folder
            int ogpathType = 0; // 0 --> relative 1 --> absolute
            int pathType = 0; // 0 --> relative 1 --> absolute

            for (int i = 0; i < parameter.length(); i++) {
                if (parameter.charAt(i) == '.') {
                    srcType = 0;
                }
                if (parameter.charAt(i) == ':') {
                    ogpathType = 1;
                }
            }

            for (int i = 0; i < parameters[parameters.length-1].length(); i++) {
                if (parameters[parameters.length-1].charAt(i) == '.') {
                    destType = 0;
                }
                if (parameters[parameters.length-1].charAt(i) == ':') {
                    pathType = 1;
                }
            }

            if (ogpathType == 1) {
                OgfileToCopy = new File(parameter);
            }
            if (pathType == 1) {
                fileToCopy = new File(parameters[parameters.length-1]);
            }

            if (srcType == 0 && destType == 0) {
                // System.out.println("0 0");

                if (!OgfileToCopy.exists()) {
                    System.out.println("Error: File does not exists.");
                    return;
                }

                if (fileToCopy.exists() && options.get("-f") == 0 && options.get("--force") == 0 && (options.get("-i") == 1 || options.get("--iteractive") == 1)) {
                    System.out.print("File '" + fileToCopy.getName() + "' this name already exists. Overide? [y/n] ");
                    String choice = inputChoice.next();
                    if (choice.equals("n") || choice.equals("N")) {
                        // System.out.println("cp cancelled");
                        return;
                    }
                }

                if(fileToCopy.exists() && (options.get("-n") == 1 || options.get("--no-clobber") == 1)) {
                    if ((options.get("-v") == 1 || options.get("--verbose") == 1)) {
                        System.out.println("skipped '" + fileToCopy.getName() + "'");
                    }
                    return;
                }

                try {
                    fileToCopy.createNewFile();
                    
                    FileWriter outputFile;
                    try (Scanner inputFile = new Scanner(OgfileToCopy)) {
                        outputFile = new FileWriter(fileToCopy);

                        String line;
                        while (inputFile.hasNextLine()) {
                            line = inputFile.nextLine();
                            outputFile.write(line + "\n");
                        }
                    }
                    outputFile.close();

                    if(options.get("-v") == 1 || options.get("--verbose") == 1) {
                        System.out.println("Copyied '" + OgfileToCopy.getPath() + "' to '" + fileToCopy.getPath() + "' Successfully");
                    }

                } catch (IOException ex) {
                    System.out.println("Error: Failed to copy file.");
                }

            } else if (srcType == 0 && destType == 1) {
                fileToCopy = new File(this.currentDir, parameters[parameters.length-1] + "/" + OgfileToCopy.getName());

                for (int i = 0; i < parameters[parameters.length-1].length(); i++) {
                    if (parameters[parameters.length-1].charAt(i) == ':') {
                        pathType = 1;
                    }
                }
                if (pathType == 1) {
                    fileToCopy = new File(parameters[parameters.length-1] + "/" + OgfileToCopy.getName());
                }

                if (!OgfileToCopy.exists()) {
                    System.out.println("Error: File does not exists.");
                    return;
                }

                if (fileToCopy.exists() && options.get("-f") == 0 && options.get("--force") == 0 && (options.get("-i") == 1 || options.get("--iteractive") == 1)) {
                    System.out.print("File '" + fileToCopy.getName() + "' this name already exists. Overide? [y/n] ");
                    String choice = inputChoice.next();
                    if (choice.equals("n") || choice.equals("N")) {
                        // System.out.println("cp cancelled");
                        return;
                    }
                }

                if(fileToCopy.exists() && (options.get("-n") == 1 || options.get("--no-clobber") == 1)) {
                    if ((options.get("-v") == 1 || options.get("--verbose") == 1)) {
                        System.out.println("skipped '" + fileToCopy.getName() + "'");
                    }
                    return;
                }

                try {
                    fileToCopy.createNewFile();
                    
                    FileWriter outputFile;
                    try (Scanner inputFile = new Scanner(OgfileToCopy)) {
                        outputFile = new FileWriter(fileToCopy);

                        String line;
                        while (inputFile.hasNextLine()) {
                            line = inputFile.nextLine();
                            outputFile.write(line + "\n");
                        }
                    }
                    outputFile.close();

                    if(options.get("-v") == 1 || options.get("--verbose") == 1) {
                        System.out.println("Copyied '" + OgfileToCopy.getPath() + "' to '" + fileToCopy.getPath() + "' Successfully");
                    }
                } catch (IOException ex) {
                    System.out.println("Error: Failed to copy file.");
                }
            } else if (srcType == 1 && destType == 1) {

                // System.out.println("1 1");
                if (!OgfileToCopy.exists()) {
                    System.out.println("Error: File does not exists.");
                    return;
                }

                if (fileToCopy.exists() && options.get("-f") == 0 && options.get("--force") == 0 && (options.get("-i") == 1 || options.get("--iteractive") == 1)) {
                    System.out.print("File '" + fileToCopy.getName() + "' this name already exists. Overide? [y/n] ");
                    String choice = inputChoice.next();
                    if (choice.equals("n") || choice.equals("N")) {
                        // System.out.println("cp cancelled");
                        return;
                    }
                }

                // if(fileToCopy.exists() && (options.get("-n") == 1 || options.get("--no-clobber") == 1)) {
                //     if ((options.get("-v") == 1 || options.get("--verbose") == 1)) {
                //         System.out.println("skipped '" + fileToCopy.getName() + "'");
                //     }
                //     return;
                // }

                fileToCopy.mkdir();
                if(options.get("-v") == 1 || options.get("--verbose") == 1) {
                    System.out.println("Created Directory '" + fileToCopy.getName() + "' inside '" + fileToCopy.getParent() + "' Successfully");
                }

                for (File file : OgfileToCopy.listFiles()) {

                    String newComm = nextCommArgs + file.getPath() + " " + parameters[parameters.length-1] + "/" + file.getName();
                    // System.out.println(newComm);
                    cp(newComm, inputChoice);

                }
                
            }
        }

    }

    // public void inputOp(String com) {                           //20220317
    //     System.out.println("inputOp called");
    //     System.out.println("args in comm: " + com);
    
    //     String[] MyArgs = proccess_args(com);
    
    //     for (int i = 1; i < MyArgs.length; i++) {
    //         System.out.println(MyArgs[i]);
    //     }
    // }
}

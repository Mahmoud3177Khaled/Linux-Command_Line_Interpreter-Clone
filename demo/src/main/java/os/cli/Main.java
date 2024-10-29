package os.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
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
                    case "<" ->
                        cli.inputOp(arg);
                    default ->
                        cli.UndefinedInput(command);
                }
            }
        }
    }
}

class CLI {

    private String currentDir;

    public CLI(String currentDir) {
        this.currentDir = currentDir;
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
        // System.out.println("pwd called");
        // System.out.println("args in comm: " + com);

        // String[] MyArgs = proccess_args(com);
        // for (int i = 1; i < MyArgs.length; i++) {
        //     System.out.println(MyArgs[i]);
        // }
        System.out.println(this.currentDir);
    }

    public void ls(String com) { //20220028
        System.out.println("ls called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for (int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
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
    //-------------------------------------------------------------------------------------

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
                                        Written by David MacKenzie.\r
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

//-----------------------------------------------------------------------------
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

                                    Written by Richard Stallman and David MacKenzie.""" 
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
        System.out.println("touch called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for (int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void mv(String com) { //20220028
        System.out.println("mv called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for (int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    

    // --------------------------- # Mahmoud Khaled 20220317 # --------------------------- //

    public void cd(String com) {

        if ("..".equals(com)) {
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
                System.out.println(System.getProperty("os.name"));
            }
            if (MyArg.equals("-r")) {
                System.out.println(System.getProperty("os.version"));
            }
            if (MyArg.equals("-m")) {
                System.out.println(System.getProperty("os.arch"));
            }
            if (MyArg.equals("-n")) {
                try {
                    System.out.println(java.net.InetAddress.getLocalHost().getHostName());
                } catch (UnknownHostException ex) {
                    System.out.println("Failed to retrive info");
                }
            }
        }

    }

    public void cp(String com, Scanner inputChoice) {                        //20220317
        // System.out.println("cp called");
        // System.out.println("args in comm: " + com);

        // String[] MyArgs = proccess_args(com);
        // for(int i = 1; i < MyArgs.length; i++) {
        //     System.out.println(MyArgs[i]);
        // }
        String[] parameters = proccess_args(com);

        File OgfileToCopy = new File(this.currentDir, parameters[0]);
        File fileToCopy = new File(this.currentDir, parameters[1]);

        int destType = 1; // 0 --> file 1 --> folder
        int srcType = 1; // 0 --> file 1 --> folder
        int ogpathType = 0; // 0 --> relative 1 --> absolute
        int pathType = 0; // 0 --> relative 1 --> absolute

        for (int i = 0; i < parameters[0].length(); i++) {
            if (parameters[0].charAt(i) == '.') {
                srcType = 0;
            }
            if (parameters[0].charAt(i) == ':') {
                ogpathType = 1;
            }
        }

        for (int i = 0; i < parameters[1].length(); i++) {
            if (parameters[1].charAt(i) == '.') {
                destType = 0;
            }
            if (parameters[1].charAt(i) == ':') {
                pathType = 1;
            }
        }

        if (ogpathType == 1) {
            OgfileToCopy = new File(parameters[0]);
        }
        if (pathType == 1) {
            fileToCopy = new File(parameters[1]);
        }

        if (srcType == 0 && destType == 0) {
            // System.out.println("0 0");

            if (!OgfileToCopy.exists()) {
                System.out.println("Error: File does not exists.");
                return;
            }

            if (fileToCopy.exists()) {
                System.out.print("File with this name already exists. Overide? [y/n] ");
                String choice = inputChoice.next();
                if (choice.equals("n") || choice.equals("N")) {
                    System.out.println("cp cancelled");
                    return;
                }
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
            } catch (IOException ex) {
                System.out.println("Error: Failed to copy file.");
            }

        } else if (srcType == 1 && destType == 1) {

            // System.out.println("1 1");
            if (!OgfileToCopy.exists()) {
                System.out.println("Error: File does not exists.");
                return;
            }

            if (fileToCopy.exists()) {
                System.out.print("File with this name already exists. Overide? [y/n] ");
                String choice = inputChoice.next();
                if (choice.equals("n") || choice.equals("N")) {
                    System.out.println("cp cancelled");
                    return;
                }
            }

            fileToCopy.mkdir();

            for (File file : OgfileToCopy.listFiles()) {

                String newComm = file.getPath() + " " + parameters[1] + "/" + file.getName();
                System.out.println(newComm);
                cp(newComm, inputChoice);

            }

        }

    }

    public void inputOp(String com) {                           //20220317
        System.out.println("inputOp called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for (int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

}

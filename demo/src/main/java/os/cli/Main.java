package os.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
                        cli.cat(arg);
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
//-------------------------------------------------------------------------------------

    private void createParentDirectory(String path) {
        File f, pf;
        if (!path.contains("\\") && !path.contains("/")) {
            f = new File(currentDir + path);
            f.mkdir();
        } else {
            if (path.charAt(1) != ':') {
                path = currentDir + path;
            }
            f = new File(path);
            String p = f.getParent();
            pf = new File(p);
            if (!pf.exists()) {
                createParentDirectory(p);
            }
            f.mkdir();
        }

    }

    public void mkdir(String com) {// 20220246

        /* 1- split command to options and paths  */
        ArrayList<String> inputOptions = new ArrayList<>();
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
                }
                if (option.equals("--verbose")) {
                    verboseOption = true;
                }
                inputOptions.add(option);
                option = "";
            } else if (i < com.length() - 1 && com.charAt(i) == '-' && Character.isAlphabetic(com.charAt(i + 1))) {
                inputOptions.add("-" + com.charAt(i + 1));
                if (com.charAt(i + 1) == 'p') {
                    parentOption = true;
                }
                if (com.charAt(i + 1) == 'v') {
                    verboseOption = true;
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
        // for (String elem : inputOptions) {
        //     System.out.println(elem);   
        // }
        // for (String elem : paths) {
        //     System.out.println(elem);   
        // }


        /* 2-chick the options is correct */
        //-set all options
        Map<String, Boolean> COMMAND_OPTIONS = new HashMap<>();
        COMMAND_OPTIONS.put("--help", true);
        COMMAND_OPTIONS.put("--version", true);
        COMMAND_OPTIONS.put("--verbose", true);  //done
        COMMAND_OPTIONS.put("-v", true);  //done
        COMMAND_OPTIONS.put("--mode", true);
        COMMAND_OPTIONS.put("-m", true);
        COMMAND_OPTIONS.put("--parents", true); //done
        COMMAND_OPTIONS.put("-p", true); //done
        COMMAND_OPTIONS.put("--context", true);
        COMMAND_OPTIONS.put("-z", true);

        //check input options
        for (int i = 0; i < inputOptions.size(); i++) {
            if (!COMMAND_OPTIONS.containsKey(inputOptions.get(i))) {
                System.out.println("mkdir: unrecognized option\'" + inputOptions.get(i) + "\'");
                System.out.println("Try \'mkdir --help\' for more information.");
                return;
            }
        }

        /* 3-execute main options */
        if (inputOptions.contains("--help")) {

        }

        /* 4-chick the paths is correct */
        File f, pf;
        String check_path = "";
        if (!parentOption) {
            for (int i = 0; i < paths.size(); i++) {
                check_path = paths.get(i);
                if (check_path.contains("\\") || check_path.contains("/")) {
                    if (check_path.charAt(1) != ':') {
                        check_path = currentDir + check_path;
                    }
                    f = new File(check_path);
                    String p = f.getParent();
                    pf = new File(p);
                    if (!pf.exists()) {
                        System.out.println("mkdir: cannot create directory \'" + paths.get(i) + "\': No such file or directory");
                        return;
                    }
                }
            }
        }

        /*5- create directory */
        if (parentOption) {
            for (int i = 0; i < paths.size(); i++) {
                createParentDirectory(paths.get(i));
                if (verboseOption) {
                    System.out.println("mkdir: created directory \'"+paths.get(i) + "\' ");
                }
            }
        } else {
            for (int i = 0; i < paths.size(); i++) {
                check_path = paths.get(i);
                if (check_path.contains("\\") || check_path.contains("/")) {
                    if (check_path.charAt(1) != ':') {
                        check_path = currentDir + check_path;
                    }
                    f = new File(check_path);
                    f.mkdir();
                    if (verboseOption) {
                        System.out.println("mkdir: created directory \'"+paths.get(i) + "\' ");
                    }
                } else {
                    f = new File(currentDir + paths.get(i));
                    f.mkdir();
                    if (verboseOption) {
                        System.out.println("mkdir: created directory \'"+paths.get(i) + "\' ");
                    }
                }
            }
        }
    }

//-----------------------------------------------------------------------------
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

    public void rm(String com) { //20220246
        System.out.println("rm called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for (int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    // --------------------------- # Mahmoud Khaled 20220317 # --------------------------- //
    public void cd(String com) {
        // System.out.println("cd called");
        // System.out.println("args in comm: " + com);

        // String[] MyArgs = proccess_args(com);
        // for(int i = 1; i < MyArgs.length; i++) {
        //     System.out.println(MyArgs[i]);
        // }
        if ("..".equals(com)) {
            File newdir = new File(this.currentDir).getParentFile();
            if (newdir != null) {
                this.currentDir = newdir.getAbsolutePath();
            }
        } else {
            File newdir = new File(this.currentDir, com);
            if (newdir.isDirectory() && newdir.exists()) {
                this.currentDir = newdir.getAbsolutePath();

            } else {
                System.out.println("Directory " + com + " does not exists in " + this.currentDir);
                // System.out.println(newdir.exists());
                // System.out.println(newdir.isDirectory());
            }

        }

    }

    public void rmdir(String com) {                          //20220317

        // System.out.println("rmdir called");
        // System.out.println("args in comm: " + com);
        // String[] MyArgs = proccess_args(com);
        // for(int i = 1; i < MyArgs.length; i++) {
            //     System.out.println(MyArgs[i]);
            // }

        String[] MyArgs = proccess_args(com);

        for (int i = 0; i < MyArgs.length; i++) {
            File folderToDelete = new File(this.currentDir, MyArgs[i]);
    
            if (!folderToDelete.exists()) {
                System.out.println("Error: Folder does not exists.");
            } else if (!folderToDelete.isDirectory()) {
                System.out.println("Error: Please provide a folder.");
            } else if (folderToDelete.listFiles().length != 0) {
                System.out.println("Error: Please provide an empty folder.");
            } else {
                if (folderToDelete.delete()) {
                    // System.out.println("Folder deleted.");
                } else {
                    System.out.println("Error: Failed to delete folder");
                }
            }
            
        }


    }

    public void cat(String com) {                           //20220317
        // System.out.println("cat called");
        // System.out.println("args in comm: " + com);

        // String[] MyArgs = proccess_args(com);
        // for(int i = 1; i < MyArgs.length; i++) {
        //     System.out.println(MyArgs[i]);
        // }
        File FileToPrint = new File(this.currentDir, com);

        if (!FileToPrint.exists()) {
            System.out.println("Error: File does not exists.");
        } else if (!FileToPrint.isFile()) {
            System.out.println("Error: Please provide a normal folder.");
        } else {
            try {
                try (Scanner scanner = new Scanner(FileToPrint)) {
                    while (scanner.hasNextLine()) {
                        System.err.println(scanner.nextLine());
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: Unable to read from file.");
            }
        }

    }

    public void uname(String com) {                           //20220317
        // System.out.println("uname called");
        // System.out.println("args in comm: " + com);

        // String[] MyArgs = proccess_args(com);
        // for(int i = 1; i < MyArgs.length; i++) {
        //     System.out.println(MyArgs[i]);
        // }
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

    public void cp(String com, Scanner inputChoice) { //20220317
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
        @SuppressWarnings("unused")
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

        if (srcType == 0 && destType == 0) {

            // System.out.println(!OgfileToCopy.exists());

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

            File FileToCopyFar = new File(this.currentDir + parameters[1], parameters[0]);
            if (pathType == 1) {
                FileToCopyFar = new File(parameters[1], parameters[0]);
            }

            if (!OgfileToCopy.exists()) {
                System.out.println("Error: File does not exists.");
                return;
            }

            if (FileToCopyFar.exists()) {
                System.out.print("File with this name already exists. Overide? [y/n] ");
                String choice = inputChoice.next();
                if (choice.equals("n") || choice.equals("N")) {
                    System.out.println("cp cancelled");
                    return;
                }
            }

            try {
                FileToCopyFar.createNewFile();

                FileWriter outputFile;
                try (Scanner inputFile = new Scanner(OgfileToCopy)) {
                    outputFile = new FileWriter(FileToCopyFar);

                    String line;
                    while (inputFile.hasNextLine()) {
                        line = inputFile.nextLine();
                        outputFile.write(line + "\n");
                    }
                }
                outputFile.close();

            } catch (IOException ex) {
                System.out.println(ex);
            }

        } else if (srcType == 1 && destType == 1) {
            // implement copying folders
            // case 1: empty folder: copy from src to dest
            // case 2: a file: comp the file from src to dest
            // case 3: a non empty folder: call same func with com: ""


            
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

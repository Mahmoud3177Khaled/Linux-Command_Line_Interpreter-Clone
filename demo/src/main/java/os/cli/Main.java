package os.cli;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            CLI cli = new CLI("D:// >");
            boolean run = true;
            String arg;
            String command;
            while (run) {
                System.out.print(cli.currentDir);
                command = input.next();
                arg = input.nextLine();
                switch (command) {
                    case "pwd" -> cli.pwd(arg);
                    case "cd" -> cli.cd(arg);
                    case "mkdir" -> cli.mkdir(arg);
                    case "touch" -> cli.touch(arg);
                    case "mv" -> cli.mv(arg);
                    case "rm" -> cli.rm(arg);
                    case "rmdir" -> cli.rmdir(arg);
                    case "cat" -> cli.cat(arg);
                    case "ls" -> cli.ls(arg);
                    case "uname" -> cli.uname(arg);
                    case "cp" -> cli.cp(arg);
                    case "<" -> cli.inputOp(arg);
                    default ->  cli.UndefinedInput(arg);
                }
            }
        }
    }
}

class CLI {

    public String currentDir;

    public CLI(String currentDir) {
        this.currentDir = currentDir;
    }

    public void UndefinedInput(String com) {
        System.out.print(com + " is not a recognized command,");
        System.out.println(" please make sure you typed a ligal command or try again.");
    }

    private String[] proccess_args(String com) {
        String[] proccessed_args = com.split(" ");
        return proccessed_args;
    }

    public void pwd(String com) {
        System.out.println("pwd called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void cd(String com) {
        System.out.println("cd called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void mkdir(String com) {
        System.out.println("mkdir called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void touch(String com) {
        System.out.println("touch called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void mv(String com) {
        System.out.println("mv called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void rm(String com) {
        System.out.println("rm called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    // --------------------------- # Mahmoud Khaled 20220317 # --------------------------- //

    public void rmdir(String com) {
        System.out.println("rmdir called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void cat(String com) {
        System.out.println("cat called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void ls(String com) {
        System.out.println("ls called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void uname(String com) {
        System.out.println("uname called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void cp(String com) {
        System.out.println("cp called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }

    public void inputOp(String com) {
        System.out.println("inputOp called");
        System.out.println("args in comm: " + com);

        String[] MyArgs = proccess_args(com);

        for(int i = 1; i < MyArgs.length; i++) {
            System.out.println(MyArgs[i]);
        }
    }


}

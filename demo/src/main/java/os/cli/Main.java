package os.cli;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CLI cli = new CLI("D:// >");
        boolean run = true;
        String command;
        while (run) {
            System.out.print(cli.currentDir);
            command = input.next();
            switch (command) {
                case "pwd" -> cli.pwd();
                case "cd" -> cli.cd();
                case "mkdir" -> cli.mkdir();
                case "touch" -> cli.touch();
                case "mv" -> cli.mv();
                case "rm" -> cli.rm();
                case "rmdir" -> cli.rmdir();
                case "cat" -> cli.cat();
                case "ls" -> cli.ls();
                case "uname" -> cli.uname();
                case "cp" -> cli.cp();
                case "<" -> cli.inputOp();
                default -> throw new AssertionError();
            }
        }
        input.close();
    }
}

class CLI {

    public String currentDir;

    public CLI(String currentDir) {
        this.currentDir = currentDir;
    }

    public void pwd() {
        System.out.println("pwd called");
    }

    public void cd() {
        System.out.println("cd called");
    }

    public void mkdir() {
        System.out.println("mkdir called");
    }

    public void touch() {
        System.out.println("touch called");
    }

    public void mv() {
        System.out.println("mv called");
    }

    public void rm() {
        System.out.println("rm called");
    }

    // --------------------------- # Mahmoud Khaled 20220317 # --------------------------- //

    public void rmdir() {
        System.out.println("rmdir called");
    }

    public void cat() {
        System.out.println("cat called");
    }

    public void ls() {
        System.out.println("ls called");
    }

    public void uname() {
        System.out.println("uname called");
    }

    public void cp() {
        System.out.println("cp called");
    }

    public void inputOp() {
        System.out.println("inputOp called");
    }


}

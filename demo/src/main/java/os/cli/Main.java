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
                case "ls" -> cli.ls();
                case "mkdir" -> cli.mkdir();
                case "touch" -> cli.touch();
                case "mv" -> cli.mv();
                case "rm" -> cli.rm();
                case "cat" -> cli.cat();
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

    }

    public void cd() {

    }

    public void ls() {

    }

    public void mkdir() {

    }

    public void touch() {

    }

    public void mv() {

    }

    public void rm() {

    }

    public void cat() {

    }

}

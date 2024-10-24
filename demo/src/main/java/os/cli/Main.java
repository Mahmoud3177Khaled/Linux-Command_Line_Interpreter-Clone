
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
                case "pwd":
                    cli.pwd();
                    break;
                case "cd":
                    cli.cd();
                    break;
                case "ls":
                    cli.ls();
                    break;
                case "mkdir":
                    cli.mkdir();
                    break;
                case "touch":
                    cli.touch();
                    break;
                case "mv":
                    cli.mv();
                    break;
                case "rm":
                    cli.rm();
                    break;
                case "cat":
                    cli.cat();
                    break;
                default:
                    throw new AssertionError();
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

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            Parser parser = new Parser();
            parser.parse(sc.nextLine());
        }
    }
}

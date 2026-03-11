/**
 * Main is the entry point of the program.
 *
 * Note:
 * Java requires the main method to be static.
 * This is the standard and expected exception even if the project discourages
 * other static methods.
 */
public class Main {
    public static void main(String[] args) {
        VPTApplication app = new VPTApplication();
        System.out.println(app.run());
    }
}
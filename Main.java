import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            String inputFileName = args[0];
            String outputFileName = args[1];
            FeedManager.scanFile(inputFileName, outputFileName);
        }
    }
}
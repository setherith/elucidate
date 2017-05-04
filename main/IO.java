package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IO {
    public static String[] LoadFromFile(String path) {
        StringBuilder content = new StringBuilder();
        File f = new File(path);
        try {
            FileReader fr = new FileReader(f);
            while (fr.ready()) {
                int bit = fr.read();
                content.append((char) bit);
            }
            fr.close();
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
        return content.toString().split("\n");
    }
}

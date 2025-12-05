import java.io.*;
import java.util.*;

public class LibraryDataStore {
    public static void save(Object obj, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.out.println("Error saving " + filename + " : " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String filename, T defaultValue) {
        File f = new File(filename);
        if (!f.exists()) return defaultValue;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading " + filename + " : " + e.getMessage());
            return defaultValue;
        }
    }
}

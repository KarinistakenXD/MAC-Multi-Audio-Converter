import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.UIManager;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

/** Loads every packaged application class without opening the GUI or installing FFmpeg. */
public class RuntimeSmokeTest {
    public static void main(String[] args) throws Exception {
        try (JarFile jar = new JarFile(new File(args[0]))) {
            Enumeration<JarEntry> entries = jar.entries();
            int count = 0;
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".class")) {
                    Class.forName(name.substring(0, name.length() - 6).replace('/', '.'),
                            false, RuntimeSmokeTest.class.getClassLoader());
                    count++;
                }
            }
            if (count == 0) throw new AssertionError("No application classes packaged");
        }
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        System.out.println("Application classes and FlatLaf loaded on Java "
                + System.getProperty("java.version"));
    }
}

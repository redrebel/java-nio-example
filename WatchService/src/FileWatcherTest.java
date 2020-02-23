import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FileWatcherTest {
    @Test
    public void test() throws IOException, InterruptedException {
        File folder = new File("src/test/resources");
        final Map<String, String> map = new HashMap<>();
        FileWatcher watcher = new FileWatcher(folder);
        watcher.addListener(new FileAdapter() {
            public void onCreated(FileEvent event) {
                map.put("file.created", event.getFile().getName());
            }
            public void onModified(FileEvent event) {
                map.put("file.modified", event.getFile().getName());
            }
            public void onDeleted(FileEvent event) {
                map.put("file.deleted", event.getFile().getName());
            }
        }).watch();
        assertEquals(1, watcher.getListeners().size());
        File file = new File(folder + "/test.txt");
        wait(5000);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("Some String");
        }
        wait(5000);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("Some String");
        }
        wait(5000);
        file.delete();
        wait(5000);
        assertEquals(file.getName(), map.get("file.created"));
        assertEquals(file.getName(), map.get("file.modified"));
        assertEquals(file.getName(), map.get("file.deleted"));
    }

    public void wait(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}

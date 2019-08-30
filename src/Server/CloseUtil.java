package Server;
import java.io.Closeable;

public class CloseUtil {
    public static void closeAll(Closeable ...io) {
        for (Closeable closeable : io) {
            try {
                if(closeable!=null)
                    closeable.close();
            }catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}

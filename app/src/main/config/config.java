import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config implements IConfig {

   private String serverip;


    private void loadFile() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("app/config.properties"));
        this.serverip = appProps.getProperty("server.ip");
    }

    @Override
    public String getServerip(){

        return this.serverip;
    }


}
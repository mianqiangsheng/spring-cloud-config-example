package example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// This will allow us to reinitialize this controller to get any new config
// values when the /refresh endpoint is POSTed to.
@RefreshScope
public class AppController {

    @Value("${loaded}")
    private String loaded;

    @Value("${config-client-test.info.foo}")
    private String foo1;

    @Value("${config-client-test-dev.info.foo}")
    private String foo2;

    @Value("${application.info.foo}")
    private String foo3;

    @Value("${application-dev.info.foo}")
    private String foo4;

    @Value("${baz}")
    private String baz;

    @RequestMapping("/getLoaded")
    public String getLoaded() {
        return "Using [" + loaded + "] from config server";
    }

    @RequestMapping("/getFoo")
    public String getFoo() {
        return "Using [" + foo1 + "] from config server \n"
                + "Using [" + foo2 + "] from config server \n"
                + "Using [" + foo3 + "] from config server \n"
                + "Using [" + foo4 + "] from config server";
    }

    @RequestMapping("/getBaz")
    public String getBaz() {
        return "Using [" + baz + "] from config server";
    }
}

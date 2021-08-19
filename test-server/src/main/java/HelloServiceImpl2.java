import com.kiro.rpc.HelloObject;
import com.kiro.rpc.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xufangmin
 * @create 2021-08-19-17:33
 */
public class HelloServiceImpl2 implements HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceImpl2.class);

    public String hello(HelloObject helloObject) {
        LOGGER.info("接收到消息：{}", helloObject.getMessage());
        return "本次处理来自Socket";
    }
}

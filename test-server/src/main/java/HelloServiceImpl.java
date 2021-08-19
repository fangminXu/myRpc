import com.kiro.rpc.HelloObject;
import com.kiro.rpc.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xufangmin
 * @create 2021-08-13-15:05
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    public String hello(HelloObject helloObject) {
        logger.info("服务端hello被调用，接收到消息：" + helloObject.getMessage());
        return "本次处理来自Netty";
    }
}

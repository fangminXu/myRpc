import com.kiro.rpc.api.HelloObject;
import com.kiro.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xufangmin
 * @create 2021-08-11-11:10
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    public String hello(HelloObject object) {
        logger.info("接收到：{}", object.getMassage());
        return "这是掉用的返回值，id=" + object.getId();
    }
}

import com.kiro.rpc.HelloService;
import com.kiro.rpc.netty.server.NettyServer;
import com.kiro.rpc.registry.DefaultServiceRegistry;
import com.kiro.rpc.registry.ServiceRegistry;

/**
 * @author Xufangmin
 * @create 2021-08-16-16:44
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}

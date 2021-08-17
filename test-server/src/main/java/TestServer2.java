import com.kiro.rpc.HelloService;
import com.kiro.rpc.registry.DefaultServiceRegistry;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.KryoSerializer;
import com.kiro.rpc.socket.server.SocketServer;

/**
 * @author Xufangmin
 * @create 2021-08-13-17:26
 */
public class TestServer2 {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        SocketServer server = new SocketServer(registry);
        server.setSerializer(new KryoSerializer());
        server.start(9000);
    }
}

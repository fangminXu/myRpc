import com.kiro.rpc.HelloService;

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
        SocketServer server = new SocketServer("127.0.0.1", 8888);
        server.publishService(helloService, HelloService.class);
        server.setSerializer(new KryoSerializer());
        server.start();
    }
}

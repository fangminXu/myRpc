import com.kiro.rpc.HelloObject;
import com.kiro.rpc.HelloService;
import com.kiro.rpc.RpcClientProxy;
import com.kiro.rpc.serializer.KryoSerializer;
import com.kiro.rpc.socket.client.SocketClient;

/**
 * @author Xufangmin
 * @create 2021-08-13-15:09
 */
public class TestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String result = helloService.hello(new HelloObject(12, "this is a message"));
        System.out.println(result);
    }
}

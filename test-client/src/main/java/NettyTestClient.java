import com.kiro.rpc.HelloObject;
import com.kiro.rpc.HelloService;
import com.kiro.rpc.RpcClient;
import com.kiro.rpc.RpcClientProxy;
import com.kiro.rpc.netty.client.NettyClient;
import com.kiro.rpc.serializer.HessianSerializer;

/**
 * @author Xufangmin
 * @create 2021-08-16-16:48
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        client.setSerializer(new HessianSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService proxy1 = proxy.getProxy(HelloService.class);
        String result = proxy1.hello(new HelloObject(12, "this is message"));
        System.out.println(result);
    }
}

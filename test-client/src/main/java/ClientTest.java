import com.kiro.rpc.api.HelloObject;
import com.kiro.client.RpcClientProxy;
import com.kiro.rpc.api.HelloService;

/**
 * @author Xufangmin
 * @create 2021-08-10-19:22
 */
public class ClientTest {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new HelloObject(12, "this is message"));
        System.out.println(hello);
    }
}

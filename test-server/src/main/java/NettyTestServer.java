import com.kiro.rpc.HelloService;
import com.kiro.rpc.netty.server.NettyServer;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.serializer.ProtobufSerializer;

/**
 * @author Xufangmin
 * @create 2021-08-16-16:44
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.start();
    }
}

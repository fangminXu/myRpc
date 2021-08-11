import com.kiro.server.RpcServer;

/**
 * @author Xufangmin
 * @create 2021-08-11-11:09
 */
public class ServerTest {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.Register(new HelloServiceImpl(), 9000);
    }
}

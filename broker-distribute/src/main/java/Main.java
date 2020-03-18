import com.broker.base.IStorage;
import com.broker.distribute.JavaMainDistribute;
import com.broker.hook.BrokerLinkableHook;
import com.broker.hook.support.ClientAuthCollectorHook;
import com.broker.utils.strorage.StorageFactory;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class Main {

    protected final static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws InterruptedException {
        JavaMainDistribute distribute = new JavaMainDistribute();
        SocketIOServer socketIOServer = distribute.initSocketIOServer("localhost", 9091);
        // broker 插件列表, 可根据需要自行添加插件
        LinkedList<BrokerLinkableHook> hooks = new LinkedList<BrokerLinkableHook>(){{
            add(new ClientAuthCollectorHook("POINT-X"));
        }};
        LinkedList<BrokerLinkableHook> hooksLinkable = distribute.createBrokerLinkableHooks(hooks);
        IStorage storage = StorageFactory.getInstance();
        storage.setKeyValue("users->auid:token", "admin", "admin");
        distribute.configBroker(socketIOServer, hooksLinkable);

        while (true){
            Thread.sleep(1000);
        }
    }

}

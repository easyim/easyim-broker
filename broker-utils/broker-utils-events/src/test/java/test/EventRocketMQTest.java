package test;

import com.broker.base.IBrokerEventBus;
import com.broker.base.event.MQSubscribe;
import com.broker.utils.events.EventFactory;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;
import test.tester_dto.AppBrokerEventMessage;
import test.tester_dto.NextBrokerEventMessage;

import java.util.Date;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 10:20
 * Description: easyimbroker
 **/
public class EventRocketMQTest {

    private IBrokerEventBus event = null;
    @Before
    public void init_event_factory(){
        System.setProperty("broker.event.use", EventFactory.EVENT_TYPE_ROCKETMQ);
        System.setProperty("broker.event.mq.point", "POINT-AA");
        event = EventFactory.getInstance();
        event.register(this);

    }

    @MQSubscribe
    @Subscribe
    public void onReceiveBrokerEvent(AppBrokerEventMessage message){
        System.out.println("onReceiveBrokerEvent:" + message.toString());
        event.send(new NextBrokerEventMessage().setName("next-name" + new Date().getTime()));
    }

    @MQSubscribe
    @Subscribe
    public void onReceiveNextBrokerEvent(NextBrokerEventMessage message){
        System.out.println("onReceiveNextBrokerEvent:" + message.toString());
    }


    @Test
    public void _0_test_rocketmq_ok() throws InterruptedException {
        AppBrokerEventMessage message = new AppBrokerEventMessage();
        message.setName("name" + new Date().getTime());
        event.send(message);
//        while (true){
//            Thread.sleep(1000);
//        }
        Thread.sleep(1000);
        event.destroy();
    }

}

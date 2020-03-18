package test;

import com.broker.base.BrokerEventMessage;
import com.broker.base.IBrokerEventBus;
import com.broker.base.event.MQSubscribe;
import com.broker.utils.events.EventFactory;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import test.tester_dto.AppBrokerEventMessage;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 10:20
 * Description: easyimbroker
 **/
public class EventTest {
    private IBrokerEventBus event = null;
    @Before
    public void init_storage(){
        System.setProperty("broker.event.use", EventFactory.EVENT_TYPE_EVENTBUS);
        event = EventFactory.getInstance();
        event.register(this);

        Arrays.asList(this.getClass().getMethods()).forEach(method -> {
            MQSubscribe subscribe = method.getAnnotation(MQSubscribe.class);
            if(subscribe != null){
                System.out.println(" Subscribe 绑定了方法:" + method.getName());
            }
        });
    }

    @Subscribe
    public void onReceiveBrokerEvent(AppBrokerEventMessage message){
        System.out.println("onReceiveBrokerEvent:" + message.toString());
    }

    @Test
    public void _0_test_eventbus_ok(){
        BrokerEventMessage message = new AppBrokerEventMessage();
        event.send(message);
        System.out.println("had send");
    }

    @Test
    public void _1_test_scan_annotations(){
//        Reflections reflections = new Reflections("test");

        Reflections reflections = new Reflections(
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner(),
                new MethodParameterNamesScanner());

        Set<Method> subMethods = reflections.getMethodsAnnotatedWith(Subscribe.class);
        subMethods.forEach(method->{
            System.out.println(method.getName() + "=methods==" + reflections.getMethodParamNames(method));
            Class<?>[] methodParameterTypes = method.getParameterTypes();
            if(methodParameterTypes.length > 0){
                System.out.println("first method paramType:" + methodParameterTypes[0]);
            }
        });

    }
}

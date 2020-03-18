package test.tester_dto;

import com.broker.base.BrokerEventMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/18 8:43
 * Description: easyimbroker
 **/
@NoArgsConstructor
@Data
public class AppBrokerEventMessage extends BrokerEventMessage {
    private String name = "";
}

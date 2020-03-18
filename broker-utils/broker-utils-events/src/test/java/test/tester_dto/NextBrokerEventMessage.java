package test.tester_dto;

import com.broker.base.BrokerEventMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/18 8:43
 * Description: easyimbroker
 **/
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class NextBrokerEventMessage extends BrokerEventMessage {
    private String name = "";
}

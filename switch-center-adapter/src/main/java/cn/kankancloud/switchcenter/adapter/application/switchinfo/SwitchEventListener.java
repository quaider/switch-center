package cn.kankancloud.switchcenter.adapter.application.switchinfo;

import cn.kankancloud.switchcenter.adapter.driving.cache.SwitchCacheKey;
import com.alibaba.fastjson.JSON;
import cn.kankancloud.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import cn.kankancloud.switchcenter.adapter.application.akka.CacheEvictionMessage;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchEvent;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SwitchEventListener {

    @Async("eventListenerPool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleSwitchAddedEvent(SwitchEvent.Added event) {
        log.info("receive domain event: SwitchEvent.Added, payload={}", JSON.toJSONString(event));

        publishCacheEvictionMessage(event.getSwitchInfo().getSwitchInfoKey());
    }

    @Async("eventListenerPool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleSwitchUpdatedEvent(SwitchEvent.Updated event) {
        log.info("receive domain event: SwitchEvent.Updated, payload={}", JSON.toJSONString(event));

        publishCacheEvictionMessage(event.getSwitchInfo().getSwitchInfoKey());
    }

    @Async("eventListenerPool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleSwitchDeletedEvent(SwitchEvent.Deleted event) {
        log.info("receive  domain event: SwitchEvent.Deleted, payload={}", JSON.toJSONString(event));

        CacheEvictionMessage message = buildMessage(event.getSwitchInfoKey());
        if (!ObjectUtils.isEmpty(event.getChildKeys())) {
            List<SwitchCacheKey> children = event.getChildKeys().stream()
                    .map(key -> new SwitchCacheKey(key.getNamespaceId(), key.getKey()))
                    .collect(Collectors.toList());

            message.setChildren(children);
        }

        publishCacheEvictionMessage(message);
    }

    private CacheEvictionMessage buildMessage(SwitchInfoKey key) {
        return new CacheEvictionMessage(new SwitchCacheKey(key.getNamespaceId(), key.getKey()));
    }

    private void publishCacheEvictionMessage(SwitchInfoKey key) {
        publishCacheEvictionMessage(buildMessage(key));
    }

    private void publishCacheEvictionMessage(CacheEvictionMessage message) {
        // akka distributed pub and sub的模式是 at most once，这意味着消息存在丢失的可能性(节点网络闪断、失联等情况)
        // 如果一定要实现 at least once模式，则需要引入mq，为了简单起见，这里允许一定的消息丢失,
        // 即可能会出现节点间数据不一致的情况，这个不一致的时间也就是缓存时长(目前设定的是30秒)
        AkkaServerEnvironment.getEnv().getPublisher().tell(message, null);
    }

}

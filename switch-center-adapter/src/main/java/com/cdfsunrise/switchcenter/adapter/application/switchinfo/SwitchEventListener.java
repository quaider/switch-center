package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import com.alibaba.fastjson.JSON;
import com.cdfsunrise.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import com.cdfsunrise.switchcenter.adapter.application.akka.CacheEvictionMessage;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchEvent;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoKey;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheKey;
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
        AkkaServerEnvironment.getEnv().getPublisher().tell(message, null);
    }

}

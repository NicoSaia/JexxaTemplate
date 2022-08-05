package io.jexxa.jexxatemplate.infrastructure.drivenadapter.messaging;

import io.jexxa.addend.infrastructure.DrivenAdapter;
import io.jexxa.infrastructure.drivenadapterstrategy.messaging.MessageSender;
import io.jexxa.jexxatemplate.domain.domainevent.BookSoldOut;
import io.jexxa.jexxatemplate.domainservice.DomainEventPublisher;

import java.util.Objects;
import java.util.Properties;

import static io.jexxa.infrastructure.drivenadapterstrategy.messaging.MessageSenderManager.getMessageSender;

@SuppressWarnings("unused")
@DrivenAdapter
public class DomainEventPublisherImpl implements DomainEventPublisher
{
    private final MessageSender messageSender;

    public DomainEventPublisherImpl(Properties properties)
    {
        messageSender = getMessageSender(DomainEventPublisher.class, properties);
    }

    @Override
    public void publish(BookSoldOut domainEvent)
    {
        Objects.requireNonNull(domainEvent);
        messageSender
                .send(domainEvent)
                .toTopic("BookStoreTopic")
                .addHeader("Type", domainEvent.getClass().getSimpleName())
                .asJson();
    }
}

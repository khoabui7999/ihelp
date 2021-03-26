package com.swp.ihelp.message;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@PropertySource("classpath:message.properties")
@Getter
public class ServiceMessage implements Serializable {
    @Value("${message.service.service-not-found}")
    private String serviceNotFoundMessage;

    @Value("${message.service.service-added}")
    private String serviceAddedMessage;

    @Value("${message.service.service-deleted}")
    private String serviceDeletedMessage;

    @Value("${message.service.service-used}")
    private String serviceUsedMessage;

    public String getServiceAddedMessage(String serviceId) {
        String result = this.serviceAddedMessage.replaceFirst("#", serviceId);
        return result;
    }

    public String getServiceDeletedMessage(String serviceId) {
        String result = this.serviceDeletedMessage.replaceFirst("#", serviceId);
        return result;
    }

    public String getServiceUsedMessage(String email, String serviceId) {
        String result = this.serviceDeletedMessage.replaceFirst("#", email);
        result = result.replaceFirst("#", email);
        return result;
    }
}

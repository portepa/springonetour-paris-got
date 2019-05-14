package io.pivotal.demos1tparis;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/");
        // config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/sockjs");
        registry.addEndpoint("/sockjs").withSockJS();
    }

    @MessageMapping("/vote")
    @SendTo("/vote")
    public Vote send(Vote vote) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println("vote" + vote.getVoteIndex() + " received");
        System.out.println("HEY GUYS ITS ME");
        return vote;
    }
}


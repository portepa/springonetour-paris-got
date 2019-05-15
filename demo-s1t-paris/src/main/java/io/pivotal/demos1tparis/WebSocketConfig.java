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


    WebSocketConfig(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    private final VoteRepository voteRepository;

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
//    @SendTo("/vote")
    public Vote send(Vote vote) throws Exception {
        System.out.println("vote" + vote.getVoteIndex() + " received");
        voteRepository.save(vote);
        return vote;
    }
}


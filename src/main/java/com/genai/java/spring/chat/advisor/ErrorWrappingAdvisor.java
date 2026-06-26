package com.genai.java.spring.chat.advisor;

import com.genai.java.spring.chat.openai.dto.response.SummarizationResponse;
import
        org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.util.List;


@Component
public class ErrorWrappingAdvisor implements CallAdvisor, StreamAdvisor {
    private static Logger logger = LoggerFactory.getLogger(ErrorWrappingAdvisor.class);
    private final ObjectMapper objectMapper;

    public ErrorWrappingAdvisor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logger.debug("Request received in Error Wrapping Advisor with prompt: {}",
                chatClientRequest.prompt().getUserMessage().getText());
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        String assistantMessage = chatClientResponse.chatResponse()
                .getResult().getOutput().getText().trim();

        if(!assistantMessage.startsWith("```json") && !assistantMessage.startsWith("{")) {
            SummarizationResponse summarizationResponse = new SummarizationResponse(null, null, assistantMessage);

            chatClientResponse = chatClientResponse.mutate()
                    .chatResponse(ChatResponse.builder()
                            .generations(List.of(new Generation(new AssistantMessage(objectMapper.writeValueAsString(summarizationResponse)))))
                            .build()
                    )
                    .context(chatClientRequest.context())
                    .build();
        }
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return "ErrorWrappingAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

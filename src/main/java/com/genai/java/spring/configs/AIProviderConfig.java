package com.genai.java.spring.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

    private static final String BEAN_PROVIDER_PREFIX = "app.ai";
    private static final String BEAN_PROVIDER_NAME = "provider";
    private static final String BEAN_PROVIDER_OPEN_AI = "openai";
    private static final String BEAN_PROVIDER_GEMINI = "gemini";
    private static final String BEAN_PROVIDER_OLLAMA = "ollama";

//    @Bean("openAIChatClient")
    @Bean
    @ConditionalOnProperty(prefix = BEAN_PROVIDER_PREFIX, name = BEAN_PROVIDER_NAME, havingValue = BEAN_PROVIDER_OPEN_AI)
    ChatClient openAIChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }

//    @Bean("ollamaChatClient")
    @Bean
    @ConditionalOnProperty(prefix = BEAN_PROVIDER_PREFIX, name = BEAN_PROVIDER_NAME, havingValue = BEAN_PROVIDER_OLLAMA, matchIfMissing = true)
    ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel).build();
    }

    @Bean
    @ConditionalOnProperty(prefix = BEAN_PROVIDER_PREFIX, name = BEAN_PROVIDER_NAME, havingValue = BEAN_PROVIDER_GEMINI)
    ChatClient geminiChatClient(GoogleGenAiChatModel googleGenAiChatModel){
        return ChatClient.builder(googleGenAiChatModel).build();
    }

}

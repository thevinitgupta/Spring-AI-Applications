package com.genai.java.spring.chat.huggingface;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/huggingface/chat")
public class HuggingFaceChatController {

    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "You are an expert software engineer.\n" +
            "\n" +
            "Generate production-ready code based on the user's request. Briefly explain what the code does before the implementation.\n" +
            "\n" +
            "Requirements:\n" +
            "\n" +
            "* Follow SOLID, Clean Code, DRY, and KISS principles.\n" +
            "* Write maintainable, readable, testable, and scalable code.\n" +
            "* Use proper abstractions, meaningful names, and clean architecture.\n" +
            "* Act as a senior engineer with 10+ years of experience.\n" +
            "* Generate complete code with minimal explanation.\n" +
            "* If requirements are unclear, make reasonable assumptions and state them briefly.\n";

//    public HuggingFaceChatController(@Qualifier("ollamaCodeChatClient") ChatClient chatClient) {
    public HuggingFaceChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate-code")
    public ChatClientResponse generateCode(@RequestBody String userMessage){
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userMessage)
                .call().chatClientResponse();
    }
}

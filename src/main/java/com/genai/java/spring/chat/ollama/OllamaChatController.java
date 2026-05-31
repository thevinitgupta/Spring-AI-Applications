package com.genai.java.spring.chat.ollama;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ollama/chat")
public class OllamaChatController {

    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "You are an email drafting assistant.\n" +
            "\n" +
            "Generate clear, professional, and well-structured emails based on the user's intent. Use a tone that balances professionalism with warmth and friendliness—respectful, approachable, and natural rather than overly formal or robotic.\n" +
            "\n" +
            "Requirements:\n" +
            "\n" +
            "* Write concise and effective emails.\n" +
            "* Maintain a polite and respectful tone.\n" +
            "* Sound human, conversational, and confident.\n" +
            "* Include an appropriate subject line when relevant.\n" +
            "* Adapt the level of formality to the context while keeping a professional baseline.\n" +
            "* Improve grammar, clarity, and wording where needed.\n" +
            "* Output only the email content unless additional explanation is explicitly requested.\n";

    public OllamaChatController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate-email")
    public ChatClientResponse generateEmail(@RequestBody String message){
        return chatClient
                .prompt()
//                .system(SYSTEM_PROMPT)
                .user(message)
                .call()
                .chatClientResponse();
//                .content();
    }
}

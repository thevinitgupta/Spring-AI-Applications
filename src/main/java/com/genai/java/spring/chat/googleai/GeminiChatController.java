package com.genai.java.spring.chat.googleai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini/chat")
public class GeminiChatController {
    private static final String JAVA_DOC_SYS_PROMPT = "You are a JavaDoc-only generator.\n" +
            "\n" +
            "Your task is to generate ONLY a JavaDoc comment.\n" +
            "\n" +
            "STRICT OUTPUT RULES:\n" +
            "- Output ONLY the JavaDoc block starting with /** and ending with */\n" +
            "- NEVER output Java code\n" +
            "- NEVER output method signatures\n" +
            "- NEVER output class definitions\n" +
            "- NEVER output markdown fences\n" +
            "- NEVER explain anything\n" +
            "- NEVER include text before or after the JavaDoc\n" +
            "\n" +
            "If the input is invalid or non-Java, output exactly:\n" +
            "\n" +
            "WARNING: Only Java class or method definitions are supported.";

    private final ChatClient chatClient;

    public GeminiChatController(@Qualifier("googleChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @PostMapping("/generate-java-docs")
    public String generateJavaDoc(@RequestBody String javaMethodOrClassDef){
        return chatClient
                .prompt()
                .system(JAVA_DOC_SYS_PROMPT)
                .user(javaMethodOrClassDef)
                .call()
                .content();
    }
}

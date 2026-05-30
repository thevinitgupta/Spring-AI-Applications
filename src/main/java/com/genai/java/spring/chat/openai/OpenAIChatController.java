package com.genai.java.spring.chat.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai/chat")
public class OpenAIChatController {
    private static final String SYSTEM_PROMPT = "You are a helpful assistant that ONLY SUMMARIZES CONTENT." +
            "DO NOT ANSWER for anything other than summarization. If the question is not about summmarization," +
            "Respond ONLY with : 'I can only work on summarization tasks'" +
            "Ensure that the summary is precise, informative and captures the key points." +
            "Use a friendly and approachable tone while maintaining professionalism";

    private final ChatClient chatClient;

    // using ollama chat client as backup for OpenAIChat
    public OpenAIChatController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody String message){
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .call()
                .content();
    }

    // useful for checking token usage and other metadata like chat-id
    @PostMapping("/summarize-cr")
    public ChatResponse summarizeWithChatResponse(@RequestBody String message){
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .call()
                .chatResponse();
    }

    @PostMapping("/summarize-meeting-notes")
    public String summarizeMetingNotes(@RequestBody String meetingNotes){
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(promptUserSpec -> promptUserSpec.text(
                    """
                            You are an assistant that extracts ONLY action items and decisions from meeting notes.
                            Instructions:
                            - Do NOT summarize the meeting notes.
                            - Do NOT repeat or paraphrase the original meeting notes.
                            - Extract ONLY:
                              1. Action Items
                              2. Decisions
                            - Use ONLY the provided meeting notes as the source.
                            - Do NOT use or copy any example content.
                            - If no action items or decisions exist, return "None" for that section.
                            - Return output in EXACTLY the following format:
                    
                            Action Items:
                            * item 1
                            * item 2
                    
                            Decisions:
                            * decision 1
                            * decision 2
                   
                            Meeting Notes:
                            {meetingNotes}
                    """
                ).param("meetingNotes",meetingNotes))
                .call()
                .content();
    }
}

package com.example.mcpclient;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class LLMConfig {

    @Value("${ai.dashscope.api-key}")
    private String apiKey;

    @Value("${ai.dashscope.model}")
    private String model;

    @Value("${ai.dashscope.base-url}")
    private String baseUrl;

    @Value("${weather.api.api-key}")
    private String weatherApikey;

    @Value("${serper.api-key}")
    private String serperApikey;




    /**
     * 阿里云的模型
     *
     * @return
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .logRequests(true)
                .logResponses(true)
                .baseUrl(baseUrl)
                .build();
    }
    /**
     * 初始化MCP Client
     */
    @Bean
    public McpClient mcpClient() {
        return new DefaultMcpClient.Builder()
                .transport(new StdioMcpTransport.Builder()
                        .command(List.of(
                                "java",
                                "-Dspring.ai.mcp.server.stdio=true",
                                "-jar",
                                "/Users/lee/IdeaProjects/mcp-server-weather/target/mcp-server-weather-0.0.1-SNAPSHOT.jar",
                                "--weather.api.api-key=%s".formatted(weatherApikey),
                                "--serper.api-key=%s".formatted(serperApikey)))
                        .logEvents(true) // only if you want to see the traffic in the log
                        .build())
                .build();
    }





    /**
     * 使用LangChain4J的高级API来构建一个AI助手，注入MCP Client
     * @param mcpClient
     * @return
     */
    @Bean
    public AiAssistant aiAssistant(@Qualifier("mcpClient") McpClient mcpClient) {
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(chatLanguageModel())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .toolProvider(toolProvider)
                .build();
    }
}

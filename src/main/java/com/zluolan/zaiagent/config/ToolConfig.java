package com.zluolan.zaiagent.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallback[] allTools(List<ToolCallbackProvider> toolCallbackProviders) {
        return toolCallbackProviders.stream()
                .flatMap(provider -> Arrays.stream(provider.getToolCallbacks()))
                .collect(Collectors.toList())
                .toArray(new ToolCallback[0]);
    }
}

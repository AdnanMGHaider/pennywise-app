package com.pennywise.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures a singleton OpenAIClient, reading credentials
 * (OPENAI_API_KEY, OPENAI_ORG_ID, OPENAI_PROJECT_ID) from env.
 */
@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAIClient openAIClient() {
        // Automatically pulls API key (and optional org/project IDs) from env vars
        return OpenAIOkHttpClient.fromEnv();
    }
}

package com.example.mcpclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class McpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);


    }

    @Bean
    public CommandLineRunner commandLineRunner(AiAssistant assistant) {
        return args -> {
            Scanner scanner = new Scanner(System.in);

            System.out.println("欢迎使用AI助手（输入 quit 退出）");
            while (true) {
                System.out.print("请输入你的问题🙋: ");
                String input = scanner.nextLine();

                if ("quit".equalsIgnoreCase(input.trim())) {
                    break;
                }

                String response = assistant.chat(input);
                System.out.println("🤖: " + response);
                System.out.println();
            }

            scanner.close();
            System.out.println("对话结束，正在退出...");
            SpringApplication.exit(SpringApplication.run(McpClientApplication.class));
        };
    }

}

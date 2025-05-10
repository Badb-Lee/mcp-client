package com.example.mcpclient;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;


@SpringBootTest
class McpClientApplicationTests {

    @Resource
    private AiAssistant aiAssistant;

    @Test
    public void testWeather(){
        System.out.println(aiAssistant.chat("获取当前项城的天气"));
    }

    @Test
    public void testHotEvent(){
        System.out.println(aiAssistant.chat("获取谷歌当前有关小米su7的热点事件"));
    }



}

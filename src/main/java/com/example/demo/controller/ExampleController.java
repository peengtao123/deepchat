package com.example.demo.controller;

import com.example.demo.util.ChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例控制器 - 展示不同场景的AI应用（支持DeepSeek等模型）
 */
@RestController
@RequestMapping("/api/ai/examples")
public class ExampleController {

    @Autowired
    private ChatAssistant chatAssistant;

    /**
     * 编程助手示例
     */
    @PostMapping("/programming")
    public Map<String, String> programmingHelp(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        
        Map<String, String> result = new HashMap<>();
        try {
            String response = chatAssistant.chatWithSystem(
                ChatAssistant.SystemPrompts.PROGRAMMING_EXPERT,
                question
            );
            result.put("response", response);
        } catch (Exception e) {
            result.put("error", "请求失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 翻译助手示例
     */
    @PostMapping("/translate")
    public Map<String, String> translate(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String targetLanguage = request.getOrDefault("targetLanguage", "中文");
        
        Map<String, String> result = new HashMap<>();
        try {
            String prompt = String.format("请将以下文本翻译成%s：\n%s", targetLanguage, text);
            String response = chatAssistant.chatWithSystem(
                ChatAssistant.SystemPrompts.TRANSLATOR,
                prompt
            );
            result.put("response", response);
        } catch (Exception e) {
            result.put("error", "请求失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 代码审查示例
     */
    @PostMapping("/code-review")
    public Map<String, String> codeReview(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        
        Map<String, String> result = new HashMap<>();
        try {
            String prompt = "请审查以下代码并提供改进建议：\n\n" + code;
            String response = chatAssistant.chatWithSystem(
                ChatAssistant.SystemPrompts.CODE_REVIEWER,
                prompt
            );
            result.put("response", response);
        } catch (Exception e) {
            result.put("error", "请求失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 学习导师示例
     */
    @PostMapping("/tutor")
    public Map<String, String> tutor(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        
        Map<String, String> result = new HashMap<>();
        try {
            String response = chatAssistant.chatWithSystem(
                ChatAssistant.SystemPrompts.TUTOR,
                question
            );
            result.put("response", response);
        } catch (Exception e) {
            result.put("error", "请求失败: " + e.getMessage());
        }
        return result;
    }
}

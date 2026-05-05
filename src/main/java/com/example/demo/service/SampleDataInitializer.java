package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * 示例数据初始化器
 * 应用启动时自动加载一些示例文档到知识库
 * 
 * 注意：如果 API 配置有问题，可以临时注释掉 @Component 注解
 */
// @Component  // 暂时禁用，避免启动时调用 API
public class SampleDataInitializer implements CommandLineRunner {

    @Autowired
    private RagService ragService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 开始加载示例文档 ===");
        
        // 示例文档1: Spring Boot 介绍
        ragService.addTextDocument(
            "Spring Boot 是由 Pivotal 团队提供的全新框架，其设计目的是用来简化新 Spring 应用的初始搭建以及开发过程。" +
            "该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。通过这种方式，Spring Boot 致力于在蓬勃发展的快速应用开发领域成为领导者。" +
            "Spring Boot 的特点包括：创建独立的 Spring 应用程序、嵌入的 Tomcat、无需部署 WAR 文件、简化的 Maven 配置、自动配置 Spring、提供生产就绪型功能等。",
            "source", "spring-boot-docs",
            "title", "Spring Boot 介绍"
        );

        // 示例文档2: RAG 技术介绍
        ragService.addTextDocument(
            "RAG (Retrieval-Augmented Generation) 检索增强生成是一种结合了信息检索和文本生成的技术架构。" +
            "RAG 的核心思想是：当用户提问时，系统先从知识库中检索相关的文档片段，然后将这些检索到的信息与用户问题一起输入给大语言模型，" +
            "让模型基于这些真实的信息生成回答。这样做的好处是可以减少模型的幻觉，提高回答的准确性，并且可以访问最新的信息。" +
            "RAG 的典型应用场景包括：企业知识库问答、文档智能搜索、客服系统、专业领域咨询等。",
            "source", "ai-tech-docs",
            "title", "RAG 技术介绍"
        );

        // 示例文档3: Java 特性
        ragService.addTextDocument(
            "Java 是一种广泛使用的面向对象编程语言，由 Sun Microsystems 于1995年推出。Java 的主要特性包括：" +
            "1. 跨平台性：一次编写，到处运行（Write Once, Run Anywhere）" +
            "2. 面向对象：支持封装、继承、多态等面向对象特性" +
            "3. 安全性：提供多层次的安全机制" +
            "4. 多线程：内置多线程支持" +
            "5. 自动内存管理：垃圾回收机制" +
            "6. 丰富的API：提供大量的标准库和第三方库" +
            "Java 在企业级应用、移动应用（Android）、大数据处理等领域有广泛应用。",
            "source", "java-docs",
            "title", "Java 语言特性"
        );

        // 示例文档4: 微服务架构
        ragService.addTextDocument(
            "微服务架构是一种将单个应用程序开发为一组小型服务的方法，每个服务运行在自己的进程中，" +
            "服务间通过轻量级的机制（通常是 HTTP API）进行通信。这些服务围绕业务能力构建，可以通过完全自动化的部署机制独立部署。" +
            "微服务的优势包括：易于开发和维护、技术栈灵活、易于扩展、容错性好、便于团队协作等。" +
            "常见的微服务框架包括：Spring Cloud、Dubbo、gRPC 等。" +
            "微服务也带来了一些挑战：分布式系统的复杂性、数据一致性、服务发现、负载均衡、监控和日志等。",
            "source", "architecture-docs",
            "title", "微服务架构介绍"
        );

        // 示例文档5: Docker 容器技术
        ragService.addTextDocument(
            "Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 或 Windows 机器上。" +
            "Docker 使用客户端-服务器架构，主要组件包括：Docker 客户端、Docker 主机、Docker 镜像、Docker 容器、Docker 仓库。" +
            "Docker 的优势：轻量级、快速启动、环境一致性、易于扩展、资源隔离等。" +
            "Docker 的核心概念：镜像（Image）、容器（Container）、仓库（Repository）。" +
            "Dockerfile 是用于构建 Docker 镜像的脚本文件，包含了一系列指令来定义镜像的构建过程。",
            "source", "devops-docs",
            "title", "Docker 容器技术"
        );

        System.out.println("=== 示例文档加载完成 ===");
        System.out.println("已加载 5 个示例文档到知识库");
        System.out.println("访问 http://localhost:8080/rag-chat.html 开始体验 RAG 问答");
    }
}

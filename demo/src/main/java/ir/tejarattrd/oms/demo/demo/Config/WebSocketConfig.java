package ir.tejarattrd.oms.demo.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // فعال‌سازی وب‌سوکت و message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // فعال‌سازی یک message broker ساده در حافظه برای ارسال پیام‌ها به کلاینت‌ها
        // کلاینت‌ها برای دریافت پیام باید روی مقاصدی با پیشوند /topic عضو شوند
        registry.enableSimpleBroker("/topic");
        // پیشوند مقصد برای پیام‌هایی که از کلاینت به سرور ارسال می‌شوند
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ثبت یک اندپوینت به نام /ws که کلاینت‌ها برای اتصال از آن استفاده می‌کنند
        // withSockJS() برای پشتیبانی از مرورگرهایی است که وب‌سوکت را پشتیبانی نمی‌کنند
        registry.addEndpoint("/ws").withSockJS();
    }
}
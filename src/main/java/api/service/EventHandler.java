package api.service;

import com.google.common.util.concurrent.Futures;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Component
public class EventHandler implements ApplicationListener<ApplicationReadyEvent> {


    private ViberBot bot;
    private ViberBotSevice viberBotSevice;

    @Autowired
    public EventHandler(ViberBot bot, ViberBotSevice viberBotSevice) {
        this.bot = bot;
        this.viberBotSevice = viberBotSevice;
    }

    @Value("${application.viber-bot.webhook-url}")
    private String webhookUrl;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        try {
            bot.setWebhook(webhookUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        bot.onMessageReceived((event, message, response) -> {
            try {
                viberBotSevice.onMessageReceived(event, message, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        bot.onConversationStarted(event -> viberBotSevice.onConversationStarted(event));
    }


}

package api.service;

import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Future;


@Service
public class ViberBotSevice {

    private WeatherService weatherService;
    private CityService cityService;

    @Autowired
    public ViberBotSevice(WeatherService weatherService, CityService cityService) {
        this.weatherService = weatherService;
        this.cityService = cityService;
    }


    public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {

        return Futures.immediateFuture(Optional.of(
                new TextMessage("Привіт " + event.getUser().getName())));
    }

    public void onMessageReceived(IncomingMessageEvent event, Message message, Response response) throws IOException {

        String city = substringUserText(message.toString());
        String country = cityService.getCountryByCity(city);

        response.send(new TextMessage("At this moment in " + city + ", " + country + ":" + "\n" + weatherService.getWeather(city)));
    }

    private String substringUserText(String textMessage) {
        return textMessage.substring(textMessage.indexOf("text=") + 5, textMessage.indexOf("messageStr") - 2);
    }

}





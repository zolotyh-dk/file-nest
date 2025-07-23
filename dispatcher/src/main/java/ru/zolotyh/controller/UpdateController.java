package ru.zolotyh.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zolotyh.service.UpdateProducer;
import ru.zolotyh.utils.MessageUtils;

import static ru.zolotyh.model.RabbitQueue.*;

@Component
@Slf4j
public class UpdateController {
    private final UpdateProducer updateProducer;
    private TelegramBot telegramBot;

    @Autowired
    public UpdateController(UpdateProducer updateProducer) {
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type: {}", update);
        }
    }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getDocument() != null) {
            processDocMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setPhotoIsReceivedView(update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setPhotoIsReceivedView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setPhotoIsReceivedView(Update update) {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,
                "Файл получен и обрабатывается!");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}

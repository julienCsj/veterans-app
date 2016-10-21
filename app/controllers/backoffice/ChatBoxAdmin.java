package controllers.backoffice;

import models.ChatBoxMessage;
import models.Track;

import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class ChatBoxAdmin extends SecureController {

    public static void index() {
        List<ChatBoxMessage> messagesChat = ChatBoxMessage.find("order by dateCreation DESC").fetch();
        render(messagesChat);
    }

    public static void silence(Long id) {
        ChatBoxMessage message = ChatBoxMessage.findById(id);
        message.silence = !message.silence;
        message.save();
        flash.success("OK");
        index();
    }
}

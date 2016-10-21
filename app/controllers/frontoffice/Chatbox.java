package controllers.frontoffice;

import models.ChatBoxMessage;
import models.Compte;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by juliencustoja on 21/10/2016.
 */
public class Chatbox extends Controller {

    public static void chatbox(String hash) {
        Compte compte = Compte.find("hash = ?", hash).first();
        render(compte);
    }

    public static void chatboxPost(String hash) {
        String message = params.get("message");
        Compte compte = Compte.find("hash = ?", hash).first();
        ChatBoxMessage messageChat = new ChatBoxMessage(compte, message);
        messageChat.save();
        renderJSON("success");
    }

    public static void messagesList(String hash) {
        List<ChatBoxMessage> messagesList = ChatBoxMessage.find("silence = false").fetch(50);
        Compte compte = Compte.find("hash = ?", hash).first();
        render(messagesList, compte);
    }
}

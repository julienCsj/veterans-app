package controllers.frontoffice;

import models.ChatBoxMessage;
import models.Compte;
import play.data.binding.As;
import play.mvc.Controller;
import play.templates.BaseTemplate;
import play.templates.JavaExtensions;
import play.templates.Template;
import play.templates.TemplateLoader;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juliencustoja on 21/10/2016.
 */
public class Chatbox extends Controller {

    public static void chatbox(String hash) {
        Compte compte = Compte.find("hash = ?", hash).first();
        List<ChatBoxMessage> messagesList = ChatBoxMessage.find("silence = false").fetch(50);
        Date dernierCheck = new Date();
        render(compte, dernierCheck, messagesList);
    }

    public static void chatboxPost(String hash) {
        String message = params.get("message");
        Compte compte = Compte.find("hash = ?", hash).first();
        ChatBoxMessage messageChat = new ChatBoxMessage(compte, message);
        messageChat.save();
        renderJSON("success");
    }

    public static void messagesList(String hash, @As("dd-MM-yyyy-HH:mm:ss.SSS") Date dernierCheck) {
        System.out.println(new Date() + " -> APPEL");
        List<ChatBoxMessage> messagesList = ChatBoxMessage.find("silence = false AND dateCreation > ?", dernierCheck).fetch(50);
        Compte compte = Compte.find("hash = ?", hash).first();
        Template t = TemplateLoader.load("frontoffice/Chatbox/messagesList.html");
        Map<String, Object> args = new HashMap<>();
        args.put("messagesList", messagesList);
        args.put("compte", compte);
        String html = t.render(args);

        Date date = new Date();
        String dateAsStr = JavaExtensions.format(date, "dd-MM-yyyy-HH:mm:ss.SSS", "fr");
        Map<String, String> json = new HashMap<>();
        json.put("html", html);
        json.put("dernierCheck", dateAsStr);
        renderJSON(json);
    }
}

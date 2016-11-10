package controllers.backoffice;

import models.ChatBoxMessage;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import play.Play;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by juliencustoja on 10/11/2016.
 */
public class Missions extends SecureController {


    private static FTPClient connectFTP() {
        String server = "188.165.46.214";
        int port = 21;
        String user = "vetapp";
        String pass = "jul2I2Y8";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);

            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return null;
            }

            boolean success = ftpClient.login(user, pass);
            if (!success) {
                return null;
            }

            return ftpClient;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void index() {


        FTPFile[] files = null;
        FTPFile[] filesArchive = null;
        String successStr = "";
        String errorStr = "";

        FTPClient ftpClient = connectFTP();
        if(ftpClient == null) {
            flash.error("Erreur lors de la connexion au serveur");
            render();
        }

        try {
            boolean success = true;

            // Liste des missions
            success = ftpClient.changeWorkingDirectory("/mpmissions");
            if(!success) {
                flash.error("Impossible de lire le contenu du repertoire /mpmissions");
                render();
            }
            files = ftpClient.listFiles();

            // Liste des missions archivées
            success = ftpClient.changeWorkingDirectory("/mpmissions_archives");
            if(!success) {
                flash.error("Impossible de lire le contenu du repertoire /mpmissions_archive");
                render();
            }
            filesArchive = ftpClient.listFiles();


            // logs out
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException ex) {
            flash.error("Une erreur est survenue. Voir avec Julien");
            render();
        }

        List<FTPFile> filesList = Arrays.asList(files);
        List<FTPFile> filesArchiveList = Arrays.asList(filesArchive);

        Collections.sort(filesList, new Comparator<FTPFile>(){
            public int compare(FTPFile p1, FTPFile p2){
                return p2.getTimestamp().getTime().compareTo(p1.getTimestamp().getTime());
            }
        });

        Collections.sort(filesArchiveList, new Comparator<FTPFile>(){
            public int compare(FTPFile p1, FTPFile p2){
                return p2.getTimestamp().getTime().compareTo(p1.getTimestamp().getTime());
            }
        });

        render(filesList, filesArchiveList);
    }


    public static void archiverFichier(String name) {
        FTPClient ftpClient = connectFTP();
        if(ftpClient == null) {
            flash.error("Erreur lors de la connexion au serveur");
            render();
        }

        try {
            boolean success = true;
            // Liste des missions
            success = ftpClient.rename("/mpmissions/"+name, "mpmissions_archives/"+name);
            if(!success) {
                flash.error("Erreur lors de l'archivage de la mission.");
                flash.keep();
                index();
            }

        } catch (Exception e) {
            e.printStackTrace();
            flash.error("Erreur lors de l'archivage de la mission. Voir avec Julien");
            flash.keep();
            index();
        }

        flash.success("Mission archivée");
        flash.keep();
        index();
    }

    public static void desarchiverFichier(String name) {
        FTPClient ftpClient = connectFTP();
        if(ftpClient == null) {
            flash.error("Erreur lors de la connexion au serveur");
            render();
        }

        try {
            boolean success = true;
            // Liste des missions
            success = ftpClient.rename("/mpmissions_archives/"+name, "mpmissions/"+name);
            if(!success) {
                flash.error("Erreur lors du desarchivage de la mission.");
                flash.keep();
                index();
            }

        } catch (Exception e) {
            e.printStackTrace();
            flash.error("Erreur lors du desarchivage de la mission. Voir avec Julien");
            flash.keep();
        }

        flash.success("Mission desarchivée");
        flash.keep();

        index();
    }
}

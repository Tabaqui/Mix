package ru.motleycrew;

import ru.motleycrew.model.UWMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        while (true) {
            refreshMessage();
            Thread.currentThread().sleep(1000 * 60 * 1);
        }
    }

    private static void refreshMessage() throws Exception {
        String urlString = "https://www.upwork.com//login";
        UserData data = Client.getLogin(urlString);
        UWMessage lastMessage = Client.lastWorkHeader(urlString, data);
        Client.close();
        if (lastMessage == null) {
            System.out.println(" last message " + " is null");
            return;
        }
        Path path = Paths.get("headers.txt");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        BufferedReader headerStream = Files.newBufferedReader(Paths.get("headers.txt"), Charset.forName("utf8"));
        String oldHeader = headerStream.readLine();
        headerStream.close();
        System.out.println(lastMessage.getHeader() + " " + lastMessage.getHref());
        String lastHeader = lastMessage.getHeader();
        if (lastHeader != null && !lastHeader.isEmpty() && !lastHeader.equals(oldHeader)) {
            ru.motleycrew.Sender.send(lastMessage, "tabaqui");
            System.out.println("UWMessage send");
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("headers.txt"), Charset.forName("utf8"), new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING});
            writer.write(lastHeader);
            writer.close();
        }
    }
}

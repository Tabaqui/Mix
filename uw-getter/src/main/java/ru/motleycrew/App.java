package ru.motleycrew;

import org.apache.commons.lang3.StringUtils;
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
            Thread.currentThread().sleep(1000 * 30 * 1);
        }
    }

    private static void refreshMessage() throws Exception {
        UWMessage lastMessage = Client.lastWorkHeader();
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
        System.out.println(oldHeader);
        String lastHeader = lastMessage.getHeader();

        if (StringUtils.isBlank(lastHeader)) {
            return;
        }

        lastHeader = lastHeader.trim().split("\n")[0];
        if (!lastHeader.equals(oldHeader)) {
            System.out.println("_" + StringUtils.difference(lastHeader, oldHeader) + "_");
            ru.motleycrew.Sender.send(lastMessage, "tabaqui");
            System.out.println("UWMessage send");
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("headers.txt"), Charset.forName("utf8"), new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING});
            writer.write(lastHeader);
            writer.close();
        }
    }
}

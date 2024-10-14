package be.pbo.jeugdcup.ranking.services;

import freemarker.template.TemplateException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CardRenderingService {


    public void renderCards(List<CardPlayer> cardPlayers) {

        try (PrintWriter pw = new PrintWriter(new FileOutputStream("out.html"))) {
            FreeMarkerGenerator templateEngine = new FreeMarkerGenerator(cardPlayers);
            String html = templateEngine.generateHTML("cardTemplate.html");
            pw.println(html);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}

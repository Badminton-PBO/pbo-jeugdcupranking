package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EventNameWithDate;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryCardImpl;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.TemplateException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CardRenderingService {


    public void renderCard(Player player, EventNameWithDate firstMatch) {

        try (OutputStream os = new FileOutputStream("out.pdf")) {
            FreeMarkerGenerator templateEngine = new FreeMarkerGenerator(player, firstMatch);
            String html = templateEngine.generateHTML("cardTemplate.html");

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //String htmlTemplateUri = CardRenderingService.class.getResource("/cardTemplate.html").toURI().toString();
            //builder.withUri(htmlTemplateUri);
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}

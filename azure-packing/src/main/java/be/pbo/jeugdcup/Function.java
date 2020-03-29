package be.pbo.jeugdcup;

import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.services.RankingGenerator;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java&code={your function key}
     * 2. curl "{your host}/api/HttpTrigger-Java?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, it is used to invoke function deployed to Azure.
     * More details: https://aka.ms/functions_authorization_keys
     */
    @FunctionName("pboJeugdcupRanking")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) final HttpRequestMessage<byte[]> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        if (request.getQueryParameters().get("isAlwaysUsingDoubleSchemes") == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please set query parameter isAlwaysUsingDoubleSchemes").build();
        }

        // Parse query parameter
        final Boolean isAlwaysUsingDoubleSchemes = Boolean.valueOf(request.getQueryParameters().get("isAlwaysUsingDoubleSchemes"));
        final int bodyLength = request.getBody().length;

        final Random rand = new Random();
        final File file;
        try {
            file = File.createTempFile("PBO-Jeugdcup-" + rand.nextInt(1000), ".tp");
            file.deleteOnExit();
            writeByte(request.getBody(), file);
        } catch (final IOException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Unable to create tmp file:" + e).build();
        }

        final RankingGenerator rankingGenerator = new RankingGenerator(file.toPath(), isAlwaysUsingDoubleSchemes);
        final Map<Player, Integer> playerIntegerMap;
        try {
            playerIntegerMap = rankingGenerator.generate();
            final String resultCSV = convertToCSVString(playerIntegerMap, rankingGenerator.getTournamentNameAndDate());

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("isAlwaysUsingDoubleSchemes", isAlwaysUsingDoubleSchemes.toString())
                    .header("file", file.getPath())
                    .header("fileSize", "" + bodyLength)
                    .body(resultCSV).build();

        } catch (final RuntimeException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Unable to generate ranking:" + e).build();
        }

    }

    private String convertToCSVString(final Map<Player, Integer> playerIntegerMap, final String tournamentNameAndDate) {
        final StringBuilder resultCSV = new StringBuilder();
        resultCSV.append("vblId").append(",");
        resultCSV.append("firstName").append(",");
        resultCSV.append("lastName").append(",");
        resultCSV.append("gender").append(",");
        resultCSV.append("clubName").append(",");
        resultCSV.append("ageCategory").append(",");
        resultCSV.append("tournament").append(",");
        resultCSV.append("point");
        resultCSV.append("\r\n");
        for (final Map.Entry<Player, Integer> kvp : playerIntegerMap.entrySet()) {
            final Player player = kvp.getKey();
            resultCSV.append(escapeSpecialCharacters(player.getMemberId())).append(",");
            resultCSV.append(escapeSpecialCharacters(player.getFirstName())).append(",");
            resultCSV.append(escapeSpecialCharacters(player.getLastName())).append(",");
            resultCSV.append(escapeSpecialCharacters(player.getGender().getGenderShort())).append(",");
            resultCSV.append(escapeSpecialCharacters(player.getClubName())).append(",");
            resultCSV.append(escapeSpecialCharacters(player.getAgeCategory().getKey())).append(",");
            resultCSV.append(escapeSpecialCharacters(tournamentNameAndDate)).append(",");
            resultCSV.append(kvp.getValue());
            resultCSV.append("\r\n");
        }
        return resultCSV.toString();
    }


    //Don't want to introduce a full blown CSV external lib for writing just a small CSV
    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    // Method which write the bytes into a file
    private void writeByte(final byte[] bytes, final File file) {
        /**
         final byte[] bytesUnboxed = new byte[bytes.length];
         int i = 0;
         for (final Byte b : bytes) {
         bytesUnboxed[i++] = b;
         }
         **/

        try {

            // Initialize a pointer
            // in file using OutputStream
            final OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);

            // Close the file
            os.close();
        } catch (final Exception e) {
            throw new RuntimeException("Unable to write file" + file.getName());
        }
    }
}

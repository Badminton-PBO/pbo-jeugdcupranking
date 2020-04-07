package be.pbo.jeugdcup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testPboJeugdcupRanking() throws Exception {
        // Setup
        @SuppressWarnings("unchecked") final HttpRequestMessage<byte[]> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("isAlwaysUsingDoubleSchemes", "true");
        queryParams.put("tournamentSequenceNumberAndName", "01 Vlabad");
        doReturn(queryParams).when(req).getQueryParameters();

        final Path tpPath = Paths.get(FunctionTest.class.getResource("/tpFiles/PBO-Jeugdcuptour-VLABAD-2020.tp").toURI());
        final byte[] bytes = Files.readAllBytes(tpPath);
        doReturn(bytes).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(final InvocationOnMock invocation) {
                final HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new Function().run(req, context);

        // Verify
        assertEquals(HttpStatus.OK, ret.getStatus());
        final String[] csvLines = ((String) ret.getBody()).split("\\r?\\n");
        assertEquals("vblId,firstName,lastName,gender,clubName,ageCategory,tournament,point", csvLines[0], "Expecting correct header CSV line");
        MatcherAssert.assertThat("A QualificationScheme with 8 teams and 4 matches can be converted into a List of EliminationSchemes",
                Arrays.asList(csvLines),
                Matchers.hasItem("50840673,Rafael,Van Den Daele,M,FLEE SHUTTLE BK,U15,\"01 VLABAD, 25 JAN 2020\",55"));
    }
}

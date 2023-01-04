package io.jexxa.jexxatemplate.integration;

import io.jexxa.jexxatemplate.JexxaTemplate;
import io.jexxa.jexxatemplate.domain.book.ISBN13;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.jexxa.infrastructure.drivingadapter.rest.JexxaWebProperties.JEXXA_REST_PORT;
import static io.jexxa.jexxatemplate.domain.book.ISBN13.createISBN;
import static io.jexxa.jexxatest.JexxaTest.getJexxaTest;
import static kong.unirest.ContentType.APPLICATION_JSON;
import static kong.unirest.HeaderNames.CONTENT_TYPE;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JexxaTemplateIT
{
    static private final String CONTEXT_NAME = "/BoundedContext/contextName";
    static private final String IS_RUNNING = "/BoundedContext/isRunning";

    static private final String ADD_TO_STOCK = "/BookStoreService/addToStock";
    static private final String AMOUNT_IN_STOCK = "/BookStoreService/amountInStock";


    static private String restPath;
    private static final ISBN13 ANY_BOOK = createISBN("978-3-86490-387-8" );

    @BeforeEach
    void initBeforeEach()
    {
        var jexxaTest = getJexxaTest(JexxaTemplate.class);

        restPath = "http://localhost:" + jexxaTest.getProperties().getProperty(JEXXA_REST_PORT);

        //Wait until application was started (using 10 seconds should be sufficient to start large applications)
        await().atMost(10, TimeUnit.SECONDS)
                .pollDelay(100, TimeUnit.MILLISECONDS)
                .ignoreException(UnirestException.class)
                .until(() -> getRequest(restPath + IS_RUNNING, Boolean.class));

    }


    @Test
    void testStartupApplication()
    {
        //Arrange -

        //Act
        var result = getRequest(restPath + CONTEXT_NAME, String.class);

        //Assert
        assertEquals(JexxaTemplate.class.getSimpleName(), result);
    }

    @Test
    void testAddBook()
    {
        //Arrange
        var amount = 5;
        var inStock = postRequest(restPath + AMOUNT_IN_STOCK, Integer.class, ANY_BOOK );
        var expectedResult = amount + inStock;

        //Act
        postRequest(restPath + ADD_TO_STOCK, Void.class, new Object[]{ANY_BOOK, amount});
        var result = postRequest(restPath + AMOUNT_IN_STOCK, Integer.class, ANY_BOOK );

        //Assert
        assertEquals(expectedResult, result);
    }


    static public <T> T getRequest(String uri, Class<T> returnType)
    {
        return Unirest.get(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .asObject(returnType)
                .getBody();
    }

    @SuppressWarnings("UnusedReturnValue")
    static public <T> T postRequest(String uri, Class<T> returnType, Object[] parameters)
    {
        return Unirest.post(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(parameters)
                .asObject(returnType).getBody();
    }

    static public <T> T postRequest(String uri, Class<T> returnType, Object parameter)
    {
        return Unirest.post(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(parameter)
                .asObject(returnType).getBody();
    }

    @AfterAll
    static void tearDown()
    {
        Unirest.shutDown();
    }
}

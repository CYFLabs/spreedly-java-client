package spreedly.client.java.request;

import static spreedly.client.java.http.Request.GET;

import java.net.URL;

import spreedly.client.java.Credentials;
import spreedly.client.java.exception.HttpHandlingException;
import spreedly.client.java.exception.XmlParserException;
import spreedly.client.java.http.HttpHandler;
import spreedly.client.java.http.HttpHandlerFactory;
import spreedly.client.java.http.Request;
import spreedly.client.java.http.Response;
import spreedly.client.java.model.PaymentMethod;
import spreedly.client.java.xml.XmlParserFactory;

public class PaymentMethodRequests
{

    public static PaymentMethod show(String token, Credentials credentials) throws XmlParserException, HttpHandlingException
    {
        URL url = UrlsBuilder.showPaymentMethod(token);
        Request request = new Request(url, GET, credentials);

        HttpHandler httpHandler = HttpHandlerFactory.getHttpHandler();
        Response response = httpHandler.execute(request);

        return XmlParserFactory.getXmlParser().parsePaymentMethod(response.body);
    }

}
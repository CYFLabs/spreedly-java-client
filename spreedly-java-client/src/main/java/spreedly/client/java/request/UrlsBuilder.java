package spreedly.client.java.request;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlsBuilder
{

    private static final Logger log = LoggerFactory.getLogger(UrlsBuilder.class); 

    private static final String V1_BASE_URL = "https://core.spreedly.com/v1";

    public static URL purchase(String gatewayToken)
    {
        return buildUrl("%s/gateways/%s/purchase.xml", V1_BASE_URL, gatewayToken);
    }

    public static URL showTransaction(String token)
    {
        return buildUrl("%s/transactions/%s.xml", V1_BASE_URL, token);
    }

    public static URL showPaymentMethod(String token)
    {
        return buildUrl("%s/payment_methods/%s.xml", V1_BASE_URL, token);
    }

    public static URL verifyPaymentMethod(String gatewayToken)
    {
        return buildUrl("%s/gateways/%s/verify.xml", V1_BASE_URL, gatewayToken);
    }

    private static URL buildUrl(String url, Object... args)
    {
        String formattedUrl = String.format(url, args);
        try
        {
            return new URL(formattedUrl);
        }
        catch (MalformedURLException e)
        {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

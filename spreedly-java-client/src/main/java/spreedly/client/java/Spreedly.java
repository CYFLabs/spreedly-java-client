package spreedly.client.java;

import spreedly.client.java.exception.SpreedlyClientException;
import spreedly.client.java.model.Transaction;
import spreedly.client.java.request.TransactionRequest;

public class Spreedly
{

    private final Credentials credentials;

    private Spreedly(String environmentKey, String accessSecret)
    {
        this.credentials = new Credentials(environmentKey, accessSecret);
    }

    public static Spreedly getInstance(String environmentKey, String accessSecret)
    {
        return new Spreedly(environmentKey, accessSecret);
    }

    public Transaction findTransaction(String token) throws SpreedlyClientException
    {
        return TransactionRequest.show(token, credentials);
    }

}
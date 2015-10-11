package spreedly.client.java.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spreedly.client.java.exception.HttpHandlingException;

/**
 * This class provides a default implementation of {@link HttpHandler} using
 * {@link java.net.HttpURLConnection}. To use a different HttpURLConnection
 * implementation simply override the
 * {@link UrlConnectionHttpHandler#execute(Request)} method.
 * 
 * @author Kevin Litwack (kevin@kevinlitwack.com)
 */
public class UrlConnectionHttpHandler implements HttpHandler
{
    private static final Logger log = LoggerFactory.getLogger(UrlConnectionHttpHandler.class);

    /**
     * Sends an HTTP request.
     * 
     * @param request
     *            The {@link Request} to send.
     * @return A {@link Response} object describing the response from the
     *         server.
     * @throws IOException
     *             If there was an error during the connection.
     */
    @Override
    public Response execute(Request request) throws HttpHandlingException
    {
        try
        {
            HttpURLConnection connection = openConnection(request);
            sendRequest(connection, request);
            return readResponse(connection);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
            throw new HttpHandlingException(e);
        }
    }

    // /// PROTECTED METHODS /////

    /**
     * Opens a connection based on the URL in the given request.
     * 
     * Subclasses can override this method to use a different implementation of
     * {@link HttpURLConnection}.
     * 
     * @param request
     *            The {@link Request}.
     * @return A new {@link HttpURLConnection}.
     * @throws IOException
     *             If there is an error opening the connection.
     */
    protected HttpURLConnection openConnection(Request request)
            throws IOException
    {
        HttpURLConnection result;
        if (request.proxy != null)
        {
            result = (HttpURLConnection) request.url
                    .openConnection(request.proxy);
        } else
        {
            result = (HttpURLConnection) request.url.openConnection();
        }
        result.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        result.setReadTimeout(DEFAULT_READ_TIMEOUT);
        return result;
    }

    /**
     * Sends a request over a given connection.
     * 
     * @param connection
     *            The connection over which to send the request.
     * @param request
     *            The request to send.
     * @throws IOException
     *             If there is an error sending the request.
     */
    protected void sendRequest(HttpURLConnection connection, Request request)
            throws IOException
    {
        // Set up the request.
        connection.setRequestMethod(request.method);
        setRequestProperties(connection, request);

        // If the request has a body, send it. Otherwise just connect.
        if (request.body != null)
        {
            connection.setDoOutput(true);
            request.body.writeTo(connection.getOutputStream());
        } else
        {
            connection.connect();
        }
    }

    protected void setRequestProperties(HttpURLConnection connection,
            Request request)
    {
        connection.setRequestProperty("Authorization",
                request.authorization.getAuthString());
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setRequestProperty("Accept", "application/xml");
        if (request.properties != null)
        {
            for (Map.Entry<String, String> entry : request.properties
                    .entrySet())
            {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Reads a {@link Response} from an existing connection. This method should
     * only be called on a connection for which the entire request has been
     * sent.
     * 
     * @throws IOException
     *             If there is an error reading the response from the
     *             connection.
     */
    protected Response readResponse(HttpURLConnection connection)
            throws IOException
    {
        // Try to get the input stream and if that fails, try to get the error
        // stream.
        InputStream in;
        try
        {
            in = connection.getInputStream();
        } catch (IOException e)
        {
            in = connection.getErrorStream();
        }

        // Build and return the HTTP response object.
        return new Response(connection.getResponseCode(), in);
    }

    // /// PRIVATE CONSTANTS /////

    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private static final int DEFAULT_READ_TIMEOUT = 30000;

}
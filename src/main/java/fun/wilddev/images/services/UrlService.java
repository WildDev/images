package fun.wilddev.images.services;

import fun.wilddev.images.config.props.UrlProps;
import fun.wilddev.images.exceptions.RemoteConnectionException;
import fun.wilddev.spring.core.services.MessageService;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.*;

@Service
public class UrlService {

    private final Integer connectTimeout;

    private final Integer readTimeout;

    private final MessageService messageService;

    public UrlService(UrlProps urlProps, MessageService messageService) {

        this.connectTimeout = urlProps.connectTimeout();
        this.readTimeout = urlProps.readTimeout();

        this.messageService = messageService;
    }

    public URLConnection getConnection(@NonNull String url) throws RemoteConnectionException {

        try {
            URLConnection conn = new URI(url).toURL().openConnection();

            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            conn.connect();

            return conn;

        } catch (Exception ex) {
            throw new RemoteConnectionException(messageService
                    .getMessage("exception.remote.resource.unreachable", url), ex);
        }
    }
}

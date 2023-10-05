package fun.wilddev.images.testers;

import fun.wilddev.images.config.props.ImageProps;
import fun.wilddev.images.exceptions.UnsupportedContentTypeException;
import fun.wilddev.images.testers.predefined.ExceptionalTester;

import fun.wilddev.spring.core.services.MessageService;

import org.springframework.stereotype.Component;

@Component
public class ImageContentTypeExceptionalTester implements ExceptionalTester<String> {

    private final ContentTypeTester contentTypeTester;

    private final MessageService messageService;

    public ImageContentTypeExceptionalTester(ImageProps imageProps,
                                             MessageService messageService) {

        this.contentTypeTester = new ContentTypeTester(imageProps.allowedTypes());
        this.messageService = messageService;
    }

    @Override
    public void test(String contentType) throws UnsupportedContentTypeException {

        if (!contentTypeTester.test(contentType))
            throw new UnsupportedContentTypeException(messageService
                    .getMessage("exception.content.type.unsupported", contentType));
    }
}

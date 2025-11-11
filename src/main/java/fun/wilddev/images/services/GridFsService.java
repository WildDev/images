package fun.wilddev.images.services;

import fun.wilddev.spring.core.services.MessageService;

import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import fun.wilddev.images.exceptions.gridfs.*;
import java.io.*;
import org.springframework.data.mongodb.gridfs.*;

@AllArgsConstructor
@Service
public class GridFsService {

    private final GridFsTemplate gridFsTemplate;

    private final MessageService messageService;

    private Query getFilenameQuery(String id) {
        return Query.query(GridFsCriteria.whereFilename().is(id));
    }

    public GridFsResource findById(@NonNull String id) {
        return gridFsTemplate.getResource(gridFsTemplate.findOne(getFilenameQuery(id)));
    }

    public void store(@NonNull String id, @NonNull String contentType, @NonNull InputStream inputStream) {
        gridFsTemplate.store(inputStream, id, contentType);
    }

    public void store(@NonNull String id, @NonNull String contentType, @NonNull File file) throws GridFsException {

        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            store(id, contentType, is);
        } catch (Exception ex) {
            throw new GridFsStorageException(messageService
                    .getMessage("exception.file.gridfs.storage.failed"), ex);
        }
    }

    public void delete(@NonNull String id) {
        gridFsTemplate.delete(getFilenameQuery(id));
    }
}

package com.wilddev.image.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wilddev.image.exceptions.gridfs.*;
import java.io.*;
import org.springframework.data.mongodb.gridfs.*;

@AllArgsConstructor
@Service
public class GridFsService {

    private final GridFsTemplate gridFsTemplate;

    private final MessageService messageService;

    @LogTimeExecuted("GridFsService#getFilenameQuery(id)")
    private Query getFilenameQuery(String id) {
        return Query.query(GridFsCriteria.whereFilename().is(id));
    }

    @LogTimeExecuted("GridFsService#findById(id)")
    public GridFsResource findById(@NonNull String id) {

        GridFSFile file = gridFsTemplate.findOne(getFilenameQuery(id));

        if (file == null)
            return null;

        return gridFsTemplate.getResource(file);
    }

    @LogTimeExecuted("GridFsService#store(id, contentType, inputStream)")
    public void store(@NonNull String id, @NonNull String contentType, @NonNull InputStream inputStream) {
        gridFsTemplate.store(inputStream, id, contentType);
    }

    @LogTimeExecuted("GridFsService#store(id, contentType, file)")
    public void store(@NonNull String id, @NonNull String contentType, @NonNull File file) throws GridFsException {

        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            store(id, contentType, is);
        } catch (Exception ex) {
            throw new GridFsStorageException(messageService
                    .getMessage("exception.file.gridfs.storage.failed"), ex);
        }
    }

    @LogTimeExecuted("GridFsService#delete(id)")
    public void delete(@NonNull String id) {
        gridFsTemplate.delete(getFilenameQuery(id));
    }
}

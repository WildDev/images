package com.wilddev.image.services;

import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.util.List;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wilddev.image.entities.settings.*;
import com.wilddev.image.repositories.settings.*;

@AllArgsConstructor
@Service
public class SettingsService {

    private final CropSizeRepository cropSizeRepository;

    private final ResizeSizeRepository resizeSizeRepository;

    @LogTimeExecuted("SettingsService#listCropSizes")
    @Transactional
    public List<CropSize> listCropSizes() {
        return cropSizeRepository.findAll();
    }

    @LogTimeExecuted("SettingsService#listResizeSizes")
    @Transactional
    public List<ResizeSize> listResizeSizes() {
        return resizeSizeRepository.findAll();
    }
}

package fun.wilddev.images.services;

import java.util.List;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fun.wilddev.images.entities.settings.*;
import fun.wilddev.images.repositories.settings.*;

@AllArgsConstructor
@Service
public class SettingsService {

    private final CropSizeRepository cropSizeRepository;

    private final ResizeSizeRepository resizeSizeRepository;

    @Transactional
    public List<CropSize> listCropSizes() {
        return cropSizeRepository.findAll();
    }

    @Transactional
    public List<ResizeSize> listResizeSizes() {
        return resizeSizeRepository.findAll();
    }
}

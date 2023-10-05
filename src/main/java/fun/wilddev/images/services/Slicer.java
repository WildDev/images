package fun.wilddev.images.services;

import org.springframework.data.domain.Slice;

public interface Slicer<T> {

    Slice<T> sliceNew(int size);
}

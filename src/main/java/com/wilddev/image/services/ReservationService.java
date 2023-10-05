package com.wilddev.image.services;

import java.util.List;

public interface ReservationService<T> {

    List<T> listNew(int batchSize);

    void reserve(List<T> items);
}

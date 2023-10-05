package com.wilddev.image.schedulers.feed;

import com.wilddev.image.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
public abstract class AbstractNewItemsFeed<T> implements NewItemsFeed<T> {

    protected final int batchSize;

    protected final ReservationService<T> reservationService;

    @Transactional
    @Override
    public List<T> stealNew() {

        List<T> items = reservationService.listNew(batchSize);

        if (items.isEmpty())
            return null;

        reservationService.reserve(items);

        return items;
    }
}

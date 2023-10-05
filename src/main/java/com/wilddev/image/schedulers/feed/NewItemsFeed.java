package com.wilddev.image.schedulers.feed;

import java.util.List;

public interface NewItemsFeed<T> {

    List<T> stealNew();
}

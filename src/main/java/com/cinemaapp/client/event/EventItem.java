package com.cinemaapp.client.event;

import com.cinemaapp.model.MovieModel;
import java.awt.Component;

public interface EventItem {

    public void itemClick(Component com, MovieModel item);
}

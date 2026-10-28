package com.example.SmartRestaurant.service.base;

public interface BaseService<Request, Response, Key> {
    Response create(Request request);

    Response update(Key id, Request request);

    void delete(Key id);

    Response getById(Key id);
}

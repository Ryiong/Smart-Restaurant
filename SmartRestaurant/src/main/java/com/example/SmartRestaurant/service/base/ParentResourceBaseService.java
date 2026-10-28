package com.example.SmartRestaurant.service.base;

public interface ParentResourceBaseService<Request, Response, Key> {
    Response create(Request request, Long parentId);

    Response update(Key id, Request request);

    void delete(Key id);

    Response getById(Key id);
}

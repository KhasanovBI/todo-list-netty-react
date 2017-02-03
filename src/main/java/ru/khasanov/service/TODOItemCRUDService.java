package ru.khasanov.service;

import org.jboss.logging.Logger;
import ru.khasanov.model.TODOItem;
import ru.khasanov.service.input.ToggleInput;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bulat on 18.01.17.
 */
@Path("/api/todo_item")
public class TODOItemCRUDService {
    private static Logger log = Logger.getLogger(TODOItemCRUDService.class);
    private AtomicLong idGenerator = new AtomicLong(1);
    private volatile Map<Long, TODOItem> database = new ConcurrentHashMap<>();

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response createTODOItem(TODOItem todoItem) {
        log.debug("POST /api/todo_item/ - " + todoItem);
        if (todoItem.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Don't set id.").build();
        }
        todoItem.setId(idGenerator.getAndIncrement());
        database.put(todoItem.getId(), todoItem);
        return Response.status(Response.Status.CREATED).entity(todoItem).build();
    }

    @GET
    @Path("/{id: \\d+}")
    @Produces("application/json")
    @Consumes("application/json")
    public TODOItem readTODOItem(@PathParam("id") long id) {
        log.debug("GET /api/todo_item/" + id);
        TODOItem todoItem = database.get(id);
        if (todoItem == null) {
            throw new NotFoundException();
        }
        return todoItem;
    }

    @PUT
    @Path("/{id: \\d+}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateTODOItem(@PathParam("id") long id, TODOItem requestTodoItem) {
        log.debug("PUT /api/todo_item/" + id + " - " + requestTodoItem);
        TODOItem todoItem = database.get(id);
        if (todoItem == null) {
            throw new NotFoundException();
        }
        if (!todoItem.getId().equals(requestTodoItem.getId())) {
            throw new BadRequestException();
        }
        todoItem.setTitle(requestTodoItem.getTitle());
        todoItem.setCompleted(requestTodoItem.isCompleted());
        return Response.status(Response.Status.OK).entity(todoItem).build();
    }

    @DELETE
    @Path("/{id: \\d+}")
    public Response deleteTODOItem(@PathParam("id") long id) {
        log.debug("DELETE /api/todo_item/" + id);
        if (database.containsKey(id)) {
            database.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        throw new NotFoundException();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Collection<TODOItem> listTODOItem() {
        log.debug("GET /api/todo_item/");
        return database.values();
    }

    @PUT
    @Path("/clear_completed")
    @Produces("application/json")
    public Collection<TODOItem> clearCompletedListTODOItem() {
        log.info("GET /api/todo_item/clear_comleted");
        database.entrySet().removeIf(entry -> entry.getValue().isCompleted());
        return database.values();
    }

    @PUT
    @Path("/toggle")
    @Produces("application/json")
    @Consumes("application/json")
    public Collection<TODOItem> toggleListTODOItem(ToggleInput toggleInput) {
        log.debug("GET /api/todo_item/toggle");
        database.values().parallelStream().forEach(todoItem -> todoItem.setCompleted(toggleInput.isCompleted()));
        return database.values();
    }
}

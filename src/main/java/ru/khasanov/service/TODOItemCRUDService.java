package ru.khasanov.service;

import org.jboss.logging.Logger;
import ru.khasanov.model.TODOItem;
import ru.khasanov.templater.PageGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bulat on 18.01.17.
 */
@Path("/api/todo_item")
public class TODOItemCRUDService {
    private static Logger log = Logger.getLogger(TODOItemCRUDService.class);
    private AtomicLong idGenerator = new AtomicLong(1);
    private volatile Map<Long, TODOItem> database = new HashMap<>();

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response readTODOItem(@PathParam("id") long id) {
        log.debug("GET /api/todo_item/" + id);
        TODOItem todoItem = database.get(id);
        if (todoItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(todoItem).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateTODOItem(@PathParam("id") long id, TODOItem requestTodoItem) {
        log.debug("PUT /api/todo_item/" + id + " - " + requestTodoItem);
        TODOItem todoItem = database.get(id);
        if (todoItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        todoItem.setTitle(requestTodoItem.getTitle());
        todoItem.setCompleted(requestTodoItem.isCompleted());
        return Response.status(Response.Status.OK).entity(todoItem).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTODOItem(@PathParam("id") long id) {
        log.debug("DELETE /api/todo_item/" + id);
        if (database.containsKey(id)) {
            database.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Response listTODOItem() {
        log.debug("GET /api/todo_item/");
        Map<String, Object> pageVars = new HashMap<>();
        pageVars.put("TODOItems", database.values());
        String page = PageGenerator.getPage("index.html", pageVars);
        return Response.status(Response.Status.OK).entity(page).build();
    }

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
}

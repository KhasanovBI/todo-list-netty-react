package ru.khasanov.service;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by bulat on 19.01.17.
 */
@Path("/")
public class IndexStaticService {
    private static final String STATIC_ROOT = "public_html";

    @GET
    @Path("")
    public Response getIndex(@Context UriInfo uriInfo) {
        return staticFile("index.html");
    }

    @GET
    @Path("/{name: .+}")
    public Response staticFile(@PathParam("name") String name) {
        java.nio.file.Path path = Paths.get(STATIC_ROOT, name);
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS) || Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            throw new NotFoundException();
        }
        File file = path.toFile();
        MediaType mediaType = getContentType(file, name);
        Response.ResponseBuilder response = Response.ok(file, mediaType);
        response.lastModified(new Date(file.lastModified()));
        return response.build();
    }

    private MediaType getContentType(File file, String name) {
        MediaType mediaType;
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException ignored) {
        }

        if (contentType == null) {
            if (name.endsWith(".json")) mediaType = MediaType.valueOf("application/json;charset=UTF-8");
            else if (name.endsWith(".js")) mediaType = MediaType.valueOf("application/javascript;charset=UTF-8");
            else if (name.endsWith(".css")) mediaType = MediaType.valueOf("text/css;charset=UTF-8");
            else mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
        } else {
            mediaType = MediaType.valueOf(contentType);
        }

        if (mediaType.getType().equals("text")) {
            mediaType = mediaType.withCharset("UTF-8");
        }
        return mediaType;
    }
}

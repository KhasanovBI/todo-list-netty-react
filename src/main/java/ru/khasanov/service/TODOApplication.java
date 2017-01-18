package ru.khasanov.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bulat on 18.01.17.
 */
@ApplicationPath("")
public class TODOApplication extends Application {

    private Set<Object> singletons = new HashSet<>();

    public TODOApplication() {
        singletons.add(new TODOItemCRUDService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}


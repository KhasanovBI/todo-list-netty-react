package ru.khasanov;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import ru.khasanov.service.TODOApplication;

public class Server {
    private static final int PORT = 8080;
    public static void main(String[] args) {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplication(new TODOApplication());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NettyJaxrsServer netty = new NettyJaxrsServer();
        netty.setDeployment(deployment);
        netty.setPort(PORT);
        netty.setSecurityDomain(null);
        netty.setRootResourcePath("");
        netty.start();
    }
}

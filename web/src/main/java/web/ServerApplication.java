package web;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("app")
public class ServerApplication extends ResourceConfig {

    public ServerApplication() {
        packages(this.getClass().getPackage().getName());
    }
}

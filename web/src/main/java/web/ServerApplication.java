package web;

import javax.servlet.http.HttpSession;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import web.common.factory.SessionFactory;

@ApplicationPath("api")
public class ServerApplication extends ResourceConfig {

    public ServerApplication() {
        packages(this.getClass().getPackage().getName());

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(SessionFactory.class).to(HttpSession.class);
            }
        });
    }
}

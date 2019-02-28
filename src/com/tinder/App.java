package com.tinder;

import com.tinder.DAO.DAO;
import com.tinder.Dto.User;
import com.tinder.Filters.FilterServletPostLogin;
import com.tinder.Filters.FilterServletPostRegister;
import com.tinder.Filters.LoginFilter;
import com.tinder.Service.UserService;
import com.tinder.Servlets.*;
import com.tinder.Utils.FreeMarker;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        FreeMarker template = new FreeMarker("./templates");
//        Connection conn = new DbConnection().connection();

//        UserService users = new UserService();
        List<User> users = new UserService().getAll();
//        initUsersList(users.getUserDao());

        Server server = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);

        handler.addServlet(AssetsServlet.class, "/assets/*");

        handler.addServlet(new ServletHolder(new LoginServlet(template)), "/login/*");
        handler.addServlet(new ServletHolder(new UsersServlet(users)), "/users");
        handler.addServlet(new ServletHolder(new LikedServlet(users)), "/liked");
        handler.addServlet(new ServletHolder(new MessagesServlet(users)), "/messages/*");
        handler.addServlet(new ServletHolder(new RedirectToServlet("/login")), "/*");

//        handler.addFilter(FilterServletPostRegister.class, "/register", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        handler.addFilter(LoginFilter.class, "/login", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        //handler.addFilter(FilterServletPostLogin.class, "/login", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        server.start();
        server.join();

    }

    private void initUsersList(DAO<User> userDao) {
        User user1 = new User(
                "diego@gmail.com",
                "Diego",
                "Maradona",
                "1",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Diego_Maradona_2012_2.jpg/800px-Diego_Maradona_2012_2.jpg"
        );
        User user2 = new User(
                "marco@gmail.com",
                "Marco",
                "van Basten",
                "1",
                "https://m.media-amazon.com/images/M/MV5BZjAxYTI3OGMtODM5Ni00M2MxLWFlNmMtYmI4NjIxYTVlODc2XkEyXkFqcGdeQXVyMjUyNDk2ODc@._V1_SY1000_CR0,0,797,1000_AL_.jpg"
        );

        User user3 = new User(
                "gabriel",
                "Gabriel",
                "Batistuta",
                "1",
                "https://abudhabiblog.com/wp-content/uploads/2015/05/batistuta-argentina-football.jpg"
        );

        User user4 = new User(
                "eric",
                "Eric",
                "Cantona",
                "1",
                "https://news.images.itv.com/image/file/991160/stream_img.jpg"
        );

        User user5 = new User(
                "oleg",
                "Oleg",
                "Blokhin",
                "1",
                "https://lvironpigs.files.wordpress.com/2011/12/1a1a1a1a1a1a1a283.jpg"
        );

        userDao.update(user1);
        userDao.update(user2);
        userDao.update(user3);
        userDao.update(user4);
        userDao.update(user5);

    }

}

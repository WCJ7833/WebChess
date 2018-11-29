package com.webchess.ui;

import com.webchess.appl.*;
import org.junit.jupiter.api.*;
import spark.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetSignOutRouteTest {

    private GetSignOutRoute CuT;
    private GameCenter gameCenter;
    private TemplateEngine engine;
    private Request request;
    private Session session;
    private Response response;

    @BeforeEach
    public void setup(){
        //gameCenter = mock(GameCenter.class);
        //templateEngine = mock(TemplateEngine.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameCenter = mock(GameCenter.class);
        // create a unique CuT for each test
        CuT = new GetSignOutRoute(engine);
    }

    /**
     * This test checks that the View component exists
     */
    @Test
    public void ViewModelIsaMap(){
        assertNotNull(CuT);
    }

    @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        gameCenter.newPlayerServices();

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute("title", "Sign Out");
        testHelper.assertViewModelAttribute(GetSignOutRoute.ALREADY_SIGNED_IN_ATTR, session.attribute(GetSignOutRoute.ALREADY_SIGNED_IN_ATTR));

        //   * test view name
        testHelper.assertViewName("signout.ftl");

    }
}

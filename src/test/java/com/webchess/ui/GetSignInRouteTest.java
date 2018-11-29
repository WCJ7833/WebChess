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
public class GetSignInRouteTest {

    private GetSignInRoute CuT;
    private TemplateEngine engine;
    private Request request;
    private Session session;
    private Response response;
    private GameCenter gameCenter;

    @BeforeEach
    public void setup(){
        gameCenter = new GameCenter();
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        // create a unique CuT for each test
        CuT = new GetSignInRoute(engine);
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

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute("title", "Sign In");

        //   * test view name
        testHelper.assertViewName("signin.ftl");

    }
}

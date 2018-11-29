package com.webchess.ui;
import static com.webchess.ui.GetHomeRoute.PLAYERSERVICES_KEY;
import static com.webchess.ui.GetHomeRoute.VIEW_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webchess.appl.*;
import com.webchess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

/**
 * This is a JUnit 5 test class for the UI tier GetHomeRoute class.
 * @author William Johnson wcj7833@rit.edu
 */
@Tag("UI-tier")
public class GetHomeRouteTest {

    private GetHomeRoute CuT;
    private GameCenter gameCenter;
    private TemplateEngine templateEngine;
    private Request request;
    private Session session;
    private Response response;

    /**
     * This test checks that the View component gets constructed properly.
     */
    @BeforeEach
    public void setup(){
        gameCenter = new GameCenter();
        templateEngine = mock(TemplateEngine.class);
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);

        // create a unique CuT for each test
        CuT = new GetHomeRoute(templateEngine,gameCenter);
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
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);
        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, "Welcome!");
        testHelper.assertViewModelAttribute(GetHomeRoute.ALREADY_SIGNED_IN_ATTR, false);
        testHelper.assertViewModelAttribute(GetHomeRoute.GAME_LOBBY, gameCenter.getPlayerLobby().PlayerCountString());
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENTUSER, null);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, null);
        testHelper.assertViewModelAttribute(GetHomeRoute.NEW_PLAYER_ATTR, true);

        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYERSERVICES_KEY), any(PlayerServices.class));
        verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
                any(SessionTimeoutWatchdog.class));
    }

    //@Test
    public void signedIn_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        PlayerServices playerServices = gameCenter.newPlayerServices();
        request.attribute(PLAYERSERVICES_KEY,playerServices);
        request.attribute(GetHomeRoute.ALREADY_SIGNED_IN_ATTR,true);

        playerServices.SignIn("MockPlayer");

        request.attribute(GetHomeRoute.CURRENTUSER,gameCenter.getPlayerLobby().getPlayer("MockPlayer"));
        System.out.println(request.attributes());
        // Invoke the test
        CuT.handle(request, response);
        // Analyze the results:

        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, "Welcome!");
        testHelper.assertViewModelAttribute(GetHomeRoute.ALREADY_SIGNED_IN_ATTR, true);
        testHelper.assertViewModelAttribute(GetHomeRoute.GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList("MockPlayer"));
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENTUSER, gameCenter.getPlayerLobby().getPlayer("MockPlayer"));
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, null);
        testHelper.assertViewModelAttribute(GetHomeRoute.NEW_PLAYER_ATTR, true);

        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYERSERVICES_KEY), any(PlayerServices.class));
        verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
                any(SessionTimeoutWatchdog.class));
    }

}


package com.webchess.ui;

import com.webchess.appl.*;
import org.junit.jupiter.api.*;
import spark.*;

import java.util.*;

import static com.webchess.ui.GetHomeRoute.PLAYERSERVICES_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetGameRouteTest {

    private GetGameRoute CuT;
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
        CuT = new GetGameRoute(engine,gameCenter);
    }

    /**
     * This test checks that the View component exists
     */
    @Test
    public void ViewModelIsaMap(){
        assertNotNull(CuT);
    }
    /**
     * Test that CuT redirects to the Game view when a @Linkplain(PlayerServices) object does
     * not exist, i.e. the session timed out or an illegal request on this URL was received.
     */
    @Test
    public void faulty_session() {
        // Arrange the test scenario: There is an existing session with a PlayerServices object
        when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(null);

        // Invoke the test
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.GAME_URL);
    }
   // @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        PlayerServices playerServices = gameCenter.newPlayerServices();
        request.attribute(PLAYERSERVICES_KEY,playerServices);
        request.attribute(GetHomeRoute.ALREADY_SIGNED_IN_ATTR,true);

        playerServices.SignIn("RedPlayer");
        playerServices.SignIn("WhitePlayer");
        gameCenter.getPlayerLobby().getPlayer("RedPlayer").setInGame(true);
        gameCenter.getPlayerLobby().getPlayer("WhitePlayer").setInGame(true);
        request.attribute(GetGameRoute.RED_PLAYER,gameCenter.getPlayerLobby().getPlayer("RedPlayer"));
        request.attribute(GetGameRoute.WHITE_PLAYER,gameCenter.getPlayerLobby().getPlayer("WhitePlayer"));

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute("title", GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR, "RED");
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE, "PLAY");

        //   * test view name
        testHelper.assertViewName(GetGameRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYERSERVICES_KEY), any(PlayerServices.class));
        verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
                any(SessionTimeoutWatchdog.class));
    }
}

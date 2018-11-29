package com.webchess.ui;

import static com.webchess.ui.PostHomeRoute.GAME_LOBBY;
import static com.webchess.ui.PostSignOutRoute.ALREADY_SIGNED_IN_ATTR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {

    private static final int NUMBER = 7;
    private static final int WRONG_GUESS = 3;
    private static final String WRONG_GUESS_STR = Integer.toString(WRONG_GUESS);
    private static final String NOT_A_NUMBER = "asdf";
    private static final int INVALID_GUESS = 47;
    private static final String INVALID_GUESS_STR = Integer.toString(INVALID_GUESS);

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GameCenter} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignOutRoute CuT;

    // friendly objects
    private GameCenter gameCenter;

    // attributes holding mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerServices playerSvc1;
    private PlayerServices playerSvc2;
    private Gson gson;
    private Player red;
    private Player white;
    private BoardView board;
    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        // build the Service and Model objects
        // the GameCenter and GuessingGame are friendly
        gameCenter = new GameCenter();
        gson = new Gson();
        // but mock up the PlayerService
        playerSvc1 = mock(PlayerServices.class);
        when(playerSvc1.getUsername()).thenReturn("Paul");

        playerSvc2 = mock(PlayerServices.class);
        when(playerSvc2.getUsername()).thenReturn("John");

        red = new Player("Paul");
        white = new Player("John");
        board = new BoardView(red,white,red);

        // store in the Session
        when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerSvc1);
        when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerSvc2);
        when(session.attribute(PostBackupMoveRoute.RED_PLAYER)).thenReturn(red);
        when(session.attribute(PostBackupMoveRoute.BOARD)).thenReturn(board);
        // create a unique CuT for each test
        CuT = new PostSignOutRoute(engine,gameCenter);
    }

    /**
     * Test that the "guess" action handled bad guesses: not a number
     */
    //@Test
    public void emptyMove() {
        // Arrange the test scenario: The user's guess is not valid number.

        //when(playerSvc1.getUsername()).thenReturn("Paul");
        //when(playerSvc2.getUsername()).thenReturn("John");
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
        testHelper.assertViewModelAttribute(
                GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
        //   * test view name
        testHelper.assertViewName(GetGameRoute.VIEW_NAME);
    }

    /**
     * Test win conditions.
     */
    //@Test
    public void successMove() {
        // Arrange the test scenario: The session holds a new game.
        when(request.queryParams(eq(PostBackupMoveRoute.BOARD))).thenReturn(String.valueOf(board));
        when(board.getLastMove()).thenReturn(new MoveAttempt(new Position(5,3),new Position(4,4),board));

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
        testHelper.assertViewModelAttribute(
                GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);

        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * Test that CuT redirects to the Home view when a @Linkplain(PlayerServices) object does
     * not exist, i.e. the session timed out or an illegal request on this URL was received.
     */
    @Test
    public void new_session() {
        // Arrange the test scenario: There is an existing session with a PlayerServices object
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute("title", "Sign Out");
        testHelper.assertViewModelAttribute(ALREADY_SIGNED_IN_ATTR, false);
        testHelper.assertViewModelAttribute(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerCountString());
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENTUSER, null);
        //   * test view name
        testHelper.assertViewName("signout.ftl");
    }
}

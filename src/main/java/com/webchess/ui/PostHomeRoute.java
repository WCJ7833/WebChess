package com.webchess.ui;

import com.google.gson.*;
import com.webchess.appl.GameCenter;
import com.webchess.appl.PlayerLobby;
import com.webchess.appl.PlayerServices;
import com.webchess.model.*;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webchess.ui.GetHomeRoute.MODE_OPTIONS;
import static spark.Spark.halt;
import static spark.Spark.webSocket;

/**
 * The {@code POST /Home} route handler.
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 */
public class PostHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostHomeRoute.class.getName());
    static final String GAME_LOBBY = "gameLobby";
    private final TemplateEngine templateEngine;
    static final String CURRENTUSER = "currentPlayer";
    static final String INDEX = "index";
    static final String COMPUTER = "computer";
    static final String REPLAY = "replay";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";
    static final String ERROR_TYPE = "error";
    static final String VIEW_NAME = "home.ftl";
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code POST /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostHomeRoute(final TemplateEngine templateEngine,final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
        //
        LOG.config("PostHomeRoute is initialized.");
    }
    /**
     * This is the method that returns the appropriate ModelAndView when the user makes invalid username entries
     * @param vm is the vm for the page
     * @param message is the message to apply to the error
     * @return the new ModelAndView with the new error included
     */
    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }
    static String makeInvalidArgMessage(final String userStr) {

        return String.format("The user, %s, is currently in a game with another user.", userStr);

    }

    /**
     * Updates the Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.finer("PostHomeRoute is invoked.");

        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Map<String, Object> vm = new HashMap<>();
        if (playerServices != null) {
            vm.put("title","Checkers Game");
            vm.put(GetHomeRoute.ALREADY_SIGNED_IN_ATTR,true);
            String i = request.queryParams(INDEX);
            String computer = request.queryParams(COMPUTER);
            String replay = request.queryParams(REPLAY);
            if (computer == null){
                computer = "false";
            }
            if (replay == null){
                replay = "false";
            }
            Player Red = httpSession.attribute(CURRENTUSER);
            if (replay.equals("true")) {
                HashMap<String, Object> modeOptions = new HashMap<>();
                modeOptions.put("hasNext",true);
                modeOptions.put("hasPrevious",false);
                Player White = new Player("william defoe");
                BoardView board =  new BoardView(Red, White, Red);
                Red.setBoard(board);
                Red.setTurn();
                httpSession.attribute(CURRENTUSER,Red);
                httpSession.attribute("redPlayer", Red);
                httpSession.attribute("whitePlayer", White);
                httpSession.attribute("viewMode", "REPLAY");
                httpSession.attribute("activeColor", "RED");
                httpSession.attribute("board",  board);
                vm.put(CURRENTUSER, Red);
                vm.put("redPlayer", Red);
                vm.put("whitePlayer", White);
                vm.put("viewMode", "REPLAY");
                vm.put("activeColor", "RED");
                vm.put("board", board);
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                gameCenter.getPlayerLobby().setSession(httpSession);
                response.redirect(WebServer.REPLAY_URL);
                return templateEngine.render(new ModelAndView(vm, GetReplayRoute.VIEW_NAME));
            }
            if (!computer.equals("true")) {
                Player White = gameCenter.getPlayerLobby().getPlayer(i);
                if (White.getInGame()) {
                    Player currentPlayer = httpSession.attribute(CURRENTUSER);
                    vm.put(CURRENTUSER, currentPlayer);
                    vm.put(GAME_LOBBY, httpSession.attribute(GAME_LOBBY));
                    return templateEngine.render(error(vm, makeInvalidArgMessage(White.getName())));
                } else {
                    White.setInGame(true);
                    Red.setInGame(true);
                    White.setColor(Piece.COLOR.WHITE);
                    Red.setColor(Piece.COLOR.RED);
                    BoardView board = new BoardView(Red, White, Red);
                    BoardView board2 = new BoardView(Red, White, White);
                    White.setBoard(board2);
                    Red.setBoard(board);
                    Red.setTurn();
                    White.clearTurn();
                    gameCenter.getReplayStorage().setPlayers(Red.getName(), White.getName());
                    httpSession.attribute("board", board);
                    httpSession.attribute("viewMode", "PLAY");
                    httpSession.attribute("redPlayer", Red);
                    httpSession.attribute("whitePlayer", White);
                    httpSession.attribute("currentPlayer", Red);
                    httpSession.attribute("activeColor", "RED");
                    Gson modeJson = httpSession.attribute(MODE_OPTIONS);
                    gameCenter.getPlayerLobby().setSession(httpSession);
                    vm.put(MODE_OPTIONS, modeJson);
                    vm.put("viewMode", "PLAY");
                    vm.put("board", board2);
                    vm.put("redPlayer", Red);
                    vm.put("whitePlayer", White);
                    vm.put("currentPlayer", White);
                    vm.put("activeColor", "RED");
                    response.redirect(WebServer.GAME_URL);
                    return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
                }
            }
            else {
                AI White = new AI("'Computer'");
                White.setInGame(true);
                Red.setInGame(true);
                White.setColor(Piece.COLOR.WHITE);
                Red.setColor(Piece.COLOR.RED);
                BoardView board = new BoardView(Red, White, Red);
                BoardView board2 = new BoardView(Red, White, White);
                White.setBoard(board2);
                Red.setBoard(board);
                Red.setTurn();
                White.clearTurn();
                httpSession.attribute("board", board);
                httpSession.attribute("viewMode", "PLAY");
                httpSession.attribute("redPlayer", Red);
                httpSession.attribute("whitePlayer", White);
                httpSession.attribute("currentPlayer", Red);
                httpSession.attribute("activeColor", "RED");
                Gson modeJson = httpSession.attribute(MODE_OPTIONS);
                gameCenter.getPlayerLobby().setSession(httpSession);
                vm.put(MODE_OPTIONS, modeJson);
                vm.put("viewMode", "PLAY");
                vm.put("board", board2);
                vm.put("redPlayer", Red);
                vm.put("whitePlayer", White);
                vm.put("currentPlayer", White);
                vm.put("activeColor", "RED");
                response.redirect(WebServer.GAME_URL);
                return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
            }
        }
        else{
            response.redirect(WebServer.GAME_URL);
            halt();
            return null;
        }
    }
}

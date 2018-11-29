package com.webchess.ui;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import spark.*;

import java.util.*;

import static com.webchess.ui.WebServer.GAME_URL;
import static spark.Spark.*;

/**
 * The {@code POST /checkTurn} route handler.
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */

public class PostCheckTurnRoute implements Route{
    // Values used in the view-model map for rendering the game view.
    static final String VIEW_NAME = "game.ftl";
    static final String CURRENT_PLAYER = "currentPlayer";
    static final String VIEW_MODE = "viewMode";
    static final String MODE_OPTIONS = "modeOptionsAsJSON";
    static final String RED_PLAYER = "redPlayer";
    static final String WHITE_PLAYER = "whitePlayer";
    static final String ACTIVE_COLOR = "activeColor";
    static final String BOARD = "board";
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final Gson gson;
    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostCheckTurnRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Checks to see if it is the current user's turn.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Game page
     */
    @Override
    public String handle(Request request, Response response) {
        // retrieve the game object and start one if no game is in progress
        final Session session = request.session();
        final PlayerServices playerServices =
                session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        final Map<String, Object> vm = new HashMap<>();
        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerServices != null) {
            Session httpSession = gameCenter.getPlayerLobby().getSession();
            Player white = httpSession.attribute(WHITE_PLAYER);
            Player red = httpSession.attribute(RED_PLAYER);
            Player currentPlayer = session.attribute(CURRENT_PLAYER);
            String activeColor = httpSession.attribute(ACTIVE_COLOR);
            if (activeColor.equals("RED") && currentPlayer.equals(red)) {
                return gson.toJson(new Message("true", Message.TYPE.info));
            }
            else if (activeColor.equals("WHITE") && currentPlayer.equals(white)) {
                return gson.toJson(new Message("true", Message.TYPE.info));
            }
            else if (!red.getInGame() || !white.getInGame()){
                return gson.toJson(new Message("true", Message.TYPE.info));
            }
            else {
                return gson.toJson(new Message("false", Message.TYPE.info));
            }
        }
        else {
            System.out.println("Redirect occurred");
            response.redirect(WebServer.GAME_URL);
            halt();
            return null;
        }
    }

}

package com.webchess.ui;

import com.google.gson.Gson;
import com.webchess.appl.GameCenter;
import com.webchess.appl.PlayerServices;
import com.webchess.model.*;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webchess.ui.GetGameRoute.*;
import static com.webchess.ui.PostBackupMoveRoute.ACTIVE_COLOR;
import static com.webchess.ui.PostBackupMoveRoute.RED_PLAYER;
import static com.webchess.ui.PostSubmitTurnRoute.BOARD;
import static spark.Spark.halt;
import static spark.Spark.setSecure;

/**
 * The {@code POST /resignGame} route handler.
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */
public class PostResignGameRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostResignGameRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Resigns current player from the game
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the appropriate message for resigning from the game
     */
    @Override
    public String handle(Request request, Response response) {
        // retrieve the game object and start one if no game is in progress
        final Session Session = request.session();
        final PlayerServices playerServices =
                Session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        final Map<String, Object> vm = new HashMap<>();
        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerServices != null) {
            Session httpSession = gameCenter.getPlayerLobby().getSession();
            Player currentPlayer = Session.attribute("currentPlayer");
            if (currentPlayer.isTurn()) {
                currentPlayer.setInGame(false);
                currentPlayer.setLost(true);
                String activeColor = httpSession.attribute(ACTIVE_COLOR);
                if (activeColor.equals("RED")){
                    activeColor = "WHITE";
                }
                else{
                    activeColor = "RED";
                }
                httpSession.attribute(ACTIVE_COLOR,activeColor);
                return gson.toJson(new Message("You have resigned the game.", Message.TYPE.info));
            }
            else{
                return gson.toJson(new Message("You cannot resign. It is not your turn.", Message.TYPE.error));
            }
        } else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;

        }
    }
}

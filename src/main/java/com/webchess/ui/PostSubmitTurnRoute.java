package com.webchess.ui;

import com.google.gson.Gson;
import com.webchess.appl.GameCenter;
import com.webchess.appl.PlayerServices;
import com.webchess.model.*;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webchess.ui.GetGameRoute.CURRENT_PLAYER;
import static com.webchess.ui.GetGameRoute.TITLE;
import static com.webchess.ui.WebServer.GAME_URL;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.setSecure;

/**
 * The {@code POST /submitTurn} route handler.
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 * @author <a href='bjs2864@rit.edu'>Brunon Sztuba</a>
 */
public class PostSubmitTurnRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final Gson gson;
    static final String VIEW_NAME = "game.ftl";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";
    static final String ERROR_TYPE = "error";
    static final String VIEW_MODE = "viewMode";
    static final String MODE_OPTIONS = "modeOptionsAsJSON";
    static final String RED_PLAYER = "redPlayer";
    static final String WHITE_PLAYER = "whitePlayer";
    static final String ACTIVE_COLOR = "activeColor";
    static final String BOARD = "board";
    /**
     * The constructor for the {@code POST /submitTurn} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostSubmitTurnRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Submits a turn for the game.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the appropriate message for submitting a turn
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
            String activeColor = httpSession.attribute(ACTIVE_COLOR);
            Player redPlayer = httpSession.attribute(RED_PLAYER);
            Player whitePlayer = httpSession.attribute(WHITE_PLAYER);
            BoardView board = httpSession.attribute(BOARD);
            Player player;
            if (activeColor.equals("WHITE")) {
                player = whitePlayer;
            }
            else{
                player = redPlayer;
            }
            BoardView boardView = player.getBoard();
            Move attempt = boardView.getLastMove();
            gameCenter.getReplayStorage().addTurn(attempt);
            Boolean isRegularMove = Math.abs((attempt.getStart().getRow()-attempt.getEnd().getRow()))==1;
            Piece piece = boardView.getPiece(boardView.getLastMove().getEnd().getRow(),boardView.getLastMove().getEnd().getCell());
            if (!boardView.jumpy(piece) || isRegularMove) {
                if(redPlayer.isTurn()){
                    whitePlayer.getBoard().getRules().checkStuck();
                }
                else{
                    redPlayer.getBoard().getRules().checkStuck();
                }
                boardView.toggleisTurn();
                whitePlayer.setMademoveAttempt(false);
                redPlayer.setMademoveAttempt(false);
                if (activeColor.equals("RED")) {
                    activeColor = "WHITE";
                } else {
                    activeColor = "RED";
                }
                if (activeColor.equals("WHITE") && whitePlayer.getName().equals("'Computer'")) {
                    AI ai = httpSession.attribute(WHITE_PLAYER);
                    ai.getBoard().getRules().checkStuck();
                    redPlayer.clearTurn();
                    ai.setTurn();
                    ai.go();
                    redPlayer.toggleTurn();
                    ai.toggleTurn();
                    if (!ai.lost()) {
                        player.getBoard().getRules().checkStuck();
                    }
                    ai.getBoard().setUpJumps();
                    activeColor = "RED";
                    httpSession.attribute(WHITE_PLAYER, ai);
                    httpSession.attribute(RED_PLAYER, redPlayer);
                    httpSession.attribute(ACTIVE_COLOR, activeColor);
                    httpSession.attribute(BOARD, redPlayer.getBoard());
                    return gson.toJson(new Message("The Computer has their finished your turn.", Message.TYPE.info));
                } else {
                    httpSession.attribute(WHITE_PLAYER, whitePlayer);
                    httpSession.attribute(RED_PLAYER, redPlayer);
                    httpSession.attribute(ACTIVE_COLOR, activeColor);
                    httpSession.attribute(BOARD, board);
                    return gson.toJson(new Message("You have finished your turn.", Message.TYPE.info));
                }
            }
            else {
                board.setForce(piece);
                player.setMademoveAttempt(false);
                httpSession.attribute(WHITE_PLAYER, whitePlayer);
                httpSession.attribute(RED_PLAYER, redPlayer);
                httpSession.attribute(ACTIVE_COLOR, activeColor);
                httpSession.attribute(BOARD, board);
                return gson.toJson(new Message("You can still jump with that piece.", Message.TYPE.info));
            }
        } else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}

package com.webchess.ui;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import spark.*;

import java.util.*;

import static com.webchess.ui.WebServer.GAME_URL;
import static spark.Spark.halt;

/**
 * The {@code POST /validateMove} route handler.
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 * @author <a href='bjs2864@rit.edu'>Brunon Sztuba</a>
 */

public class PostValidateMoveRoute implements Route{
    // Values used in the view-model map for rendering the game view.
    static final String VIEW_NAME = "game.ftl";
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
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostValidateMoveRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Validates the current users move
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
        final PlayerServices playerServices = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        final Map<String, Object> vm = new HashMap<>();
        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerServices != null) {
            Session httpSession = gameCenter.getPlayerLobby().getSession();
            Player White = httpSession.attribute(WHITE_PLAYER);
            Player Red = httpSession.attribute(RED_PLAYER);
            String active = httpSession.attribute(ACTIVE_COLOR);

            Player player;
            Player opponent;
            if (active == "WHITE") {
                player = White;
                opponent = Red;
            }
            else{
                player = Red;
                opponent = White;
            }
            httpSession.attribute(ACTIVE_COLOR,active);
            BoardView board1 = player.getBoard();
            BoardView board2 = opponent.getBoard();

            String UncheckedMoveStr = request.body();
            MoveAttempt moveAttempt = gson.fromJson(UncheckedMoveStr, MoveAttempt.class);
            MoveAttempt moveAttempt1 = new MoveAttempt(moveAttempt.getStart(),moveAttempt.getEnd(),board1);
            MoveAttempt moveAttempt2 = moveAttempt1.reverse(moveAttempt1,board2);
            List<Object> result=board1.isAttemptValid(moveAttempt1);
            System.out.println(player.alreadyMademoveAttempt());
            System.out.println(moveAttempt1);
            if (board1.allowed(moveAttempt1)&&!player.alreadyMademoveAttempt()){
                moveAttempt2.setReverse();
                board1.takeMove(moveAttempt1);
                board2.takeMove(moveAttempt2);
                player.setMademoveAttempt(true);
                player.setBoard(board1);
                opponent.setBoard(board2);
                player.getBoard().updatePieceList();
                opponent.getBoard().updatePieceList();
                if (player.equals(White)) {
                    httpSession.attribute(WHITE_PLAYER,player);
                    httpSession.attribute(RED_PLAYER,opponent);
                    return gson.toJson(new Message("Your move was valid", Message.TYPE.info));
                }
                else{
                    httpSession.attribute(WHITE_PLAYER,opponent);
                    httpSession.attribute(RED_PLAYER,player);

                    return gson.toJson(new Message("Your move was valid", Message.TYPE.info));
                }
            }
            else{
                httpSession.attribute(WHITE_PLAYER,White);
                httpSession.attribute(RED_PLAYER,Red);
                return gson.toJson(new Message((String) result.get(1), Message.TYPE.error));
            }



         } else {
            response.redirect(GAME_URL);
            halt();
            return null;

        }
    }

}


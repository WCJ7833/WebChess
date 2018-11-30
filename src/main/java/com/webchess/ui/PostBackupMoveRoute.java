package com.webchess.ui;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import spark.*;

import java.util.*;

import static com.webchess.ui.WebServer.GAME_URL;
import static spark.Spark.get;
import static spark.Spark.halt;

/**
 * The {@code POST /backup} route handler.
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */

public class PostBackupMoveRoute implements Route{
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
     * The constructor for the {@code POST /backup} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostBackupMoveRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }
    /**
     * Backs up the last move attempt
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the appropriate message for the request and response
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
            try {
                Session httpSession = gameCenter.getPlayerLobby().getSession();
                String activeColor = httpSession.attribute(ACTIVE_COLOR);
                Player player;
                Player opponent;
                if (activeColor.equals("RED")){
                    player = httpSession.attribute(RED_PLAYER);
                    opponent = httpSession.attribute(WHITE_PLAYER);
                }
                else{
                    player = httpSession.attribute(WHITE_PLAYER);
                    opponent = httpSession.attribute(RED_PLAYER);
                }

                BoardView board1 = player.getBoard();
                BoardView board2 = opponent.getBoard();
                MoveAttempt move1 = (MoveAttempt) board1.getLastMove();
                MoveAttempt backupMove1 = new MoveAttempt(move1.getEnd(), move1.getStart(),board1);
                if (Math.abs(backupMove1.getStart().getRow() - backupMove1.getEnd().getRow()) == 2){
                    Piece piece1 = board1.getLastRemovedPiece();
                    player.addPiece(piece1);
                    board1.addPiece(piece1);
                    Piece piece2 = board2.getLastRemovedPiece();
                    opponent.addPiece(piece2);
                    board2.addPiece(piece2);
                }

                MoveAttempt backupMove2 = backupMove1.reverse(backupMove1,board2);
                board1.getMoves().remove(move1);
                board2.getMoves().remove(board2.getLastMove());
                board1.implementMove(backupMove1);
                board2.implementMove(backupMove2);
                player.setBoard(board1);
                opponent.setBoard(board2);
                player.setMademoveAttempt(false);
                if (activeColor.equals("RED")){
                    httpSession.attribute(RED_PLAYER,player);
                    httpSession.attribute(WHITE_PLAYER,opponent);
                }
                else{
                    httpSession.attribute(WHITE_PLAYER,player);
                    httpSession.attribute(RED_PLAYER,opponent);
                }
                return gson.toJson(new Message("Your move was backed up", Message.TYPE.info));
            }
            catch (Error e){
                return gson.toJson(new Message("Your move wasn't backed up", Message.TYPE.error));
            }
        } else {
            response.redirect(GAME_URL);
            halt();
            return null;

        }
    }

}

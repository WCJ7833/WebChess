package com.webchess.ui;

import com.google.gson.Gson;
import com.webchess.appl.GameCenter;
import com.webchess.model.BoardView;
import com.webchess.model.Move;
import spark.*;
import java.util.Objects;
/**
 * The {@code POST /replay/previousTurn} route handler.
 *
 * @author <a href='mailto:gej@9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='mailto:smd6336@rit.edu'>Sean Dunn</a>
 */
public class PostReplayPreviousTurnRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    static final String TITLE = "Replay Mode";
    static final String VIEW_NAME = "game.ftl";
    static final String CURRENT_PLAYER = "currentPlayer";
    static final String VIEW_MODE = "viewMode";
    static final String MODE_OPTIONS = "modeOptionsAsJSON";
    static final String RED_PLAYER = "redPlayer";
    static final String WHITE_PLAYER = "whitePlayer";
    static final String ACTIVE_COLOR = "activeColor";
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final Gson gson;
    static boolean setup = true;

    /**
     * The constructor for the {@code POST /replay/previousTurn} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostReplayPreviousTurnRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Shows previous turn for replay
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the appropriate message for showing the previous replay turn
     */
    @Override
    public String handle(Request request, Response response) {
        // retrieve the game object and start one if no game is in progress
        final Session Session = gameCenter.getPlayerLobby().getSession();
        BoardView board =  Session.attribute("board");
        if (Session.attribute(ACTIVE_COLOR).equals("WHITE")) {
            Session.attribute(ACTIVE_COLOR,"RED");
        }
        else{
            Session.attribute(ACTIVE_COLOR,"WHITE");
        }
        int index = gameCenter.getReplayStorage().getIndex();
        Boolean hasPreviousTurn;
        Move move = null;
        if (index == (gameCenter.getReplayStorage().getSize()-1)) {
            hasPreviousTurn = true;
        }
        else if (index == 0){
            move = gameCenter.getReplayStorage().getMoves().get(0);
            hasPreviousTurn = false;
        }
        else{
            move = gameCenter.getReplayStorage().getNextMove();
            hasPreviousTurn = true;
        }
        if (hasPreviousTurn){
            board.takeMove(move);
            Session.attribute("board", board);
            return gson.toJson(new Message("true", Message.TYPE.info));

        }
        else{
            return gson.toJson(new Message("false", Message.TYPE.info));
        }
    }
}

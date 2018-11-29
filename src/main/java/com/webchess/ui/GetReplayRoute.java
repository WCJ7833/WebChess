package com.webchess.ui;

import com.google.gson.Gson;
import com.webchess.appl.GameCenter;
import com.webchess.model.BoardView;
import com.webchess.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * The {@code GET /replay/game} route handler.
 *
 * @author <a href='mailto:gej@9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='mailto:smd6336@rit.edu'>Sean Dunn</a>
 */
public class GetReplayRoute implements Route{
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

    GetReplayRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Render the WebCheckers Replay page.
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
        final Session Session = gameCenter.getPlayerLobby().getSession();
        final Map<String, Object> vm = new HashMap<>();
        final Map<String, Boolean> modeOptions = new HashMap<>();
        System.out.println("GetReplayGame is working");
        Player Red = Session.attribute(CURRENT_PLAYER);
        Player White;
        BoardView board;
        if (Session.attribute(WHITE_PLAYER) == null){
            White = new Player();
        }
        else{
            White = Session.attribute(WHITE_PLAYER);
        }
        if (Session.attribute("board") == null) {
            board = new BoardView(Red, White, Red);
        }
        else{
            board = Session.attribute("board");
        }
        int index = gameCenter.getReplayStorage().getIndex();
        if (index == (gameCenter.getReplayStorage().getSize()-1)){
            modeOptions.put("hasNext",false);
            modeOptions.put("hasPrevious",true);
        }
        else if (index == 0){
            modeOptions.put("hasNext",true);
            modeOptions.put("hasPrevious",false);
        }
        else{
            modeOptions.put("hasNext",true);
            modeOptions.put("hasPrevious",true);
        }
        if (Session.attribute(ACTIVE_COLOR) == null){
            vm.put(ACTIVE_COLOR, "RED");
            Session.attribute(ACTIVE_COLOR, "RED");
        }
        else{
            vm.put(ACTIVE_COLOR,Session.attribute(ACTIVE_COLOR));
        }
        vm.put("title",TITLE);
        vm.put("board", board);
        vm.put(VIEW_MODE, "REPLAY");
        vm.put(RED_PLAYER, Red);
        vm.put(WHITE_PLAYER, White);
        vm.put(CURRENT_PLAYER, Red);
        vm.put(MODE_OPTIONS, gson.toJson(modeOptions));
        //response.redirect(WebServer.REPLAY_URL);
        return templateEngine.render(new ModelAndView(vm, GetReplayRoute.VIEW_NAME));
    }
}

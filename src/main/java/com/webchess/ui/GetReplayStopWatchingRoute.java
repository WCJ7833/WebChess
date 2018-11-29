package com.webchess.ui;

import com.google.gson.Gson;
import com.webchess.appl.GameCenter;
import com.webchess.model.BoardView;
import com.webchess.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webchess.ui.GetHomeRoute.*;
/**
 * The {@code GET /replay/stopWatching} route handler.
 *
 * @author <a href='mailto:gej@9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='mailto:smd6336@rit.edu'>Sean Dunn</a>
 */
public class GetReplayStopWatchingRoute implements Route {
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

    GetReplayStopWatchingRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Render the ReplayStopWatching page.
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
        Player currentPlayer = Session.attribute(CURRENT_PLAYER);
        final Map<String, Object> vm = new HashMap<>();
        vm.put("title","Welcome!");
        vm.put(ALREADY_SIGNED_IN_ATTR, true);
        vm.put(MESSAGE_ATTR,null);
        vm.put(CURRENT_PLAYER, currentPlayer);
        vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(currentPlayer.getName()));
        vm.put(NEW_PLAYER_ATTR, true);
        response.redirect(WebServer.HOME_URL);
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}

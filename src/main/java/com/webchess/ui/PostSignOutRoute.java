package com.webchess.ui;

import com.webchess.appl.*;
import com.webchess.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webchess.ui.GetHomeRoute.GAME_LOBBY;

/**
 * The {@code POST /SignOut} route handler.
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */
public class PostSignOutRoute implements Route {
    static final String ALREADY_SIGNED_IN_ATTR = "signedIn";
    static final String CURRENT_PLAYER = "currentPlayer";
    static final String GAME_STATS_MSG_ATTR = "gameStatsMessage";
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code POST /signOut} HTTP request.
     *
     * @param templateEngine the HTML template rendering engine
     * @param gameCenter the Game Center for the application
     */
    public PostSignOutRoute(final TemplateEngine templateEngine, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        //
        this.gameCenter = gameCenter;
        LOG.config("PostSignOutRoute is initialized.");
    }

    /**
     * Signs the current user out of the game.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the SignIn page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSignOutRoute is invoked.");
        //
        final Session session = request.session();
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Sign Out");
        session.attribute(ALREADY_SIGNED_IN_ATTR, false);
        vm.put(ALREADY_SIGNED_IN_ATTR, false);
        Player currentPlayer = session.attribute(CURRENT_PLAYER);
        gameCenter.getPlayerLobby().removePlayerList(currentPlayer);
        vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerCountString());
        session.attribute(GAME_LOBBY,gameCenter.getPlayerLobby().PlayerCountString());
        response.redirect(WebServer.SIGNOUT_URL);
        vm.put(GetHomeRoute.CURRENTUSER, null);
        session.attribute(GetHomeRoute.CURRENTUSER, null);
        return templateEngine.render(new ModelAndView(vm, "signout.ftl"));
    }
}

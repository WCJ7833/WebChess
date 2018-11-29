package com.webchess.ui;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import spark.*;

import java.util.*;

import static com.webchess.ui.GetHomeRoute.*;
import static com.webchess.ui.PostHomeRoute.MESSAGE_TYPE_ATTR;
import static spark.Spark.halt;

/**
 * The {@code POST /game} route handler.
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */

public class PostGameRoute implements Route{
    // Values used in the view-model map for rendering the game view.
    static final String TITLE = "Checkers Game";
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

    /**
     * The constructor for the {@code POST /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    PostGameRoute(final TemplateEngine templateEngine, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    /**
     * Updates the game page
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
        final Session Session = request.session();
        final PlayerServices playerServices =
                Session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        final Map<String, Object> vm = new HashMap<>();
        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerServices != null) {
            Session httpSession = gameCenter.getPlayerLobby().getSession();

            Player currentPlayer = httpSession.attribute("currentPlayer");
            Player Red = httpSession.attribute("redPlayer");
            Player White = httpSession.attribute("whitePlayer");
            //Resignation
            if (Red.isLost()){
                vm.put(TITLE_ATTR, "Welcome!");
                vm.put(ALREADY_SIGNED_IN_ATTR, true);
                vm.put(GetHomeRoute.CURRENTUSER, currentPlayer);
                if (currentPlayer.equals(Red)) {
                    vm.put(MESSAGE_TYPE_ATTR, new Message("You lost the game against " + White.getName(), Message.TYPE.info));
                }
                else{
                    vm.put(MESSAGE_TYPE_ATTR, new Message("You beat " + White.getName(), Message.TYPE.info));
                }
                vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(currentPlayer.getName()));
                response.redirect(WebServer.GAME_URL);
                halt();
                return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
            }
            else {
                vm.put(TITLE_ATTR, "Welcome!");
                vm.put(ALREADY_SIGNED_IN_ATTR, true);
                vm.put(GetHomeRoute.CURRENTUSER, currentPlayer);
                if (currentPlayer.equals(White)) {
                    vm.put(MESSAGE_TYPE_ATTR, new Message("You lost the game against " + Red.getName(), Message.TYPE.info));
                }
                else{
                    vm.put(MESSAGE_TYPE_ATTR, new Message("You beat " + Red.getName(), Message.TYPE.info));
                }
                vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(currentPlayer.getName()));
                response.redirect(WebServer.HOME_URL);
                halt();
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));

            }



        } else {
            response.redirect(WebServer.GAME_URL);
            halt();
            return null;

        }
    }

}

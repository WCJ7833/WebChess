package com.webchess.ui;


import com.webchess.appl.GameCenter;
import com.webchess.appl.PlayerServices;
import com.webchess.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import spark.ModelAndView;

import static spark.Spark.halt;
import java.util.regex.Pattern;

/**
 * The {@code POST /SignIn} route handler.
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */
public class PostSignInRoute implements Route {

    private final TemplateEngine templateEngine;
    static final String USERNAME = "UserName";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";
    static final String ERROR_TYPE = "error";
    static final String INFO_TYPE = "info";
    static final String VIEW_NAME = "signin.ftl";
    private final GameCenter gameCenter;

    /**
     * This is the constructor for PostSignInRoute
     * @param templateEngine is the TemplateEngine for the page
     * @param gameCenter is the GameCenter of the server
     */
    PostSignInRoute(TemplateEngine templateEngine, final GameCenter gameCenter){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    /**
     * This is the message to send to the user if they enter an invalid username
     * @param userStr is the string the user entered
     * @param value is a determining value that informs the method if they had improperly formatted name
     *              or if they entered an already used username
     * @return an error string
     */
    static String makeInvalidArgMessage(final String userStr,int value) {
        if (value == 2) {
            return String.format("You entered %s; only use alphanumeric characters and spaces.", userStr);
        }
        else{
            return String.format("You entered %s; this username is already in use.", userStr);
        }
    }

    /**
     * This is the method that returns the appropriate ModelAndView when the user makes invalid username entries
     * @param vm is the vm for the page
     * @param message is the message to apply to the error
     * @return the new ModelAndView with the new error included
     */
    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }

    /**
     * This is the handle method for the Post/SignIn route
     * @param request is the request associated with the Post
     * @param response is the response associated with the Post
     * @return the rendered HTML for the Home page
     */
    @Override
    public String handle(Request request, Response response){
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR,"Welcome!");
        vm.put(GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);

        final Session session = request.session();
        final PlayerServices playerServices = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        if (playerServices != null){
            String UncheckedUserStr = request.queryParams(USERNAME);
            int check = playerServices.SignIn(UncheckedUserStr);
            if (check == 0){
                session.attribute(GetHomeRoute.ALREADY_SIGNED_IN_ATTR, true);
                session.attribute(GetHomeRoute.CURRENTUSER, gameCenter.getPlayerLobby().getPlayer(UncheckedUserStr));
                response.redirect(WebServer.HOME_URL);
                vm.put(GetHomeRoute.ALREADY_SIGNED_IN_ATTR,true);
                vm.put(GetHomeRoute.GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(UncheckedUserStr));
                vm.put(GetHomeRoute.CURRENTUSER, gameCenter.getPlayerLobby().getPlayer(UncheckedUserStr));
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
            else{
                return templateEngine.render(error(vm,makeInvalidArgMessage(UncheckedUserStr,check)));
            }
        }
        else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
    /*
    private ModelAndView signedIn(final boolean IsSignedIn, final Map<String, Object> vm, final PlayerServices playerServices){
        vm.put("signedIn",IsSignedIn);
        return new ModelAndView(vm)
    }
    */
}

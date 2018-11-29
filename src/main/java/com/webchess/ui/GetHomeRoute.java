package com.webchess.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.*;
import com.webchess.appl.*;
import com.webchess.model.*;
import spark.*;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page .
 *
 * @author <a href='mailto:wcj@rit.edu'>William Johnson</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
  static final String GAME_LOBBY = "gameLobby";
  static final String TITLE_ATTR = "title";
  static final String NEW_PLAYER_ATTR = "newPlayer";
  static final String ALREADY_SIGNED_IN_ATTR = "signedIn";
  static final String MESSAGE_ATTR = "message";
  static final String VIEW_NAME = "home.ftl";
  static final String MODE_OPTIONS = "modeOptionsAsJSON";
    private final GameCenter gameCenter;
  private final TemplateEngine templateEngine;
  static final String CURRENTUSER = "currentPlayer";
  // Key in the session attribute map for the player who started the session
  static final String PLAYERSERVICES_KEY = "playerServices";
  static final String TIMEOUT_SESSION_KEY = "timeoutWatchdog";

  // The length of the session timeout in seconds
  static final int SESSION_TIMEOUT_PERIOD = 120;

  /**
   * Create the Spark Route (UI controller) for the
   * {@code GET /} HTTP request.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine,final GameCenter gameCenter) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gameCenter, "gameCenter must not be null");
    //
    this.templateEngine = templateEngine;
    this.gameCenter = gameCenter;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public String handle(Request request, Response response) {
    final Session httpSession = request.session();

    LOG.finer("GetHomeRoute is invoked.");

    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, "Welcome!");
    if (httpSession.attribute(ALREADY_SIGNED_IN_ATTR) == null || !((Boolean) httpSession.attribute(ALREADY_SIGNED_IN_ATTR))) {
      httpSession.attribute(ALREADY_SIGNED_IN_ATTR, false);
      vm.put(ALREADY_SIGNED_IN_ATTR, false);
      vm.put(CURRENTUSER, null);
      vm.put(MESSAGE_ATTR,null);

      vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerCountString());
    }
    else {
      vm.put(ALREADY_SIGNED_IN_ATTR, true);
      Player CurrentPlayer = httpSession.attribute(CURRENTUSER);
      vm.put(MESSAGE_ATTR,null);
      vm.put(CURRENTUSER,CurrentPlayer);
      vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(CurrentPlayer.getName()));
      httpSession.attribute(GAME_LOBBY,gameCenter.getPlayerLobby().PlayerList(CurrentPlayer.getName()));

    }
    Player CurrentPlayer = httpSession.attribute(CURRENTUSER);
    if (CurrentPlayer != null){
        if (gameCenter.getPlayerLobby().getPlayer(CurrentPlayer.getName()).getInGame()) {
            Session currentSession = gameCenter.getPlayerLobby().getSession();
            String viewMode = currentSession.attribute("viewMode");
            Player Red = currentSession.attribute("redPlayer");
            Player White = currentSession.attribute("whitePlayer");
            Player currentPlayer = currentSession.attribute("currentPlayer");
            String activeColor = currentSession.attribute("activeColor");
            Gson gson = currentSession.attribute(MODE_OPTIONS);
            BoardView board = new BoardView(Red, White, White);
            currentSession.attribute("board", board);
            CurrentPlayer.setInGame(true);
            vm.put("viewMode", viewMode);
            vm.put("board", board);
            vm.put("redPlayer", Red);
            vm.put("whitePlayer", White);
            vm.put(CURRENTUSER, currentPlayer);
            vm.put("activeColor", activeColor);
            vm.put(MODE_OPTIONS,gson);
            response.redirect(WebServer.GAME_URL);
            return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
        }
    }
    vm.put(NEW_PLAYER_ATTR, true);
    if(httpSession.attribute(PLAYERSERVICES_KEY) == null) {
      final PlayerServices playerService = gameCenter.newPlayerServices();
      httpSession.attribute(PLAYERSERVICES_KEY, playerService);
      httpSession.attribute(TIMEOUT_SESSION_KEY, new SessionTimeoutWatchdog(playerService));
      httpSession.maxInactiveInterval(SESSION_TIMEOUT_PERIOD);
      return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
    else {
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
  }

}
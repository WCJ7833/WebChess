

        package com.webchess.ui;

        import java.io.*;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Objects;

        import com.webchess.appl.GameCenter;
        import com.webchess.model.*;
        import spark.ModelAndView;
        import spark.Request;
        import spark.Response;
        import spark.Route;
        import spark.Session;
        import spark.TemplateEngine;

        import static com.webchess.ui.GetHomeRoute.*;
        import static com.webchess.ui.GetHomeRoute.MESSAGE_ATTR;
        import static com.webchess.ui.PostHomeRoute.CURRENTUSER;
        import static com.webchess.ui.PostHomeRoute.MESSAGE_TYPE_ATTR;
        import static spark.Spark.halt;
        import static spark.Spark.redirect;

        import com.webchess.appl.PlayerServices;

/**
 * The {@code GET /game} route handler.
 *
 * @author <a href='mailto:gej@9887@rit.edu'>Gabriel Jusino</a>
 * @author <a href='mailto:wcj7833@rit.edu'>William Johnson</a>
 */
public class GetGameRoute implements Route {

    // Values used in the view-model map for rendering the game view.
    static final String TITLE = "Checkers Game";
    static final String VIEW_NAME = "game.ftl";
    static final String CURRENT_PLAYER = "currentPlayer";
    static final String VIEW_MODE = "viewMode";
    static final String MODE_OPTIONS = "modeOptionsAsJSON";
    static final String RED_PLAYER = "redPlayer";
    static final String WHITE_PLAYER = "whitePlayer";
    static final String ACTIVE_COLOR = "activeColor";
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    static boolean setup = true;
    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    GetGameRoute(final TemplateEngine templateEngine, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    /**
     * Render the WebCheckers Game page.
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
        if (playerServices != null) {
            Session httpSession = gameCenter.getPlayerLobby().getSession();
            Player Red = httpSession.attribute("redPlayer");
            Player White = httpSession.attribute("whitePlayer");
            Player currentPlayer = Session.attribute("currentPlayer");
            int RedCount = Red.getBoard().getPieceCount(Red);
            int WhiteCount = White.getBoard().getPieceCount(White);
            if (RedCount == 0){
                White.setInGame(false);
                Red.setInGame(false);
                Red.setLost(true);
            }
            else if (WhiteCount == 0) {
                White.setInGame(false);
                Red.setInGame(false);
                White.setLost(true);
            }
            if (Red.isLost()){
                White.setInGame(false);
                Red.setInGame(false);
                vm.put(TITLE_ATTR, "Welcome!");
                vm.put(ALREADY_SIGNED_IN_ATTR, true);
                vm.put(GetHomeRoute.CURRENTUSER, currentPlayer);
                if (currentPlayer.equals(Red)) {
                    vm.put(MESSAGE_ATTR, "You lost the game against " + White.getName());
                }
                else{
                    vm.put(MESSAGE_ATTR, "You beat " + White.getName());
                }
                vm.put(MESSAGE_TYPE_ATTR,"info");
                vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(currentPlayer.getName()));
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
            }
            else if (White.isLost()){
                System.out.println(White.getName());
                System.out.println(currentPlayer.getName());
                Red.setInGame(false);
                White.setInGame(false);
                vm.put(TITLE_ATTR, "Welcome!");
                vm.put(ALREADY_SIGNED_IN_ATTR, true);
                vm.put(GetHomeRoute.CURRENTUSER, currentPlayer);
                if (currentPlayer.equals(White)) {
                    vm.put(MESSAGE_ATTR, "You lost the game against " + Red.getName());
                }
                else{
                    vm.put(MESSAGE_ATTR, "You beat " + Red.getName());
                }
                vm.put(MESSAGE_TYPE_ATTR,"info");
                vm.put(GAME_LOBBY, gameCenter.getPlayerLobby().PlayerList(currentPlayer.getName()));
                return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));

            }
            else {
                vm.put("title", "Checkers Game");
                String viewMode = httpSession.attribute("viewMode");
                String activeColor = httpSession.attribute("activeColor");
                String modeJson = httpSession.attribute(MODE_OPTIONS);
                if (currentPlayer.equals(Red)){
                    vm.put("board",Red.getBoard());
                }
                else {
                    vm.put("board",White.getBoard());
                }
                vm.put("viewMode", viewMode);
                vm.put(MODE_OPTIONS, modeJson);
                vm.put("redPlayer", Red);
                vm.put("whitePlayer", White);
                vm.put("currentPlayer", currentPlayer);
                vm.put(ACTIVE_COLOR, activeColor);
                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
            }
        } else {
            response.redirect(WebServer.GAME_URL);
            halt();
            return null;

        }
    }
}

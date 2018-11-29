package com.webchess.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

/**
 * The UI Controller to GET the SignOut page.
 *
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 */
public class GetSignOutRoute implements Route {

    static final String ALREADY_SIGNED_IN_ATTR = "signedIn";
    private static final Logger LOG = Logger.getLogger(GetSignOutRoute.class.getName());

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /signout} HTTP request.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetSignOutRoute(final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        //
        LOG.config("GetSignOutRoute is initialized.");
    }

    /**
     * Render the WebCheckers SignOut page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the SignIn page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignOutRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Sign Out");
        final Session httpSession = request.session();
        Boolean signedIn = httpSession.attribute(ALREADY_SIGNED_IN_ATTR);
        vm.put(ALREADY_SIGNED_IN_ATTR, signedIn);
        return templateEngine.render(new ModelAndView(vm, "signout.ftl"));
    }
}

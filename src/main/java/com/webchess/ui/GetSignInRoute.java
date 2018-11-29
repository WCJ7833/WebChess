package com.webchess.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller to GET the SignIn page.
 *
 * @author <a href='gej9887@rit.edu'>Gabriel Jusino</a>
 */
public class GetSignInRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /signin} HTTP request.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Signin page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the SignIn page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Sign In");
        return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
    }
}

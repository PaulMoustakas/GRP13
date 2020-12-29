
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */

public class APIRunner {
    public static void main(String[] args) {
        port(3000);
//        staticFiles.location("/public");
//
//
//        get("/front", (request, response) -> {
//            return new PebbleTemplateEngine().render (
//                    new ModelAndView(null, "/templates/index.htlm"));
//        });

        get("/:country", (req,res)->{
            return "Hello, "+ req.params(":country");
        });
    }
}


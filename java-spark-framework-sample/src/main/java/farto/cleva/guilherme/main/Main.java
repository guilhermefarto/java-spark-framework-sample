package farto.cleva.guilherme.main;

import java.text.MessageFormat;
import java.util.Arrays;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;
import spark.Spark;

public class Main {

	// http://sparkjava.com/documentation

	public static void main(String[] args) {
		try {

			Spark.port(8181);

			Spark.notFound(new Route() {

				@Override
				public Object handle(Request req, Response res) throws Exception {
					res.type("application/json");

					return "{\"message\":\"Custom 404\"}";
				}
			});

			Spark.internalServerError(new Route() {

				@Override
				public Object handle(Request req, Response res) throws Exception {
					res.type("application/json");

					return "{\"message\":\"Custom 500 handling\"}";
				}
			});

			Spark.before("/*", new Filter() {

				@Override
				public void handle(Request q, Response a) throws Exception {
					System.out.println(q.url());
				}
			});

			Spark.before("/protected/*", (request, response) -> {
				Spark.halt(401, "Go away!");
			});

			Spark.after(new Filter() {

				@Override
				public void handle(Request request, Response response) throws Exception {
					response.header("Content-Encoding", "gzip");
					response.header("foo", "set by after filter");
				}
			});

			Spark.get("/hello", new Route() {

				@Override
				public Object handle(Request req, Response res) throws Exception {
					return "Hello World";
				}
			});

			Spark.get("/hello/:name", new Route() {

				@Override
				public Object handle(Request request, Response response) throws Exception {
					return MessageFormat.format("Hello {0}", request.params(":name"));
				}
			});

			Spark.get("/say/*/to/*", new Route() {

				@Override
				public Object handle(Request request, Response response) throws Exception {
					return "Number of splat parameters: " + request.splat().length + " - " + Arrays.toString(request.splat());
				}
			});

			Spark.options("/", new Route() {

				@Override
				public Object handle(Request request, Response response) throws Exception {
					return "Sample OPTIONS request";
				}
			});

			Spark.path("/api", new RouteGroup() {

				@Override
				public void addRoutes() {
					Spark.path("/email", new RouteGroup() {

						@Override
						public void addRoutes() {
							Spark.get("/list", new Route() {

								@Override
								public Object handle(Request q, Response a) throws Exception {
									return "/list";
								}
							});

							Spark.get("/all", new Route() {

								@Override
								public Object handle(Request q, Response a) throws Exception {
									a.redirect("/api/email/list");

									// Spark.redirect.get("/fromPath", "/toPath");
									// Spark.redirect.post("/fromPath", "/toPath", Redirect.Status.SEE_OTHER);
									// Spark.redirect.any("/fromPath", "/toPath", Redirect.Status.MOVED_PERMANENTLY);

									return null;
								}
							});

							Spark.post("/add", new Route() {

								@Override
								public Object handle(Request req, Response res) throws Exception {
									return "/add";
								}
							});

							Spark.put("/change", new Route() {

								@Override
								public Object handle(Request req, Response res) throws Exception {
									return "/add";
								}
							});

							Spark.delete("/remove", new Route() {

								@Override
								public Object handle(Request req, Response res) throws Exception {
									return "/add";
								}
							});
						}
					});

					Spark.path("/username", new RouteGroup() {

						@Override
						public void addRoutes() {
							Spark.get("/list", new Route() {

								@Override
								public Object handle(Request q, Response a) throws Exception {
									return "/list";
								}
							});

							Spark.post("/add", new Route() {

								@Override
								public Object handle(final Request req, final Response res) throws Exception {
									return "/add";
								}
							});

							Spark.put("/change", new Route() {

								@Override
								public Object handle(final Request req, final Response res) throws Exception {
									return "/add";
								}
							});

							Spark.delete("/remove", new Route() {

								@Override
								public Object handle(final Request req, final Response res) throws Exception {
									return "/add";
								}
							});
						}
					});
				}
			});

			Spark.awaitInitialization();

			System.out.println("##########");
			System.out.println("Spark Framework initialized...");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ###################
	// ##### REQUEST #####
	// ###################

	//	request.attributes();             // the attributes list
	//	request.attribute("foo");         // value of foo attribute
	//	request.attribute("A", "V");      // sets value of attribute A to V
	//	request.body();                   // request body sent by the client
	//	request.bodyAsBytes();            // request body as bytes
	//	request.contentLength();          // length of request body
	//	request.contentType();            // content type of request.body
	//	request.contextPath();            // the context path, e.g. "/hello"
	//	request.cookies();                // request cookies sent by the client
	//	request.headers();                // the HTTP header list
	//	request.headers("BAR");           // value of BAR header
	//	request.host();                   // the host, e.g. "example.com"
	//	request.ip();                     // client IP address
	//	request.params("foo");            // value of foo path parameter
	//	request.params();                 // map with all parameters
	//	request.pathInfo();               // the path info
	//	request.port();                   // the server port
	//	request.protocol();               // the protocol, e.g. HTTP/1.1
	//	request.queryMap();               // the query map
	//	request.queryMap("foo");          // query map for a certain parameter
	//	request.queryParams();            // the query param list
	//	request.queryParams("FOO");       // value of FOO query param
	//	request.queryParamsValues("FOO")  // all values of FOO query param
	//	request.raw();                    // raw request handed in by Jetty
	//	request.requestMethod();          // The HTTP method (GET, ..etc)
	//	request.scheme();                 // "http"
	//	request.servletPath();            // the servlet path, e.g. /result.jsp
	//	request.session();                // session management
	//	request.splat();                  // splat (*) parameters
	//	request.uri();                    // the uri, e.g. "http://example.com/foo"
	//	request.url();                    // the url. e.g. "http://example.com/foo"
	//	request.userAgent();              // user agent

	// ####################
	// ##### RESPONSE #####
	// ####################

	//	response.body();               // get response content
	//	response.body("Hello");        // sets content to Hello
	//	response.header("FOO", "bar"); // sets header FOO with value bar
	//	response.raw();                // raw response handed in by Jetty
	//	response.redirect("/example"); // browser redirect to /example
	//	response.status();             // get the response status
	//	response.status(401);          // set status code to 401
	//	response.type();               // get the content type
	//	response.type("text/xml");     // set content type to text/xml

	// ######################
	// ##### QUERY MAPS #####
	// ######################

	//	request.queryMap().get("user", "name").value();
	//	request.queryMap().get("user").get("name").value();
	//	request.queryMap("user").get("age").integerValue();
	//	request.queryMap("user").toMap();

	// ###################
	// ##### COOKIES #####
	// ###################

	//	request.cookies();                         // get map of all request cookies
	//	request.cookie("foo");                     // access request cookie by name
	//	response.cookie("foo", "bar");             // set cookie with a value
	//	response.cookie("foo", "bar", 3600);       // set cookie with a max-age
	//	response.cookie("foo", "bar", 3600, true); // secure cookie
	//	response.removeCookie("foo");              // remove cookie

	// ###################
	// ##### SESSIONS ####
	// ###################

	//	request.session(true);                     // create and return session
	//	request.session().attribute("user");       // Get session attribute 'user'
	//	request.session().attribute("user","foo"); // Set session attribute 'user'
	//	request.session().removeAttribute("user"); // Remove session attribute 'user'
	//	request.session().attributes();            // Get all session attributes
	//	request.session().id();                    // Get session id
	//	request.session().isNew();                 // Check if session is new
	//	request.session().raw();                   // Return servlet object

	// ###################
	// ####### HALT ######
	// ###################

	//	halt();                // halt 
	//	halt(401);             // halt with status
	//	halt("Body Message");  // halt with message
	//	halt(401, "Go away!"); // halt with status and message

}

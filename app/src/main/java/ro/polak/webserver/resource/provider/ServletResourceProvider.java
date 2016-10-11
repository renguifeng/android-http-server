/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2008-2016
 **************************************************/

package ro.polak.webserver.resource.provider;

import java.io.IOException;

import ro.polak.utilities.Utilities;
import ro.polak.webserver.Headers;
import ro.polak.webserver.HttpResponseHeaders;
import ro.polak.webserver.controller.MainController;
import ro.polak.webserver.error.HttpError500;
import ro.polak.webserver.servlet.HttpRequestWrapper;
import ro.polak.webserver.servlet.HttpResponseWrapper;
import ro.polak.webserver.servlet.HttpSessionWrapper;
import ro.polak.webserver.servlet.Servlet;
import ro.polak.webserver.servlet.ServletConfigWrapper;
import ro.polak.webserver.servlet.ServletContextWrapper;
import ro.polak.webserver.servlet.loader.ClassPathServletLoader;
import ro.polak.webserver.servlet.loader.ServletLoader;
import ro.polak.webserver.session.storage.FileSessionStorage;

/**
 * Servlet resource provider
 * <p/>
 * This provider enables the URLs to be interpreted by servlets
 *
 * @author Piotr Polak piotr [at] polak [dot] ro
 * @since 201509
 */
public class ServletResourceProvider implements ResourceProvider {

    // Initialize servlet service in a static way
    private static ServletLoader servletService;
    private static ServletContextWrapper servletContext;

    static {
        String tmpPath = MainController.getInstance().getWebServer().getServerConfig().getTempPath();
        servletService = new ro.polak.webserver.servlet.ServletLoader(new ClassPathServletLoader());
        servletContext = new ServletContextWrapper(new FileSessionStorage(tmpPath));
    }

    @Override
    public boolean load(String uri, HttpRequestWrapper request, HttpResponseWrapper response) {

        String extension = Utilities.getExtension(uri);

        // Check whether the extension is of Servlet type
        if (extension.equals(MainController.getInstance().getWebServer().getServerConfig().getServletMappedExtension())) {
            try {
                ServletConfigWrapper servletConfig = new ServletConfigWrapper();
                request.setServletContext(servletContext);
                servletConfig.setServletContext(servletContext);

                Servlet servlet = servletService.loadServlet(uri);
                servlet.init(servletConfig);
                response.setStatus(HttpResponseHeaders.STATUS_OK);
                servlet.service(request, response);
                this.terminate(request, response);
            } catch (Exception e) {
                HttpError500 error500 = new HttpError500();
                error500.setReason(e);
                error500.serve(response);
                e.printStackTrace();
            } catch (Error e) {
                // For compilation problems
                HttpError500 error500 = new HttpError500();
                error500.setReason(e);
                error500.serve(response);
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    /**
     * Terminates servlet
     * <p/>
     * Sets all necessary headers, flushes content
     */
    private void terminate(HttpRequestWrapper request, HttpResponseWrapper response) throws IOException {
        request.getFileUpload().freeResources();

        if (!response.isCommitted()) {
            if (response.getContentType() == null) {
                response.setContentType("text/html");
            }

            if (response.getPrintWriter().isInitialized()) {
                response.setContentLength(response.getPrintWriter().length());
            }

            response.getHeaders().setHeader(Headers.HEADER_CACHE_CONTROL, "no-cache");
            response.getHeaders().setHeader(Headers.HEADER_PRAGMA, "no-cache");

            HttpSessionWrapper session = request.getSession(false);
            if (session != null) {
                try {
                    servletContext.handleSession(session, response);
                } catch (IOException e) {
                    MainController.getInstance().println(this.getClass(), "Unable to persist session: " + e.getMessage());
                }
            }


            response.flushHeaders();
        }

        response.write(response.getPrintWriter().toString());

        try {
            response.flush();
        } catch (Exception e) {
        }
    }
}

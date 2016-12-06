package org.zkoss.zats.mimic.impl.emulator;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.zkoss.zul.South;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Emulator in Spring Boot Test environnement
 */
@Component
public class SpringBootEmulator implements Emulator {

    private String host;
    private int port;
    private String address;
    private String contextPath;
    private ServletContext servletContext;
    private Map<String, Object> requestAttributes;
    private Map<String, String[]> requestParameters;
    private Map<String, Object> sessionAttributes;
    private String sessionId;

    public String getHost() {
        return "localhost";
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        if (address == null) {
            String cp = getContextPath();
            if (cp.endsWith("/")) {//
                cp = cp.substring(0, cp.length() - 1);
            }
            address = MessageFormat.format("http://{0}:{1,number,#}{2}", getHost(), getPort(), cp);
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContextPath() {
        return this.servletContext.getContextPath();
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Map<String, Object> getRequestAttributes() {
            return requestAttributes;
    }

    public void setRequestAttributes(Map<String, Object> requestAttributes) {
        this.requestAttributes = requestAttributes;
    }

    public Map<String, String[]> getRequestParameters() {
            return requestParameters;
    }

    public void setRequestParameters(Map<String, String[]> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public Map<String, Object> getSessionAttributes() {
            return sessionAttributes;
    }

    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    public String getSessionId() {
            return RequestContextHolder.getRequestAttributes().getSessionId();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void close() {
        //this.close();
    }

    /**
     * The handler after original handler. Fetch attributes and release access
     * lock.
     *
     * @author pao
     */
    @Bean
    public EmbeddedServletContainerCustomizer customizer() throws MalformedURLException {

        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof JettyEmbeddedServletContainerFactory) {
                    customizeJetty((JettyEmbeddedServletContainerFactory) container);
                }
            }

            private void customizeJetty(JettyEmbeddedServletContainerFactory jetty) {
                jetty.addServerCustomizers((JettyServerCustomizer) server -> {
                    HandlerCollection handlerCollection = new HandlerCollection();
                    handlerCollection.setHandlers(new Handler[]{server.getHandler(), new MyCustomHandler()});
                    server.setHandler(handlerCollection);
                });
            }

            class MyCustomHandler extends HandlerWrapper {
                @SuppressWarnings("unchecked")
                public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException {
                    try {
                        // copy attributes for test case
                        requestParameters = new HashMap<String, String[]>(request.getParameterMap());
                        HttpSession session = request.getSession(false);
                        sessionId = session != null ? session.getId() : null;
                        sessionAttributes = new HashMap<String, Object>();
                        if (session != null) {
                            Enumeration<String> names = session.getAttributeNames();
                            while (names.hasMoreElements()) {
                                String name = names.nextElement();
                                sessionAttributes.put(name, session.getAttribute(name));
                            }
                        }
                        requestAttributes = new HashMap<String, Object>();
                        Enumeration<String> names = request.getAttributeNames();
                        while (names.hasMoreElements()) {
                            String name = names.nextElement();
                            requestAttributes.put(name, request.getAttribute(name));
                        }
                    } catch (Exception e) {
                        System.err.println("Exception : " + e);
                    }
                }
            }
        };
    }
}

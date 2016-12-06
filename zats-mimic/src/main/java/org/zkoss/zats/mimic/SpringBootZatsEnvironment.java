package org.zkoss.zats.mimic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;
import org.zkoss.zats.mimic.impl.emulator.SpringBootEmulator;

import javax.servlet.ServletContext;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DUCHENEO on 1/12/2016.
 */
@Component
public class SpringBootZatsEnvironment implements ZatsEnvironment {

    private String contextPath;
    private ServletContext servletContext;
    private List<Client> clients = new LinkedList<Client>();

    @Autowired
    private SpringBootEmulator emulator ;

    public void init(String resourceRoot) {
    }

    public void init(ServletContext servletContext){
        emulator.setServletContext(servletContext);
    }

    public Emulator getEmulator() {
        return emulator;
    }

    public void destroy() {
        cleanup();
        if (emulator!=null){
            emulator.close();
            emulator=null;
        }
    }

    /**
     * create a client.
     * @return a new client
     */
    public Client newClient(){
        if(emulator==null){
            throw new ZatsException("not initialize yet, please call init first");
        }
        Client client = new EmulatorClient(emulator);
        ((ClientCtrl)client).setDestroyListener(new ClientCtrl.DestroyListener() {
            public void willDestroy(Client conv) {
                clients.remove(conv);//just remove it from list, client will destory itself
            }
        });
        clients.add(client);
        return client;
    }

    /**
     * close all clients and release resources.
     */
    public void cleanup() {
        //to avoid concurrent modification exception in willClose
        for (Client c : clients.toArray(new Client[clients.size()])){
            c.destroy();
        }
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}

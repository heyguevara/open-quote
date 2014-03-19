package com.ail.core.urlhandler.product;

import java.net.URL;
import java.net.URLConnection;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;

@ServiceInterface
public class ProductRepositoryService {
    
    @ServiceArgument
    public interface ProductRepositoryArgument extends Argument {
        public void setProductUrlArg(URL productUrlArg);
        public URL getProductUrlArg();
        public void setURLConnectionRet(URLConnection urlConnectionRet);
        public URLConnection getURLConnectionRet();
    }
    
    @ServiceCommand(defaultServiceClass=LiferayProductRepositoryService.class)
    public interface ProductRepositoryCommand extends ProductRepositoryArgument, Command {
    }
}

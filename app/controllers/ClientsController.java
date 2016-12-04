package controllers;

import models.Client;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ClientsController extends Controller {
    private FormFactory formFactory;
    private Form<Client> clientForm;

    @Inject
    public ClientsController(FormFactory formFactory){
        this.formFactory = formFactory;
        this.clientForm = formFactory.form(Client.class);
    }

    public Result index() {
        //get all cars
        List<Client> clients = Client.findAll();
        return ok(views.html.clients.index.render(clients));
    }

    public Result newClient(){
        clientForm = clientForm.fill(new Client());
        return ok(views.html.clients.newClient.render(clientForm));
    }

    public Result create(){
        clientForm = formFactory.form(Client.class).bindFromRequest();
        if (clientForm.hasErrors()){
            return ok(views.html.clients.newClient.render(clientForm));
        } else {
            // save the data
            Client client = clientForm.get();
            client.save();
            flash("success", "The client was created successfully.");
            return redirect(routes.ClientsController.index());
        }
    }
}

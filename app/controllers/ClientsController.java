package controllers;

import models.Client;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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
        List<Client> clients = Client.find(null, "{name: 1}", false);
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

    public Result edit(String id){
        Client client = Client.findById(id);
        if (client == null){
            return notFound();
        }
        clientForm = clientForm.fill(client);
        return ok(views.html.clients.edit.render(clientForm));
    }

    public Result update(String id){
        Client client = Client.findById(id);
        if (client == null){
            return notFound();
        }
        clientForm = formFactory.form(Client.class).bindFromRequest();
        if (clientForm.hasErrors()){
            return ok(views.html.clients.edit.render(clientForm));
        } else {
            Client updatedClient = clientForm.get();
            updatedClient.id = client.id;
            updatedClient.save();
            flash("success", "The client was updated successfully.");
            return redirect(routes.ClientsController.index());
        }
    }
}



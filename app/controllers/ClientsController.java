package controllers;

import models.Client;
import models.Site;
import models.dao.ClientDao;
import org.jongo.MongoCursor;
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
    private ClientDao clientDao;

    @Inject
    public ClientsController(FormFactory formFactory, ClientDao clientDao){
        this.formFactory = formFactory;
        this.clientForm = formFactory.form(Client.class);
        this.clientDao = clientDao;
    }

    public Result index() {
        String searchTerm = request().getQueryString("search");
        List<Client> clients = clientDao.searchOrAll(searchTerm, "{name: 1}");
        if (searchTerm == null) searchTerm = "";
        return ok(views.html.clients.index.render(clients, searchTerm));
    }

    public Result newClient(){
        clientForm = clientForm.fill(new Client());
        return ok(views.html.clients.newClient.render(clientForm));
    }

    public Result create(){
        clientForm = clientForm.bindFromRequest();
        if (clientForm.hasErrors()){
            return ok(views.html.clients.newClient.render(clientForm));
        } else {
            // save the data
            Client client = clientForm.get();
            clientDao.save(client);
            flash("success", "The client was created successfully.");
            return redirect(routes.ClientsController.index());
        }
    }

    public Result edit(String id){
        Client client = clientDao.findById(id);
        if (client == null){
            return notFound();
        }
        clientForm = clientForm.fill(client);
        return ok(views.html.clients.edit.render(clientForm));
    }

    public Result update(String id){
        Client client = clientDao.findById(id);
        if (client == null){
            return notFound();
        }
        clientForm = formFactory.form(Client.class).bindFromRequest();
        if (clientForm.hasErrors()){
            return ok(views.html.clients.edit.render(clientForm));
        } else {
            Client updatedClient = clientForm.get();
            updatedClient.id = client.id;
            clientDao.save(updatedClient);
            flash("success", "The client was updated successfully.");
            return redirect(routes.ClientsController.index());
        }
    }

    public Result show(String id)
    {
        Client client = clientDao.findById(id);
        if (client == null){
            return notFound();
        }
        return ok(views.html.clients.show.render(client));
    }

    public Result delete(String id){
        Client client = clientDao.findById(id);
        if (client == null){
            return notFound();
        }
        clientDao.delete(client);
        flash("success", "The client was deleted successfully.");
        return redirect(routes.ClientsController.index());
    }
}



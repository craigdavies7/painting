package controllers;

import models.Client;
import models.Site;
import models.dao.ClientDao;
import models.dao.SiteDao;
import org.jongo.MongoCursor;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SitesController extends Controller {
    private FormFactory formFactory;
    private Form<Site> siteForm;
    private SiteDao siteDao;
    private ClientDao clientDao;

    @Inject
    public SitesController(FormFactory formFactory, SiteDao siteDao, ClientDao clientDao){
        this.formFactory = formFactory;
        this.siteForm = formFactory.form(Site.class);
        this.siteDao = siteDao;
        this.clientDao = clientDao;
    }

    public Result index() {
        String searchTerm = request().getQueryString("search");
        List<Site> sites = siteDao.search(searchTerm, "{name: 1}");
        if (searchTerm == null) searchTerm = "";
        return ok(views.html.sites.index.render(sites, searchTerm));
    }

    public Result newSite(){
        siteForm = siteForm.fill(new Site());
        return ok(views.html.sites.newSite.render(siteForm));
    }

    public Result edit(String id){
        Site site = siteDao.findById(id);
        if (site == null){
            return notFound();
        }
        siteForm = siteForm.fill(site);
        return ok(views.html.sites.edit.render(siteForm));
    }

    public Result show(String id){
        Site site = siteDao.findById(id);
        if (site == null){
            return notFound();
        }
        return ok(views.html.sites.show.render(site));
    }

    public Result create(){
        siteForm = siteForm.bindFromRequest();
        if (siteForm.hasErrors()){
            return ok(views.html.sites.newSite.render(siteForm));
        } else {
            // save the data
            Site site = siteForm.get();
            siteDao.save(site);
            flash("success", "The site was created successfully.");
            return redirect(routes.SitesController.index());
        }
    }

    public Result update(String id){
        Site site = siteDao.findById(id);
        if (site == null){
            return notFound();
        }
        // preload siteForm with db data
        siteForm = formFactory.form(Site.class).bindFromRequest();
        if (siteForm.hasErrors()){
            return ok(views.html.sites.edit.render(siteForm));
        } else {
            // save the data
            // we need to get the form site, and set it's id using our looked up site
            Site updatedSite = siteForm.get();
            updatedSite.id = site.id;
            siteDao.save(updatedSite);
            flash("success", "The site was updated successfully.");
            return redirect(routes.SitesController.index());
        }
    }

    public Result delete(String id){
        Site site = siteDao.findById(id);
        if (site == null){
            return notFound();
        }
        siteDao.delete(site);
        flash("success", "The site was deleted successfully.");
        return redirect(routes.SitesController.index());
    }

    public Result manageClients(String id) {
        Site site = siteDao.findById(id);
        if (site == null) {
            return notFound();
        }

        String searchTerm = request().getQueryString("search");
        List<Client> clients = clientDao.searchOrNone(searchTerm, "{name: 1}");
        List<Client> associatedClients = siteDao.clients("{name: 1}");
        if (searchTerm == null) searchTerm = "";
        return ok(views.html.sites.manageClients.render(id, clients, associatedClients, searchTerm));
    }
}

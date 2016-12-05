package controllers;

import models.Client;
import models.Site;
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
public class SitesController extends Controller {
    private FormFactory formFactory;
    private Form<Site> siteForm;

    @Inject
    public SitesController(FormFactory formFactory){
        this.formFactory = formFactory;
        this.siteForm = formFactory.form(Site.class);
    }

    public Result index() {
        String searchTerm = request().getQueryString("search");
        List<Site> sites = Site.search(searchTerm);
        if (searchTerm == null) searchTerm = "";
        return ok(views.html.sites.index.render(sites, searchTerm));
    }

    public Result newSite(){
        siteForm = siteForm.fill(new Site());
        return ok(views.html.sites.newSite.render(siteForm));
    }

    public Result edit(String id){
        Site site = Site.findById(id);
        if (site == null){
            return notFound();
        }
        siteForm = siteForm.fill(site);
        return ok(views.html.sites.edit.render(siteForm));
    }

    public Result show(String id){
        Site site = Site.findById(id);
        if (site == null){
            return notFound();
        }
        return ok(views.html.sites.show.render(site));
    }

    public Result create(){
        siteForm = formFactory.form(Site.class).bindFromRequest();
        if (siteForm.hasErrors()){
            return ok(views.html.sites.newSite.render(siteForm));
        } else {
            // save the data
            Site site = siteForm.get();
            site.save();
            flash("success", "The site was created successfully.");
            return redirect(routes.SitesController.index());
        }
    }

    public Result update(String id){
        Site site = Site.findById(id);
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
            updatedSite.save();
            flash("success", "The site was updated successfully.");
            return redirect(routes.SitesController.index());
        }
    }

    public Result delete(String id){
        Site site = Site.findById(id);
        if (site == null){
            return notFound();
        }
        site.delete();
        flash("success", "The site was deleted successfully.");
        return redirect(routes.SitesController.index());
    }

    public Result manageClients(String id){
        Site site = Site.findById(id);
        if (site == null){
            return notFound();
        }
        List<Client> clients = site.clients();
        return ok(views.html.sites.manageClients.render(id, clients));
    }
}

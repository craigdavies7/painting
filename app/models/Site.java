package models;

import play.data.validation.Constraints;

import java.util.List;
public class Site extends Base {

    private Client staticClient;

    @Constraints.Required
    public String name;
    public String description;
    public List<Integer> clientIds;
}

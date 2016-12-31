package models;

import play.data.validation.Constraints;

import java.util.List;
public class Site extends Base {

    @Constraints.Required
    public String name;
    public String description;
}

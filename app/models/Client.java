package models;

import play.data.validation.Constraints;

public class Client extends Base {

    @Constraints.Required
    public String name;
}

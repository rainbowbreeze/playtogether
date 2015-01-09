package it.rainbowbreeze.playtog.data;

import com.googlecode.objectify.Key;

import java.util.List;

import static it.rainbowbreeze.playtog.data.OfyService.ofy;

/**
 * Very basic Objectify DAO:
 * http://turbomanage.wordpress.com/2010/02/09/generic-dao-for-objectify-2/
 *
 * More complex library of utilities for GAE, included an objectify DAO
 * https://github.com/sappenin/appengine-utils
 *
 * Created by alfredomorresi on 08/01/15.
 */
public abstract class BaseOfyDao <Ent> {
    protected static final int MAX_COUNT = 30;

    private final Class<Ent> clazz;

    protected BaseOfyDao(Class<Ent> clazz) {
        this.clazz = clazz;
    }

    public void save(Ent entity) {
        ofy().save().entity(entity).now();
    }

    public Ent get(long id) {
        return ofy().load().type(clazz).id(id).now();
    }

    public void delete(Ent entity) {
        ofy().delete().entity(entity).now();
    }

    public List<Ent> listAll() {
        return ofy().load().type(clazz).limit(MAX_COUNT).list();
    }


}

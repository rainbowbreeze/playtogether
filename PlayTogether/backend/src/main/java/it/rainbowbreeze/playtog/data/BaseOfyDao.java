/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
   -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

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

    public void deleteAll() {
        // https://groups.google.com/forum/#!msg/objectify-appengine/p4UylG6jTwU/qIT8sqrPBokJ
        List<Key<Ent>> keys = ofy().load().type(clazz).keys().list();
        ofy().delete().keys(keys).now();
    }

}

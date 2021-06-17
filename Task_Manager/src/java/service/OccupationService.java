/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.JPAUtil;
import dao.OccupationDao;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import model.Occupation;

/**
 *
 * @author andre
 */
public class OccupationService {
    
    @Inject
    private OccupationDao dao;
    
    public void save(Occupation occupation) {
        dao.save(occupation);
    }
    
}

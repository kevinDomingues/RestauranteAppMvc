/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.BLL;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import restauranteapp.BLL.CodpostaisJpaController;
import restauranteapp.BLL.EmpresaJpaController;
import restauranteapp.BLL.EntidadeJpaController;
import restauranteapp.DAL.Codpostais;
import restauranteapp.DAL.Empresa;
import restauranteapp.DAL.Entidade;


/**
 *
 * @author kevin
 */
public class LoginJpaController {
    
    private EntityManagerFactory em;
    private EntidadeJpaController ec;
    private CodpostaisJpaController postalControl;

    public LoginJpaController() {
        this.em = Persistence.createEntityManagerFactory("RestauranteAppPU");
        this.ec = new EntidadeJpaController(this.em);
        this.postalControl = new CodpostaisJpaController(this.em);
    }
    
    public Entidade findClienteUsername(String username){
       EntityManager em2 = em.createEntityManager();
       try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entidade.class));
          Query q =   em2.createNamedQuery("Entidade.findByUsername", Entidade.class);
          q.setParameter("username", username);
           return (Entidade) q.getSingleResult();
         }catch(NoResultException e){
            return null;
        } finally {
            em2.close();
        }
    }
    
    
    public boolean validateLogin(String username, String password){  
        Entidade temp = findClienteUsername(username);
        boolean loginStatus = false;
        
        if(temp.getPasswordp().equals(password)){
            if(temp.getNivelpermissao()==2){
                loginStatus = true;
            }
        }
        
        if(loginStatus) {
            
            return true;
        } else return false;
        
    }
    
    public boolean validateLoginWeb(String username, String password){  
        Entidade temp = findClienteUsername(username);
        boolean loginStatus = false;
        
        if(temp.getPasswordp().equals(password)){
            if(temp.getNivelpermissao()==1){
                loginStatus = true;
            }
        }
        
        if(loginStatus) {           
            return true;
        } else return false;
        
    }
    
    public List<Codpostais> populateCodPostais(){
        List<Codpostais> cods = postalControl.findCodpostaisEntities();
        
        return cods;
    }
      
}

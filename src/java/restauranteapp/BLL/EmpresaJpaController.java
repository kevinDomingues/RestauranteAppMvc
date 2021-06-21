/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.BLL;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import restauranteapp.DAL.Codpostais;
import restauranteapp.DAL.Entidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Empresa;

/**
 *
 * @author kevin
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getEntidadeList() == null) {
            empresa.setEntidadeList(new ArrayList<Entidade>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codpostais codpostal = empresa.getCodpostal();
            if (codpostal != null) {
                codpostal = em.getReference(codpostal.getClass(), codpostal.getCodpostal());
                empresa.setCodpostal(codpostal);
            }
            List<Entidade> attachedEntidadeList = new ArrayList<Entidade>();
            for (Entidade entidadeListEntidadeToAttach : empresa.getEntidadeList()) {
                entidadeListEntidadeToAttach = em.getReference(entidadeListEntidadeToAttach.getClass(), entidadeListEntidadeToAttach.getIdEntidade());
                attachedEntidadeList.add(entidadeListEntidadeToAttach);
            }
            empresa.setEntidadeList(attachedEntidadeList);
            em.persist(empresa);
            if (codpostal != null) {
                codpostal.getEmpresaList().add(empresa);
                codpostal = em.merge(codpostal);
            }
            for (Entidade entidadeListEntidade : empresa.getEntidadeList()) {
                Empresa oldIdEmpresaOfEntidadeListEntidade = entidadeListEntidade.getIdEmpresa();
                entidadeListEntidade.setIdEmpresa(empresa);
                entidadeListEntidade = em.merge(entidadeListEntidade);
                if (oldIdEmpresaOfEntidadeListEntidade != null) {
                    oldIdEmpresaOfEntidadeListEntidade.getEntidadeList().remove(entidadeListEntidade);
                    oldIdEmpresaOfEntidadeListEntidade = em.merge(oldIdEmpresaOfEntidadeListEntidade);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Codpostais codpostalOld = persistentEmpresa.getCodpostal();
            Codpostais codpostalNew = empresa.getCodpostal();
            List<Entidade> entidadeListOld = persistentEmpresa.getEntidadeList();
            List<Entidade> entidadeListNew = empresa.getEntidadeList();
            List<String> illegalOrphanMessages = null;
            for (Entidade entidadeListOldEntidade : entidadeListOld) {
                if (!entidadeListNew.contains(entidadeListOldEntidade)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entidade " + entidadeListOldEntidade + " since its idEmpresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codpostalNew != null) {
                codpostalNew = em.getReference(codpostalNew.getClass(), codpostalNew.getCodpostal());
                empresa.setCodpostal(codpostalNew);
            }
            List<Entidade> attachedEntidadeListNew = new ArrayList<Entidade>();
            for (Entidade entidadeListNewEntidadeToAttach : entidadeListNew) {
                entidadeListNewEntidadeToAttach = em.getReference(entidadeListNewEntidadeToAttach.getClass(), entidadeListNewEntidadeToAttach.getIdEntidade());
                attachedEntidadeListNew.add(entidadeListNewEntidadeToAttach);
            }
            entidadeListNew = attachedEntidadeListNew;
            empresa.setEntidadeList(entidadeListNew);
            empresa = em.merge(empresa);
            if (codpostalOld != null && !codpostalOld.equals(codpostalNew)) {
                codpostalOld.getEmpresaList().remove(empresa);
                codpostalOld = em.merge(codpostalOld);
            }
            if (codpostalNew != null && !codpostalNew.equals(codpostalOld)) {
                codpostalNew.getEmpresaList().add(empresa);
                codpostalNew = em.merge(codpostalNew);
            }
            for (Entidade entidadeListNewEntidade : entidadeListNew) {
                if (!entidadeListOld.contains(entidadeListNewEntidade)) {
                    Empresa oldIdEmpresaOfEntidadeListNewEntidade = entidadeListNewEntidade.getIdEmpresa();
                    entidadeListNewEntidade.setIdEmpresa(empresa);
                    entidadeListNewEntidade = em.merge(entidadeListNewEntidade);
                    if (oldIdEmpresaOfEntidadeListNewEntidade != null && !oldIdEmpresaOfEntidadeListNewEntidade.equals(empresa)) {
                        oldIdEmpresaOfEntidadeListNewEntidade.getEntidadeList().remove(entidadeListNewEntidade);
                        oldIdEmpresaOfEntidadeListNewEntidade = em.merge(oldIdEmpresaOfEntidadeListNewEntidade);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Entidade> entidadeListOrphanCheck = empresa.getEntidadeList();
            for (Entidade entidadeListOrphanCheckEntidade : entidadeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Entidade " + entidadeListOrphanCheckEntidade + " in its entidadeList field has a non-nullable idEmpresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Codpostais codpostal = empresa.getCodpostal();
            if (codpostal != null) {
                codpostal.getEmpresaList().remove(empresa);
                codpostal = em.merge(codpostal);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

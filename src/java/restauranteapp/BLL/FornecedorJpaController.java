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
import restauranteapp.DAL.Encomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Fornecedor;

/**
 *
 * @author kevin
 */
public class FornecedorJpaController implements Serializable {

    public FornecedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fornecedor fornecedor) {
        if (fornecedor.getEncomendaList() == null) {
            fornecedor.setEncomendaList(new ArrayList<Encomenda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codpostais codpostal = fornecedor.getCodpostal();
            if (codpostal != null) {
                codpostal = em.getReference(codpostal.getClass(), codpostal.getCodpostal());
                fornecedor.setCodpostal(codpostal);
            }
            List<Encomenda> attachedEncomendaList = new ArrayList<Encomenda>();
            for (Encomenda encomendaListEncomendaToAttach : fornecedor.getEncomendaList()) {
                encomendaListEncomendaToAttach = em.getReference(encomendaListEncomendaToAttach.getClass(), encomendaListEncomendaToAttach.getIdEncomenda());
                attachedEncomendaList.add(encomendaListEncomendaToAttach);
            }
            fornecedor.setEncomendaList(attachedEncomendaList);
            em.persist(fornecedor);
            if (codpostal != null) {
                codpostal.getFornecedorList().add(fornecedor);
                codpostal = em.merge(codpostal);
            }
            for (Encomenda encomendaListEncomenda : fornecedor.getEncomendaList()) {
                Fornecedor oldIdFornecedorOfEncomendaListEncomenda = encomendaListEncomenda.getIdFornecedor();
                encomendaListEncomenda.setIdFornecedor(fornecedor);
                encomendaListEncomenda = em.merge(encomendaListEncomenda);
                if (oldIdFornecedorOfEncomendaListEncomenda != null) {
                    oldIdFornecedorOfEncomendaListEncomenda.getEncomendaList().remove(encomendaListEncomenda);
                    oldIdFornecedorOfEncomendaListEncomenda = em.merge(oldIdFornecedorOfEncomendaListEncomenda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fornecedor fornecedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fornecedor persistentFornecedor = em.find(Fornecedor.class, fornecedor.getIdFornecedor());
            Codpostais codpostalOld = persistentFornecedor.getCodpostal();
            Codpostais codpostalNew = fornecedor.getCodpostal();
            List<Encomenda> encomendaListOld = persistentFornecedor.getEncomendaList();
            List<Encomenda> encomendaListNew = fornecedor.getEncomendaList();
            List<String> illegalOrphanMessages = null;
            for (Encomenda encomendaListOldEncomenda : encomendaListOld) {
                if (!encomendaListNew.contains(encomendaListOldEncomenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomenda " + encomendaListOldEncomenda + " since its idFornecedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codpostalNew != null) {
                codpostalNew = em.getReference(codpostalNew.getClass(), codpostalNew.getCodpostal());
                fornecedor.setCodpostal(codpostalNew);
            }
            List<Encomenda> attachedEncomendaListNew = new ArrayList<Encomenda>();
            for (Encomenda encomendaListNewEncomendaToAttach : encomendaListNew) {
                encomendaListNewEncomendaToAttach = em.getReference(encomendaListNewEncomendaToAttach.getClass(), encomendaListNewEncomendaToAttach.getIdEncomenda());
                attachedEncomendaListNew.add(encomendaListNewEncomendaToAttach);
            }
            encomendaListNew = attachedEncomendaListNew;
            fornecedor.setEncomendaList(encomendaListNew);
            fornecedor = em.merge(fornecedor);
            if (codpostalOld != null && !codpostalOld.equals(codpostalNew)) {
                codpostalOld.getFornecedorList().remove(fornecedor);
                codpostalOld = em.merge(codpostalOld);
            }
            if (codpostalNew != null && !codpostalNew.equals(codpostalOld)) {
                codpostalNew.getFornecedorList().add(fornecedor);
                codpostalNew = em.merge(codpostalNew);
            }
            for (Encomenda encomendaListNewEncomenda : encomendaListNew) {
                if (!encomendaListOld.contains(encomendaListNewEncomenda)) {
                    Fornecedor oldIdFornecedorOfEncomendaListNewEncomenda = encomendaListNewEncomenda.getIdFornecedor();
                    encomendaListNewEncomenda.setIdFornecedor(fornecedor);
                    encomendaListNewEncomenda = em.merge(encomendaListNewEncomenda);
                    if (oldIdFornecedorOfEncomendaListNewEncomenda != null && !oldIdFornecedorOfEncomendaListNewEncomenda.equals(fornecedor)) {
                        oldIdFornecedorOfEncomendaListNewEncomenda.getEncomendaList().remove(encomendaListNewEncomenda);
                        oldIdFornecedorOfEncomendaListNewEncomenda = em.merge(oldIdFornecedorOfEncomendaListNewEncomenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fornecedor.getIdFornecedor();
                if (findFornecedor(id) == null) {
                    throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.");
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
            Fornecedor fornecedor;
            try {
                fornecedor = em.getReference(Fornecedor.class, id);
                fornecedor.getIdFornecedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Encomenda> encomendaListOrphanCheck = fornecedor.getEncomendaList();
            for (Encomenda encomendaListOrphanCheckEncomenda : encomendaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fornecedor (" + fornecedor + ") cannot be destroyed since the Encomenda " + encomendaListOrphanCheckEncomenda + " in its encomendaList field has a non-nullable idFornecedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Codpostais codpostal = fornecedor.getCodpostal();
            if (codpostal != null) {
                codpostal.getFornecedorList().remove(fornecedor);
                codpostal = em.merge(codpostal);
            }
            em.remove(fornecedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fornecedor> findFornecedorEntities() {
        return findFornecedorEntities(true, -1, -1);
    }

    public List<Fornecedor> findFornecedorEntities(int maxResults, int firstResult) {
        return findFornecedorEntities(false, maxResults, firstResult);
    }

    private List<Fornecedor> findFornecedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fornecedor.class));
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

    public Fornecedor findFornecedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fornecedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getFornecedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fornecedor> rt = cq.from(Fornecedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

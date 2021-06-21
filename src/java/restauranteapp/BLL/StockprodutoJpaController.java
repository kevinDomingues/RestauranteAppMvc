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
import restauranteapp.DAL.Linhaencomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Stockproduto;

/**
 *
 * @author kevin
 */
public class StockprodutoJpaController implements Serializable {

    public StockprodutoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stockproduto stockproduto) {
        if (stockproduto.getLinhaencomendaList() == null) {
            stockproduto.setLinhaencomendaList(new ArrayList<Linhaencomenda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Linhaencomenda> attachedLinhaencomendaList = new ArrayList<Linhaencomenda>();
            for (Linhaencomenda linhaencomendaListLinhaencomendaToAttach : stockproduto.getLinhaencomendaList()) {
                linhaencomendaListLinhaencomendaToAttach = em.getReference(linhaencomendaListLinhaencomendaToAttach.getClass(), linhaencomendaListLinhaencomendaToAttach.getLinhaencomendaPK());
                attachedLinhaencomendaList.add(linhaencomendaListLinhaencomendaToAttach);
            }
            stockproduto.setLinhaencomendaList(attachedLinhaencomendaList);
            em.persist(stockproduto);
            for (Linhaencomenda linhaencomendaListLinhaencomenda : stockproduto.getLinhaencomendaList()) {
                Stockproduto oldStockprodutoOfLinhaencomendaListLinhaencomenda = linhaencomendaListLinhaencomenda.getStockproduto();
                linhaencomendaListLinhaencomenda.setStockproduto(stockproduto);
                linhaencomendaListLinhaencomenda = em.merge(linhaencomendaListLinhaencomenda);
                if (oldStockprodutoOfLinhaencomendaListLinhaencomenda != null) {
                    oldStockprodutoOfLinhaencomendaListLinhaencomenda.getLinhaencomendaList().remove(linhaencomendaListLinhaencomenda);
                    oldStockprodutoOfLinhaencomendaListLinhaencomenda = em.merge(oldStockprodutoOfLinhaencomendaListLinhaencomenda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stockproduto stockproduto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stockproduto persistentStockproduto = em.find(Stockproduto.class, stockproduto.getIdStockproduto());
            List<Linhaencomenda> linhaencomendaListOld = persistentStockproduto.getLinhaencomendaList();
            List<Linhaencomenda> linhaencomendaListNew = stockproduto.getLinhaencomendaList();
            List<String> illegalOrphanMessages = null;
            for (Linhaencomenda linhaencomendaListOldLinhaencomenda : linhaencomendaListOld) {
                if (!linhaencomendaListNew.contains(linhaencomendaListOldLinhaencomenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linhaencomenda " + linhaencomendaListOldLinhaencomenda + " since its stockproduto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Linhaencomenda> attachedLinhaencomendaListNew = new ArrayList<Linhaencomenda>();
            for (Linhaencomenda linhaencomendaListNewLinhaencomendaToAttach : linhaencomendaListNew) {
                linhaencomendaListNewLinhaencomendaToAttach = em.getReference(linhaencomendaListNewLinhaencomendaToAttach.getClass(), linhaencomendaListNewLinhaencomendaToAttach.getLinhaencomendaPK());
                attachedLinhaencomendaListNew.add(linhaencomendaListNewLinhaencomendaToAttach);
            }
            linhaencomendaListNew = attachedLinhaencomendaListNew;
            stockproduto.setLinhaencomendaList(linhaencomendaListNew);
            stockproduto = em.merge(stockproduto);
            for (Linhaencomenda linhaencomendaListNewLinhaencomenda : linhaencomendaListNew) {
                if (!linhaencomendaListOld.contains(linhaencomendaListNewLinhaencomenda)) {
                    Stockproduto oldStockprodutoOfLinhaencomendaListNewLinhaencomenda = linhaencomendaListNewLinhaencomenda.getStockproduto();
                    linhaencomendaListNewLinhaencomenda.setStockproduto(stockproduto);
                    linhaencomendaListNewLinhaencomenda = em.merge(linhaencomendaListNewLinhaencomenda);
                    if (oldStockprodutoOfLinhaencomendaListNewLinhaencomenda != null && !oldStockprodutoOfLinhaencomendaListNewLinhaencomenda.equals(stockproduto)) {
                        oldStockprodutoOfLinhaencomendaListNewLinhaencomenda.getLinhaencomendaList().remove(linhaencomendaListNewLinhaencomenda);
                        oldStockprodutoOfLinhaencomendaListNewLinhaencomenda = em.merge(oldStockprodutoOfLinhaencomendaListNewLinhaencomenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stockproduto.getIdStockproduto();
                if (findStockproduto(id) == null) {
                    throw new NonexistentEntityException("The stockproduto with id " + id + " no longer exists.");
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
            Stockproduto stockproduto;
            try {
                stockproduto = em.getReference(Stockproduto.class, id);
                stockproduto.getIdStockproduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stockproduto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Linhaencomenda> linhaencomendaListOrphanCheck = stockproduto.getLinhaencomendaList();
            for (Linhaencomenda linhaencomendaListOrphanCheckLinhaencomenda : linhaencomendaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Stockproduto (" + stockproduto + ") cannot be destroyed since the Linhaencomenda " + linhaencomendaListOrphanCheckLinhaencomenda + " in its linhaencomendaList field has a non-nullable stockproduto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(stockproduto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stockproduto> findStockprodutoEntities() {
        return findStockprodutoEntities(true, -1, -1);
    }

    public List<Stockproduto> findStockprodutoEntities(int maxResults, int firstResult) {
        return findStockprodutoEntities(false, maxResults, firstResult);
    }

    private List<Stockproduto> findStockprodutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stockproduto.class));
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

    public Stockproduto findStockproduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stockproduto.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockprodutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stockproduto> rt = cq.from(Stockproduto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

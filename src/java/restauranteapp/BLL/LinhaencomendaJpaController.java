/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.BLL;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.BLL.exceptions.PreexistingEntityException;
import restauranteapp.DAL.Encomenda;
import restauranteapp.DAL.Linhaencomenda;
import restauranteapp.DAL.LinhaencomendaPK;
import restauranteapp.DAL.Stockproduto;

/**
 *
 * @author kevin
 */
public class LinhaencomendaJpaController implements Serializable {

    public LinhaencomendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Linhaencomenda linhaencomenda) throws PreexistingEntityException, Exception {
        if (linhaencomenda.getLinhaencomendaPK() == null) {
            linhaencomenda.setLinhaencomendaPK(new LinhaencomendaPK());
        }
        linhaencomenda.getLinhaencomendaPK().setIdEncomenda(linhaencomenda.getEncomenda().getIdEncomenda());
        linhaencomenda.getLinhaencomendaPK().setIdStockproduto(linhaencomenda.getStockproduto().getIdStockproduto());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encomenda encomenda = linhaencomenda.getEncomenda();
            if (encomenda != null) {
                encomenda = em.getReference(encomenda.getClass(), encomenda.getIdEncomenda());
                linhaencomenda.setEncomenda(encomenda);
            }
            Stockproduto stockproduto = linhaencomenda.getStockproduto();
            if (stockproduto != null) {
                stockproduto = em.getReference(stockproduto.getClass(), stockproduto.getIdStockproduto());
                linhaencomenda.setStockproduto(stockproduto);
            }
            em.persist(linhaencomenda);
            if (encomenda != null) {
                encomenda.getLinhaencomendaList().add(linhaencomenda);
                encomenda = em.merge(encomenda);
            }
            if (stockproduto != null) {
                stockproduto.getLinhaencomendaList().add(linhaencomenda);
                stockproduto = em.merge(stockproduto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLinhaencomenda(linhaencomenda.getLinhaencomendaPK()) != null) {
                throw new PreexistingEntityException("Linhaencomenda " + linhaencomenda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Linhaencomenda linhaencomenda) throws NonexistentEntityException, Exception {
        linhaencomenda.getLinhaencomendaPK().setIdEncomenda(linhaencomenda.getEncomenda().getIdEncomenda());
        linhaencomenda.getLinhaencomendaPK().setIdStockproduto(linhaencomenda.getStockproduto().getIdStockproduto());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linhaencomenda persistentLinhaencomenda = em.find(Linhaencomenda.class, linhaencomenda.getLinhaencomendaPK());
            Encomenda encomendaOld = persistentLinhaencomenda.getEncomenda();
            Encomenda encomendaNew = linhaencomenda.getEncomenda();
            Stockproduto stockprodutoOld = persistentLinhaencomenda.getStockproduto();
            Stockproduto stockprodutoNew = linhaencomenda.getStockproduto();
            if (encomendaNew != null) {
                encomendaNew = em.getReference(encomendaNew.getClass(), encomendaNew.getIdEncomenda());
                linhaencomenda.setEncomenda(encomendaNew);
            }
            if (stockprodutoNew != null) {
                stockprodutoNew = em.getReference(stockprodutoNew.getClass(), stockprodutoNew.getIdStockproduto());
                linhaencomenda.setStockproduto(stockprodutoNew);
            }
            linhaencomenda = em.merge(linhaencomenda);
            if (encomendaOld != null && !encomendaOld.equals(encomendaNew)) {
                encomendaOld.getLinhaencomendaList().remove(linhaencomenda);
                encomendaOld = em.merge(encomendaOld);
            }
            if (encomendaNew != null && !encomendaNew.equals(encomendaOld)) {
                encomendaNew.getLinhaencomendaList().add(linhaencomenda);
                encomendaNew = em.merge(encomendaNew);
            }
            if (stockprodutoOld != null && !stockprodutoOld.equals(stockprodutoNew)) {
                stockprodutoOld.getLinhaencomendaList().remove(linhaencomenda);
                stockprodutoOld = em.merge(stockprodutoOld);
            }
            if (stockprodutoNew != null && !stockprodutoNew.equals(stockprodutoOld)) {
                stockprodutoNew.getLinhaencomendaList().add(linhaencomenda);
                stockprodutoNew = em.merge(stockprodutoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                LinhaencomendaPK id = linhaencomenda.getLinhaencomendaPK();
                if (findLinhaencomenda(id) == null) {
                    throw new NonexistentEntityException("The linhaencomenda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(LinhaencomendaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linhaencomenda linhaencomenda;
            try {
                linhaencomenda = em.getReference(Linhaencomenda.class, id);
                linhaencomenda.getLinhaencomendaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The linhaencomenda with id " + id + " no longer exists.", enfe);
            }
            Encomenda encomenda = linhaencomenda.getEncomenda();
            if (encomenda != null) {
                encomenda.getLinhaencomendaList().remove(linhaencomenda);
                encomenda = em.merge(encomenda);
            }
            Stockproduto stockproduto = linhaencomenda.getStockproduto();
            if (stockproduto != null) {
                stockproduto.getLinhaencomendaList().remove(linhaencomenda);
                stockproduto = em.merge(stockproduto);
            }
            em.remove(linhaencomenda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Linhaencomenda> findLinhaencomendaEntities() {
        return findLinhaencomendaEntities(true, -1, -1);
    }

    public List<Linhaencomenda> findLinhaencomendaEntities(int maxResults, int firstResult) {
        return findLinhaencomendaEntities(false, maxResults, firstResult);
    }

    private List<Linhaencomenda> findLinhaencomendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Linhaencomenda.class));
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

    public Linhaencomenda findLinhaencomenda(LinhaencomendaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Linhaencomenda.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinhaencomendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Linhaencomenda> rt = cq.from(Linhaencomenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

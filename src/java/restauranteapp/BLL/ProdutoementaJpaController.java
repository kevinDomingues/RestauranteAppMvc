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
import restauranteapp.DAL.Linhapedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Produtoementa;

/**
 *
 * @author kevin
 */
public class ProdutoementaJpaController implements Serializable {

    public ProdutoementaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produtoementa produtoementa) {
        if (produtoementa.getLinhapedidoList() == null) {
            produtoementa.setLinhapedidoList(new ArrayList<Linhapedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Linhapedido> attachedLinhapedidoList = new ArrayList<Linhapedido>();
            for (Linhapedido linhapedidoListLinhapedidoToAttach : produtoementa.getLinhapedidoList()) {
                linhapedidoListLinhapedidoToAttach = em.getReference(linhapedidoListLinhapedidoToAttach.getClass(), linhapedidoListLinhapedidoToAttach.getLinhapedidoPK());
                attachedLinhapedidoList.add(linhapedidoListLinhapedidoToAttach);
            }
            produtoementa.setLinhapedidoList(attachedLinhapedidoList);
            em.persist(produtoementa);
            for (Linhapedido linhapedidoListLinhapedido : produtoementa.getLinhapedidoList()) {
                Produtoementa oldProdutoementaOfLinhapedidoListLinhapedido = linhapedidoListLinhapedido.getProdutoementa();
                linhapedidoListLinhapedido.setProdutoementa(produtoementa);
                linhapedidoListLinhapedido = em.merge(linhapedidoListLinhapedido);
                if (oldProdutoementaOfLinhapedidoListLinhapedido != null) {
                    oldProdutoementaOfLinhapedidoListLinhapedido.getLinhapedidoList().remove(linhapedidoListLinhapedido);
                    oldProdutoementaOfLinhapedidoListLinhapedido = em.merge(oldProdutoementaOfLinhapedidoListLinhapedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produtoementa produtoementa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produtoementa persistentProdutoementa = em.find(Produtoementa.class, produtoementa.getIdProduto());
            List<Linhapedido> linhapedidoListOld = persistentProdutoementa.getLinhapedidoList();
            List<Linhapedido> linhapedidoListNew = produtoementa.getLinhapedidoList();
            List<String> illegalOrphanMessages = null;
            for (Linhapedido linhapedidoListOldLinhapedido : linhapedidoListOld) {
                if (!linhapedidoListNew.contains(linhapedidoListOldLinhapedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linhapedido " + linhapedidoListOldLinhapedido + " since its produtoementa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Linhapedido> attachedLinhapedidoListNew = new ArrayList<Linhapedido>();
            for (Linhapedido linhapedidoListNewLinhapedidoToAttach : linhapedidoListNew) {
                linhapedidoListNewLinhapedidoToAttach = em.getReference(linhapedidoListNewLinhapedidoToAttach.getClass(), linhapedidoListNewLinhapedidoToAttach.getLinhapedidoPK());
                attachedLinhapedidoListNew.add(linhapedidoListNewLinhapedidoToAttach);
            }
            linhapedidoListNew = attachedLinhapedidoListNew;
            produtoementa.setLinhapedidoList(linhapedidoListNew);
            produtoementa = em.merge(produtoementa);
            for (Linhapedido linhapedidoListNewLinhapedido : linhapedidoListNew) {
                if (!linhapedidoListOld.contains(linhapedidoListNewLinhapedido)) {
                    Produtoementa oldProdutoementaOfLinhapedidoListNewLinhapedido = linhapedidoListNewLinhapedido.getProdutoementa();
                    linhapedidoListNewLinhapedido.setProdutoementa(produtoementa);
                    linhapedidoListNewLinhapedido = em.merge(linhapedidoListNewLinhapedido);
                    if (oldProdutoementaOfLinhapedidoListNewLinhapedido != null && !oldProdutoementaOfLinhapedidoListNewLinhapedido.equals(produtoementa)) {
                        oldProdutoementaOfLinhapedidoListNewLinhapedido.getLinhapedidoList().remove(linhapedidoListNewLinhapedido);
                        oldProdutoementaOfLinhapedidoListNewLinhapedido = em.merge(oldProdutoementaOfLinhapedidoListNewLinhapedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produtoementa.getIdProduto();
                if (findProdutoementa(id) == null) {
                    throw new NonexistentEntityException("The produtoementa with id " + id + " no longer exists.");
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
            Produtoementa produtoementa;
            try {
                produtoementa = em.getReference(Produtoementa.class, id);
                produtoementa.getIdProduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produtoementa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Linhapedido> linhapedidoListOrphanCheck = produtoementa.getLinhapedidoList();
            for (Linhapedido linhapedidoListOrphanCheckLinhapedido : linhapedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produtoementa (" + produtoementa + ") cannot be destroyed since the Linhapedido " + linhapedidoListOrphanCheckLinhapedido + " in its linhapedidoList field has a non-nullable produtoementa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(produtoementa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produtoementa> findProdutoementaEntities() {
        return findProdutoementaEntities(true, -1, -1);
    }

    public List<Produtoementa> findProdutoementaEntities(int maxResults, int firstResult) {
        return findProdutoementaEntities(false, maxResults, firstResult);
    }

    private List<Produtoementa> findProdutoementaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produtoementa.class));
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

    public Produtoementa findProdutoementa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produtoementa.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoementaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produtoementa> rt = cq.from(Produtoementa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

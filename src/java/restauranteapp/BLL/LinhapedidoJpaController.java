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
import restauranteapp.DAL.Linhapedido;
import restauranteapp.DAL.LinhapedidoPK;
import restauranteapp.DAL.Pedido;
import restauranteapp.DAL.Produtoementa;

/**
 *
 * @author kevin
 */
public class LinhapedidoJpaController implements Serializable {

    public LinhapedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Linhapedido linhapedido) throws PreexistingEntityException, Exception {
        if (linhapedido.getLinhapedidoPK() == null) {
            linhapedido.setLinhapedidoPK(new LinhapedidoPK());
        }
        linhapedido.getLinhapedidoPK().setIdProduto(linhapedido.getProdutoementa().getIdProduto());
        linhapedido.getLinhapedidoPK().setCodpedido(linhapedido.getPedido().getCodpedido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido = linhapedido.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getCodpedido());
                linhapedido.setPedido(pedido);
            }
            Produtoementa produtoementa = linhapedido.getProdutoementa();
            if (produtoementa != null) {
                produtoementa = em.getReference(produtoementa.getClass(), produtoementa.getIdProduto());
                linhapedido.setProdutoementa(produtoementa);
            }
            em.persist(linhapedido);
            if (pedido != null) {
                pedido.getLinhapedidoList().add(linhapedido);
                pedido = em.merge(pedido);
            }
            if (produtoementa != null) {
                produtoementa.getLinhapedidoList().add(linhapedido);
                produtoementa = em.merge(produtoementa);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLinhapedido(linhapedido.getLinhapedidoPK()) != null) {
                throw new PreexistingEntityException("Linhapedido " + linhapedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Linhapedido linhapedido) throws NonexistentEntityException, Exception {
        linhapedido.getLinhapedidoPK().setIdProduto(linhapedido.getProdutoementa().getIdProduto());
        linhapedido.getLinhapedidoPK().setCodpedido(linhapedido.getPedido().getCodpedido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linhapedido persistentLinhapedido = em.find(Linhapedido.class, linhapedido.getLinhapedidoPK());
            Pedido pedidoOld = persistentLinhapedido.getPedido();
            Pedido pedidoNew = linhapedido.getPedido();
            Produtoementa produtoementaOld = persistentLinhapedido.getProdutoementa();
            Produtoementa produtoementaNew = linhapedido.getProdutoementa();
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getCodpedido());
                linhapedido.setPedido(pedidoNew);
            }
            if (produtoementaNew != null) {
                produtoementaNew = em.getReference(produtoementaNew.getClass(), produtoementaNew.getIdProduto());
                linhapedido.setProdutoementa(produtoementaNew);
            }
            linhapedido = em.merge(linhapedido);
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getLinhapedidoList().remove(linhapedido);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getLinhapedidoList().add(linhapedido);
                pedidoNew = em.merge(pedidoNew);
            }
            if (produtoementaOld != null && !produtoementaOld.equals(produtoementaNew)) {
                produtoementaOld.getLinhapedidoList().remove(linhapedido);
                produtoementaOld = em.merge(produtoementaOld);
            }
            if (produtoementaNew != null && !produtoementaNew.equals(produtoementaOld)) {
                produtoementaNew.getLinhapedidoList().add(linhapedido);
                produtoementaNew = em.merge(produtoementaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                LinhapedidoPK id = linhapedido.getLinhapedidoPK();
                if (findLinhapedido(id) == null) {
                    throw new NonexistentEntityException("The linhapedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(LinhapedidoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linhapedido linhapedido;
            try {
                linhapedido = em.getReference(Linhapedido.class, id);
                linhapedido.getLinhapedidoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The linhapedido with id " + id + " no longer exists.", enfe);
            }
            Pedido pedido = linhapedido.getPedido();
            if (pedido != null) {
                pedido.getLinhapedidoList().remove(linhapedido);
                pedido = em.merge(pedido);
            }
            Produtoementa produtoementa = linhapedido.getProdutoementa();
            if (produtoementa != null) {
                produtoementa.getLinhapedidoList().remove(linhapedido);
                produtoementa = em.merge(produtoementa);
            }
            em.remove(linhapedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Linhapedido> findLinhapedidoEntities() {
        return findLinhapedidoEntities(true, -1, -1);
    }

    public List<Linhapedido> findLinhapedidoEntities(int maxResults, int firstResult) {
        return findLinhapedidoEntities(false, maxResults, firstResult);
    }

    private List<Linhapedido> findLinhapedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Linhapedido.class));
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

    public Linhapedido findLinhapedido(LinhapedidoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Linhapedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinhapedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Linhapedido> rt = cq.from(Linhapedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

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
import restauranteapp.DAL.Entidade;
import restauranteapp.DAL.Estado;
import restauranteapp.DAL.Linhapedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Pedido;

/**
 *
 * @author kevin
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getLinhapedidoList() == null) {
            pedido.setLinhapedidoList(new ArrayList<Linhapedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entidade idEntidade = pedido.getIdEntidade();
            if (idEntidade != null) {
                idEntidade = em.getReference(idEntidade.getClass(), idEntidade.getIdEntidade());
                pedido.setIdEntidade(idEntidade);
            }
            Estado idEstado = pedido.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                pedido.setIdEstado(idEstado);
            }
            List<Linhapedido> attachedLinhapedidoList = new ArrayList<Linhapedido>();
            for (Linhapedido linhapedidoListLinhapedidoToAttach : pedido.getLinhapedidoList()) {
                linhapedidoListLinhapedidoToAttach = em.getReference(linhapedidoListLinhapedidoToAttach.getClass(), linhapedidoListLinhapedidoToAttach.getLinhapedidoPK());
                attachedLinhapedidoList.add(linhapedidoListLinhapedidoToAttach);
            }
            pedido.setLinhapedidoList(attachedLinhapedidoList);
            em.persist(pedido);
            if (idEntidade != null) {
                idEntidade.getPedidoList().add(pedido);
                idEntidade = em.merge(idEntidade);
            }
            if (idEstado != null) {
                idEstado.getPedidoList().add(pedido);
                idEstado = em.merge(idEstado);
            }
            for (Linhapedido linhapedidoListLinhapedido : pedido.getLinhapedidoList()) {
                Pedido oldPedidoOfLinhapedidoListLinhapedido = linhapedidoListLinhapedido.getPedido();
                linhapedidoListLinhapedido.setPedido(pedido);
                linhapedidoListLinhapedido = em.merge(linhapedidoListLinhapedido);
                if (oldPedidoOfLinhapedidoListLinhapedido != null) {
                    oldPedidoOfLinhapedidoListLinhapedido.getLinhapedidoList().remove(linhapedidoListLinhapedido);
                    oldPedidoOfLinhapedidoListLinhapedido = em.merge(oldPedidoOfLinhapedidoListLinhapedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getCodpedido());
            Entidade idEntidadeOld = persistentPedido.getIdEntidade();
            Entidade idEntidadeNew = pedido.getIdEntidade();
            Estado idEstadoOld = persistentPedido.getIdEstado();
            Estado idEstadoNew = pedido.getIdEstado();
            List<Linhapedido> linhapedidoListOld = persistentPedido.getLinhapedidoList();
            List<Linhapedido> linhapedidoListNew = pedido.getLinhapedidoList();
            List<String> illegalOrphanMessages = null;
            for (Linhapedido linhapedidoListOldLinhapedido : linhapedidoListOld) {
                if (!linhapedidoListNew.contains(linhapedidoListOldLinhapedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linhapedido " + linhapedidoListOldLinhapedido + " since its pedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEntidadeNew != null) {
                idEntidadeNew = em.getReference(idEntidadeNew.getClass(), idEntidadeNew.getIdEntidade());
                pedido.setIdEntidade(idEntidadeNew);
            }
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                pedido.setIdEstado(idEstadoNew);
            }
            List<Linhapedido> attachedLinhapedidoListNew = new ArrayList<Linhapedido>();
            for (Linhapedido linhapedidoListNewLinhapedidoToAttach : linhapedidoListNew) {
                linhapedidoListNewLinhapedidoToAttach = em.getReference(linhapedidoListNewLinhapedidoToAttach.getClass(), linhapedidoListNewLinhapedidoToAttach.getLinhapedidoPK());
                attachedLinhapedidoListNew.add(linhapedidoListNewLinhapedidoToAttach);
            }
            linhapedidoListNew = attachedLinhapedidoListNew;
            pedido.setLinhapedidoList(linhapedidoListNew);
            pedido = em.merge(pedido);
            if (idEntidadeOld != null && !idEntidadeOld.equals(idEntidadeNew)) {
                idEntidadeOld.getPedidoList().remove(pedido);
                idEntidadeOld = em.merge(idEntidadeOld);
            }
            if (idEntidadeNew != null && !idEntidadeNew.equals(idEntidadeOld)) {
                idEntidadeNew.getPedidoList().add(pedido);
                idEntidadeNew = em.merge(idEntidadeNew);
            }
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getPedidoList().remove(pedido);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getPedidoList().add(pedido);
                idEstadoNew = em.merge(idEstadoNew);
            }
            for (Linhapedido linhapedidoListNewLinhapedido : linhapedidoListNew) {
                if (!linhapedidoListOld.contains(linhapedidoListNewLinhapedido)) {
                    Pedido oldPedidoOfLinhapedidoListNewLinhapedido = linhapedidoListNewLinhapedido.getPedido();
                    linhapedidoListNewLinhapedido.setPedido(pedido);
                    linhapedidoListNewLinhapedido = em.merge(linhapedidoListNewLinhapedido);
                    if (oldPedidoOfLinhapedidoListNewLinhapedido != null && !oldPedidoOfLinhapedidoListNewLinhapedido.equals(pedido)) {
                        oldPedidoOfLinhapedidoListNewLinhapedido.getLinhapedidoList().remove(linhapedidoListNewLinhapedido);
                        oldPedidoOfLinhapedidoListNewLinhapedido = em.merge(oldPedidoOfLinhapedidoListNewLinhapedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getCodpedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getCodpedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Linhapedido> linhapedidoListOrphanCheck = pedido.getLinhapedidoList();
            for (Linhapedido linhapedidoListOrphanCheckLinhapedido : linhapedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Linhapedido " + linhapedidoListOrphanCheckLinhapedido + " in its linhapedidoList field has a non-nullable pedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entidade idEntidade = pedido.getIdEntidade();
            if (idEntidade != null) {
                idEntidade.getPedidoList().remove(pedido);
                idEntidade = em.merge(idEntidade);
            }
            Estado idEstado = pedido.getIdEstado();
            if (idEstado != null) {
                idEstado.getPedidoList().remove(pedido);
                idEstado = em.merge(idEstado);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

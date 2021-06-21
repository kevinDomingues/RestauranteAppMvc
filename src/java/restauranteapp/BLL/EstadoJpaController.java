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
import restauranteapp.DAL.Encomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Estado;
import restauranteapp.DAL.Pedido;
import restauranteapp.DAL.Reserva;

/**
 *
 * @author kevin
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getEncomendaList() == null) {
            estado.setEncomendaList(new ArrayList<Encomenda>());
        }
        if (estado.getPedidoList() == null) {
            estado.setPedidoList(new ArrayList<Pedido>());
        }
        if (estado.getReservaList() == null) {
            estado.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Encomenda> attachedEncomendaList = new ArrayList<Encomenda>();
            for (Encomenda encomendaListEncomendaToAttach : estado.getEncomendaList()) {
                encomendaListEncomendaToAttach = em.getReference(encomendaListEncomendaToAttach.getClass(), encomendaListEncomendaToAttach.getIdEncomenda());
                attachedEncomendaList.add(encomendaListEncomendaToAttach);
            }
            estado.setEncomendaList(attachedEncomendaList);
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : estado.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getCodpedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            estado.setPedidoList(attachedPedidoList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : estado.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            estado.setReservaList(attachedReservaList);
            em.persist(estado);
            for (Encomenda encomendaListEncomenda : estado.getEncomendaList()) {
                Estado oldIdEstadoOfEncomendaListEncomenda = encomendaListEncomenda.getIdEstado();
                encomendaListEncomenda.setIdEstado(estado);
                encomendaListEncomenda = em.merge(encomendaListEncomenda);
                if (oldIdEstadoOfEncomendaListEncomenda != null) {
                    oldIdEstadoOfEncomendaListEncomenda.getEncomendaList().remove(encomendaListEncomenda);
                    oldIdEstadoOfEncomendaListEncomenda = em.merge(oldIdEstadoOfEncomendaListEncomenda);
                }
            }
            for (Pedido pedidoListPedido : estado.getPedidoList()) {
                Estado oldIdEstadoOfPedidoListPedido = pedidoListPedido.getIdEstado();
                pedidoListPedido.setIdEstado(estado);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldIdEstadoOfPedidoListPedido != null) {
                    oldIdEstadoOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldIdEstadoOfPedidoListPedido = em.merge(oldIdEstadoOfPedidoListPedido);
                }
            }
            for (Reserva reservaListReserva : estado.getReservaList()) {
                Estado oldIdEstadoOfReservaListReserva = reservaListReserva.getIdEstado();
                reservaListReserva.setIdEstado(estado);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldIdEstadoOfReservaListReserva != null) {
                    oldIdEstadoOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldIdEstadoOfReservaListReserva = em.merge(oldIdEstadoOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getIdEstado());
            List<Encomenda> encomendaListOld = persistentEstado.getEncomendaList();
            List<Encomenda> encomendaListNew = estado.getEncomendaList();
            List<Pedido> pedidoListOld = persistentEstado.getPedidoList();
            List<Pedido> pedidoListNew = estado.getPedidoList();
            List<Reserva> reservaListOld = persistentEstado.getReservaList();
            List<Reserva> reservaListNew = estado.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Encomenda encomendaListOldEncomenda : encomendaListOld) {
                if (!encomendaListNew.contains(encomendaListOldEncomenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomenda " + encomendaListOldEncomenda + " since its idEstado field is not nullable.");
                }
            }
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoListOldPedido + " since its idEstado field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its idEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Encomenda> attachedEncomendaListNew = new ArrayList<Encomenda>();
            for (Encomenda encomendaListNewEncomendaToAttach : encomendaListNew) {
                encomendaListNewEncomendaToAttach = em.getReference(encomendaListNewEncomendaToAttach.getClass(), encomendaListNewEncomendaToAttach.getIdEncomenda());
                attachedEncomendaListNew.add(encomendaListNewEncomendaToAttach);
            }
            encomendaListNew = attachedEncomendaListNew;
            estado.setEncomendaList(encomendaListNew);
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getCodpedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            estado.setPedidoList(pedidoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            estado.setReservaList(reservaListNew);
            estado = em.merge(estado);
            for (Encomenda encomendaListNewEncomenda : encomendaListNew) {
                if (!encomendaListOld.contains(encomendaListNewEncomenda)) {
                    Estado oldIdEstadoOfEncomendaListNewEncomenda = encomendaListNewEncomenda.getIdEstado();
                    encomendaListNewEncomenda.setIdEstado(estado);
                    encomendaListNewEncomenda = em.merge(encomendaListNewEncomenda);
                    if (oldIdEstadoOfEncomendaListNewEncomenda != null && !oldIdEstadoOfEncomendaListNewEncomenda.equals(estado)) {
                        oldIdEstadoOfEncomendaListNewEncomenda.getEncomendaList().remove(encomendaListNewEncomenda);
                        oldIdEstadoOfEncomendaListNewEncomenda = em.merge(oldIdEstadoOfEncomendaListNewEncomenda);
                    }
                }
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Estado oldIdEstadoOfPedidoListNewPedido = pedidoListNewPedido.getIdEstado();
                    pedidoListNewPedido.setIdEstado(estado);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldIdEstadoOfPedidoListNewPedido != null && !oldIdEstadoOfPedidoListNewPedido.equals(estado)) {
                        oldIdEstadoOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldIdEstadoOfPedidoListNewPedido = em.merge(oldIdEstadoOfPedidoListNewPedido);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Estado oldIdEstadoOfReservaListNewReserva = reservaListNewReserva.getIdEstado();
                    reservaListNewReserva.setIdEstado(estado);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldIdEstadoOfReservaListNewReserva != null && !oldIdEstadoOfReservaListNewReserva.equals(estado)) {
                        oldIdEstadoOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldIdEstadoOfReservaListNewReserva = em.merge(oldIdEstadoOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getIdEstado();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Encomenda> encomendaListOrphanCheck = estado.getEncomendaList();
            for (Encomenda encomendaListOrphanCheckEncomenda : encomendaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Encomenda " + encomendaListOrphanCheckEncomenda + " in its encomendaList field has a non-nullable idEstado field.");
            }
            List<Pedido> pedidoListOrphanCheck = estado.getPedidoList();
            for (Pedido pedidoListOrphanCheckPedido : pedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Pedido " + pedidoListOrphanCheckPedido + " in its pedidoList field has a non-nullable idEstado field.");
            }
            List<Reserva> reservaListOrphanCheck = estado.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable idEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

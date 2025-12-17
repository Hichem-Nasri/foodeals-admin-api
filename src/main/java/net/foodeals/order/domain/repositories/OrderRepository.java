package net.foodeals.order.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.TransactionStatus;
import net.foodeals.organizationEntity.domain.entities.Contact;
import net.foodeals.user.domain.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends BaseRepository<Order, UUID> {
	@Query("SELECT o FROM Order o WHERE o.offer.publisherInfo.id = :publisherId "
			+ "AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CAST(:orderDate AS timestamp)) "
			+ "AND EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CAST(:orderDate AS timestamp))")
	List<Order> findOrdersByPublisherIdAndOrderDate(@Param("publisherId") UUID publisherId,
			@Param("orderDate") Date orderDate);

	@Query("SELECT o FROM Order o WHERE o.offer.publisherInfo.id = :publisherId "
			+ "AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CAST(:orderDate AS timestamp)) "
			+ "AND EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CAST(:orderDate AS timestamp))")
	Page<Order> findOrdersByPublisherIdAndOrderDate(@Param("publisherId") UUID publisherId,
			@Param("orderDate") Date orderDate, Pageable pageable);

	@Query("SELECT o FROM Order o JOIN o.transaction t WHERE o.offer.publisherInfo.id = :publisherId "
			+ "AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CAST(:orderDate AS timestamp)) "
			+ "AND EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CAST(:orderDate AS timestamp)) "
			+ "AND o.status = :orderStatus " + "AND t.status = :transactionStatus")
	Page<Order> findOrdersByPublisherIdAndOrderDateAndStatusAndTransactionStatus(@Param("publisherId") UUID publisherId,
			@Param("orderDate") Date orderDate, @Param("orderStatus") OrderStatus orderStatus,
			@Param("transactionStatus") TransactionStatus transactionStatus, Pageable pageable);

	@Query("SELECT o FROM Order o JOIN o.transaction t WHERE o.offer.publisherInfo.id = :publisherId "
			+ "AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CAST(:orderDate AS timestamp)) "
			+ "AND EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CAST(:orderDate AS timestamp)) "
			+ "AND o.status = :orderStatus " + "AND t.status = :transactionStatus")
	List<Order> findOrdersByPublisherIdAndOrderDateAndStatusAndTransactionStatus(@Param("publisherId") UUID publisherId,
			@Param("orderDate") Date orderDate, @Param("orderStatus") OrderStatus orderStatus,
			@Param("transactionStatus") TransactionStatus transactionStatus);

	@Query("SELECT o FROM Order o " + "JOIN o.delivery d " + "JOIN d.deliveryBoy u "
			+ "WHERE u.organizationEntity.id = :organizationId " + "AND d.status = :deliveryStatus "
			+ "AND DATE(o.createdAt) = :orderDate " + "AND o.status = :orderStatus "
			+ "AND o.transaction.status = :transactionStatus")
	Page<Order> findOrdersByOrganizationAndDeliveryStatusAndCriteria(@Param("organizationId") UUID organizationId,
			@Param("deliveryStatus") DeliveryStatus deliveryStatus, @Param("orderDate") Date orderDate,
			@Param("orderStatus") OrderStatus orderStatus,
			@Param("transactionStatus") TransactionStatus transactionStatus, Pageable pageable);

	@Query("SELECT o FROM Order o " + "JOIN o.delivery d " + "JOIN d.deliveryBoy u "
			+ "WHERE u.organizationEntity.id = :organizationId " + "AND d.status = :deliveryStatus "
			+ "AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CAST(:orderDate AS timestamp)) "
			+ "AND EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CAST(:orderDate AS timestamp)) "
			+ "AND o.status = :orderStatus " + "AND o.transaction.status = :transactionStatus")
	List<Order> findOrdersByOrganizationAndDeliveryStatusAndCriteria(@Param("organizationId") UUID organizationId,
			@Param("deliveryStatus") DeliveryStatus deliveryStatus, @Param("orderDate") Date orderDate,
			@Param("orderStatus") OrderStatus orderStatus,
			@Param("transactionStatus") TransactionStatus transactionStatus);

	@EntityGraph(attributePaths = { "delivery" })
	@Query("SELECT o FROM Order o " + "WHERE o.delivery.id = :deliveryId")
	List<Order> findOrdersByDeliveryId(@Param("deliveryId") UUID deliveryId);

	long countBySeen(boolean seen);

	// Compte les commandes livrées/non livrées via leur statut de livraison
	@Query("SELECT COUNT(o) FROM Order o JOIN o.delivery d WHERE d.status = 'DELIVERED'")
	long countDeliveredOrders();

	@Query("SELECT COUNT(o) FROM Order o JOIN o.delivery d WHERE d.status != 'DELIVERED'")
	long countNotDeliveredOrders();

	// Requête pour récupérer les commandes paginées avec les jointures nécessaires
	@Query("SELECT o FROM Order o " + "LEFT JOIN FETCH o.offer off " + "LEFT JOIN FETCH off.box b " + // Si une offre
																										// peut être une
																										// Box
			"LEFT JOIN FETCH b.boxItems bi " + // Si la box contient des items
			"LEFT JOIN FETCH bi.product p " + // Les produits dans les box items
			"LEFT JOIN FETCH off.deal d " + // Si une offre peut être un Deal
			"LEFT JOIN FETCH d.product dp " + // Les produits dans les deals
			"LEFT JOIN FETCH o.client c " + // Le client (User)
			"LEFT JOIN FETCH o.clientPro cp " + // Le client pro (SubEntity)
			"LEFT JOIN FETCH o.delivery del " + // La livraison
			"LEFT JOIN FETCH del.deliveryPosition delPos " + // La position de livraison
			"LEFT JOIN FETCH o.donate don " + // Le don
			"LEFT JOIN FETCH don.donor orgDonor " + // L'organisation donneuse
			"LEFT JOIN FETCH don.receiver orgReceiver " + // L'organisation receveuse
			"LEFT JOIN FETCH off.organizationEntity offerOrg " + // L'organisation de l'offre
			"ORDER BY o.createdAt DESC") // Ou toute autre logique de tri
	Page<Order> findAllOrdersWithDetails(Pageable pageable);

	// Vous pourriez aussi avoir des méthodes pour rechercher par partenaire, etc.

	// Méthode pour récupérer une commande avec ses OrderItems (si vous avez une
	// telle entité)
	// Cela permet de charger les données nécessaires pour le DTO en une seule
	// requête.
	// @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH
	// oi.product p WHERE o.id = :id")
	// Optional<Order> findByIdWithDetails(UUID id);

	long countByStatus(OrderStatus status);

	long countByStatusNot(OrderStatus status);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :id")
	long countByClient(Integer id);

	@Query("SELECT o FROM Order o WHERE o.client.id = :id  ORDER BY o.createdAt DESC")
	Optional<Order> findTopByClientOrderByCreatedAtDesc(Integer id);

	Optional<Order> findFirstByClientIdOrderByCreatedAtDesc(Integer id);

	@Query("SELECT o FROM Order o WHERE o.client.id = :idClient")
	List<Order> findAllByClientId(Integer idClient);

	@Query("SELECT o FROM Order o WHERE o.client.id = :clientId AND o.id= :commandId")
	Optional<Order> findByIdAndClientId(@Param("commandId") UUID commandId, @Param("clientId") Integer clientId);

	List<Order> findByStatus(OrderStatus status);

	Page<Order> findAllByClientIsNotNull(Pageable pageable); // commandes “client particulier”

	Page<Order> findAllByClientProIsNotNull(Pageable pageable); // commandes “client pro”
	
	Page<Order> findByClient_Id(Integer clientId, Pageable pageable);

    // Pour un client Pro
    Page<Order> findByClientPro_Id(UUID clientProId, Pageable pageable);
    @Query("""
    	    SELECT o
    	    FROM Order o
    	    LEFT JOIN o.offer off
    	    LEFT JOIN Deal d ON d.offer.id = off.id
    	    LEFT JOIN Box b ON b.offer.id = off.id
    	    LEFT JOIN b.boxItems bi
    	    LEFT JOIN bi.product bp
    	    LEFT JOIN d.product dp
    	    WHERE (:name IS NULL 
    	           OR LOWER(dp.name) LIKE LOWER(CONCAT('%', :name, '%')) 
    	           OR LOWER(bp.name) LIKE LOWER(CONCAT('%', :name, '%')))
    	""")
    	Page<Order> searchOrdersByProductName(@Param("name") String name, Pageable pageable);



}

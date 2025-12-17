package net.foodeals.notification.domain.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "device_tokens ")
public class DeviceToken {
	
	 @Id
	    @GeneratedValue(generator = "UUID")
	    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	    private UUID id;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	    @Column(length = 500, nullable = false)
	    private String token;

	    private String deviceType; // 'ios', 'android', 'web'

	    @JdbcTypeCode(SqlTypes.JSON)
	    @Column( columnDefinition = "jsonb")
	    private List<String> deviceInfo;

	    private boolean isActive = true;

}

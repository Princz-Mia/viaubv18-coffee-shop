package com.princz_mia.viaubv18_coffee_shop.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.princz_mia.viaubv18_coffee_shop.exceptions.AppException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt, updatedAt" }, allowGetters = true)
public abstract class Auditable {

    @Id
    @SequenceGenerator(
            name = "primary_key_seq",
            sequenceName = "primary_key_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_key_seq"
    )
    private Long id;

    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();

    @NotNull
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    @NotNull
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        Long userId = RequestContext.getUserId();
        if (userId == null)
            throw new AppException("Cannot persist entity without user ID in RequestContext for this thread.");
        setCreatedAt(LocalDateTime.now());
        setCreatedBy(userId);
        //setUpdatedAt(LocalDateTime.now());
        //setUpdatedBy(userId);
    }

    @PreUpdate
    public void beforeUpdate() {
        Long userId = RequestContext.getUserId();
        if (userId == null)
            throw new AppException("Cannot update entity without user ID in RequestContext for this thread.");
        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(userId);
    }
}

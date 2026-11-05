package com.pradeep.smart_parking_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
@Table(name = "parking_slots")
public class ParkingSlot {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String slotNumber;
   private String status;
   private String location;

   // Optimistic locking: Hibernate bumps this on every update
   @Version
   @Column(nullable = false, columnDefinition = "bigint default 0")
   private Long version = 0L;
}
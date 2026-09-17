package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Reserva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservaRepository : JpaRepository<Reserva, Long>

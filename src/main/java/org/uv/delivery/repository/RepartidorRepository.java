/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.uv.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uv.delivery.models.usuario.Repartidor;

/**
 *
 * @author juan
 */
public interface RepartidorRepository extends JpaRepository<Repartidor, Long>{
    
}

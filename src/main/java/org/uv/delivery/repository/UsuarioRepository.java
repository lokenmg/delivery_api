/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uv.delivery.models.usuario.UsuarioBase;

/**
 *
 * @author juan
 */
public interface UsuarioRepository extends JpaRepository<UsuarioBase, Long>{
    public UsuarioBase findByEmail(String email);
}
